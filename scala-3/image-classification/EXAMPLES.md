# Image Classification Examples

## Quick Start Examples

### 1. Simple Classifier

```scala
import ai.djl.ndarray.NDManager
import com.rbleggi.imageclassification.SimpleImageClassifier

val manager = NDManager.newBaseManager()
val classes = List("cat", "dog", "bird")
val classifier = SimpleImageClassifier(classes, manager)

val trainImages = (1 to 12).map { _ =>
  manager.randomUniform(0f, 1f, new Shape(3, 64, 64))
}.toList
val trainLabels = List(0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2)

classifier.train(trainImages, trainLabels, epochs = 5, learningRate = 0.01f)

val testImage = manager.randomUniform(0f, 1f, new Shape(3, 64, 64))
val result = classifier.predict(testImage)
println(s"Predicted: ${result.label} (${result.confidence * 100}% confidence)")
```

### 2. Data Augmentation

```scala
import com.rbleggi.imageclassification.augmentation.DataAugmentation
import com.rbleggi.imageclassification.model.AugmentationType

val augmentation = DataAugmentation(manager)
val image = manager.randomUniform(0f, 255f, new Shape(3, 64, 64))

val flipped = augmentation.applyAugmentation(image, AugmentationType.HorizontalFlip)
val rotated = augmentation.applyAugmentation(image, AugmentationType.Rotation)
val brightened = augmentation.applyAugmentation(image, AugmentationType.Brightness)

val multiAugmented = augmentation.applyMultipleAugmentations(
  image,
  List(AugmentationType.HorizontalFlip, AugmentationType.Brightness)
)
```

### 3. Learning Rate Scheduling

```scala
import com.rbleggi.imageclassification.utils.LearningRateScheduler

val stepDecay = LearningRateScheduler.stepDecay(0.1f, dropRate = 0.5f, epochsDrop = 10)
val cosineAnnealing = LearningRateScheduler.cosineAnnealing(0.1f, minLR = 0.001f, totalEpochs = 100)
val exponential = LearningRateScheduler.exponentialDecay(0.1f, decayRate = 0.1f)

(0 until 50).foreach { epoch =>
  val lr = stepDecay.getLearningRate(epoch)
  println(f"Epoch $epoch: LR = $lr%.6f")
}
```

### 4. Early Stopping

```scala
import com.rbleggi.imageclassification.utils.EarlyStopping

val earlyStopping = EarlyStopping(patience = 5, minDelta = 0.001f)

var epoch = 0
var shouldContinue = true

while shouldContinue && epoch < 100 do
  val validationLoss = trainEpoch()
  earlyStopping.checkImprovement(validationLoss, higherIsBetter = false)

  if earlyStopping.shouldStopTraining then
    println(s"Early stopping at epoch $epoch")
    shouldContinue = false

  epoch += 1
```

### 5. Evaluation Metrics

```scala
import com.rbleggi.imageclassification.evaluation.MetricsCalculator

val metricsCalc = MetricsCalculator()

val predictions = List(0, 1, 2, 0, 1, 2, 1, 0)
val actuals = List(0, 1, 1, 0, 2, 2, 1, 1)

val multiMetrics = metricsCalc.calculateMultiClassMetrics(predictions, actuals, numClasses = 3)

println(f"Accuracy: ${multiMetrics.accuracy * 100}%.2f%%")
println(f"Macro F1: ${multiMetrics.macroAverageF1 * 100}%.2f%%")

val precisions = multiMetrics.precisionPerClass
val recalls = multiMetrics.recallPerClass
```

### 6. Data Loading and Batching

```scala
import com.rbleggi.imageclassification.utils.DataLoader

val loader = DataLoader(manager)

val dataset = loader.loadSyntheticDataset(
  numSamples = 100,
  numClasses = 5,
  imageShape = new Shape(3, 64, 64)
)

val (trainSet, testSet) = loader.splitDataset(dataset, trainRatio = 0.8f)

val batches = loader.createBatches(trainSet.images, trainSet.labels, batchSize = 32)

batches.foreach { case (images, labels) =>
  trainBatch(images, labels)
}
```

### 7. Image Preprocessing

```scala
import com.rbleggi.imageclassification.preprocessing.ImagePreprocessor

val preprocessor = ImagePreprocessor(manager)

val image = manager.randomUniform(0f, 255f, new Shape(3, 224, 224))

val normalized = preprocessor.normalize(
  image,
  ImagePreprocessor.ImageNetMean,
  ImagePreprocessor.ImageNetStd
)

val resized = preprocessor.resize(image, targetHeight = 128, targetWidth = 128)

val cropped = preprocessor.centerCrop(image, cropHeight = 100, cropWidth = 100)

val grayscale = preprocessor.toGrayscale(image)

val processed = preprocessor.applyPreprocessingPipeline(
  image,
  resize = Some((128, 128)),
  normalize = Some((ImagePreprocessor.ImageNetMean, ImagePreprocessor.ImageNetStd)),
  centerCrop = Some((100, 100))
)
```

### 8. Complete Training Pipeline

```scala
import com.rbleggi.imageclassification.core.CNNClassifier
import com.rbleggi.imageclassification.model.TrainingConfig

val classes = List("cat", "dog", "bird")
val classifier = CNNClassifier(
  numClasses = classes.size,
  inputShape = Array(3, 64, 64),
  classes = classes
)

classifier.initializeModel()

val loader = DataLoader(manager)
val dataset = loader.loadSyntheticDataset(100, classes.size, new Shape(3, 64, 64))
val (trainSet, testSet) = loader.splitDataset(dataset, 0.8f)

val trainData = trainSet.images.zip(trainSet.labels)

val config = TrainingConfig(
  epochs = 20,
  batchSize = 16,
  learningRate = 0.001f,
  useAugmentation = true
)

val metrics = classifier.train(trainData, config, manager)

val testImage = testSet.images.head
val result = classifier.predict(testImage, manager)

println(s"Predicted: ${result.predictedLabel}")
println(f"Confidence: ${result.confidence * 100}%.2f%%")
```

### 9. Visualization

```scala
import com.rbleggi.imageclassification.utils.Visualizer

val visualizer = Visualizer()

visualizer.printTrainingMetrics(metrics)
visualizer.printClassificationResult(result)

val predictions = testSet.images.map(img => classifier.predict(img, manager).predictedLabel)
val predictedIndices = predictions.map(classes.indexOf)

val multiMetrics = MetricsCalculator().calculateMultiClassMetrics(
  predictedIndices,
  testSet.labels,
  classes.size
)

visualizer.printMultiClassMetrics(multiMetrics, classes)
visualizer.printConfusionMatrix(multiMetrics.confusionMatrix, classes)
```

### 10. Model Persistence

```scala
import com.rbleggi.imageclassification.utils.ModelPersistence
import java.nio.file.Paths

val persistence = ModelPersistence()

val weightsPath = Paths.get("models/classifier_weights.bin")
persistence.saveWeights(weights, bias, weightsPath)

val (loadedWeights, loadedBias) = persistence.loadWeights(weightsPath, manager)

if persistence.modelExists(weightsPath) then
  println("Model weights found!")
```

## Running Examples

All examples can be run using:

```bash
./sbtw compile run
```

To run the simple classifier:

```bash
./sbtw "runMain com.rbleggi.imageclassification.runSimpleClassifier"
```

To run the full demo:

```bash
./sbtw "runMain com.rbleggi.imageclassification.runImageClassification"
```
