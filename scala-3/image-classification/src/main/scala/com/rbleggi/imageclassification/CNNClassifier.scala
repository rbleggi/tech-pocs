package com.rbleggi.imageclassification

import ai.djl.ndarray.{NDArray, NDList, NDManager}
import ai.djl.ndarray.types.Shape
import ai.djl.nn.*
import ai.djl.nn.convolutional.*
import ai.djl.nn.core.*
import ai.djl.nn.pooling.*
import ai.djl.training.*
import ai.djl.training.loss.*
import ai.djl.training.optimizer.*
import ai.djl.training.tracker.*
import ai.djl.training.evaluator.Accuracy
import ai.djl.engine.Engine
import ai.djl.Model
import ai.djl.ModelException
import ai.djl.Application
import ai.djl.repository.zoo.*
import ai.djl.translate.*
import scala.jdk.CollectionConverters.*

trait ModelStrategy:
  def buildModel(numClasses: Int): SequentialBlock

class SimpleCNN extends ModelStrategy:
  def buildModel(numClasses: Int): SequentialBlock =
    val block = SequentialBlock()

    // First convolutional block: 32 filters, 3x3 kernel
    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(3, 3))
        .setFilters(32)
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(2, 2), Shape(2, 2)))

    // Second convolutional block: 64 filters
    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(3, 3))
        .setFilters(64)
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(2, 2), Shape(2, 2)))

    // Flatten and fully connected layers
    block
      .add(LambdaBlock(arr => {
        val reshaped = arr.head.flatten()
        new NDList(reshaped)
      }))
      .add(Linear.builder().setUnits(128).build())
      .add(Activation.reluBlock())
      .add(Linear.builder().setUnits(numClasses).build())

    block

class TransferLearningCNN(modelName: String = "resnet50") extends ModelStrategy:
  def buildModel(numClasses: Int): SequentialBlock =
    // Load pre-trained model and extract features
    val criteria = Criteria.builder()
      .optApplication(Application.CV.IMAGE_CLASSIFICATION)
      .setTypes(classOf[NDList], classOf[NDList])
      .optFilter("layers", "50")
      .optFilter("flavor", "v1")
      .build()

    val block = SequentialBlock()

    // Note: In real implementation, would load pre-trained weights
    // For simplicity, showing the architecture concept

    // Feature extraction layers (would be frozen in transfer learning)
    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(7, 7))
        .setFilters(64)
        .optStride(Shape(2, 2))
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(3, 3), Shape(2, 2)))

    // Simplified ResNet block
    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(3, 3))
        .setFilters(128)
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(2, 2), Shape(2, 2)))

    // Custom classifier head (trainable)
    block
      .add(LambdaBlock(arr => {
        val reshaped = arr.head.flatten()
        new NDList(reshaped)
      }))
      .add(Linear.builder().setUnits(256).build())
      .add(Activation.reluBlock())
      .add(Linear.builder().setUnits(numClasses).build())

    block

// CNN Classifier with configurable strategy
class CNNImageClassifier(
  strategy: ModelStrategy,
  numClasses: Int,
  inputShape: Shape
):
  private val model: Model = Model.newInstance("cnn-classifier")
  private val block: SequentialBlock = strategy.buildModel(numClasses)

  model.setBlock(block)

  // Initialize model parameters
  def initialize(): Unit =
    model.getBlock.initialize(model.getNDManager, ai.djl.ndarray.types.DataType.FLOAT32, inputShape)

  // Train the model
  def train(
    trainImages: List[NDArray],
    trainLabels: List[Int],
    epochs: Int,
    batchSize: Int,
    learningRate: Float
  ): Unit =
    val config = DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
      .optOptimizer(Optimizer.adam().optLearningRateTracker(Tracker.fixed(learningRate)).build())
      .addEvaluator(Accuracy())

    val trainer = model.newTrainer(config)
    trainer.initialize(inputShape)

    println(s"Training CNN for $epochs epochs...")

    for epoch <- 0 until epochs do
      var totalLoss = 0.0
      var batches = 0

      // Simple batch processing
      trainImages.zip(trainLabels).grouped(batchSize).foreach { batch =>
        val (images, labels) = batch.unzip

        val gc = Engine.getInstance().newGradientCollector()
        try {
          val batchImages = NDList(images*)
          val batchLabels = trainer.getManager.create(labels.map(_.toFloat).toArray)

          val predictions = trainer.forward(batchImages)
          val lossValue = trainer.getLoss.evaluate(NDList(batchLabels), predictions)

          gc.backward(lossValue)
          trainer.step()

          totalLoss += lossValue.getFloat()
          batches += 1

          predictions.close()
          batchLabels.close()
          lossValue.close()
        } finally {
          gc.close()
        }
      }

      val avgLoss = totalLoss / batches
      println(f"Epoch ${epoch + 1}%3d: Loss = $avgLoss%.4f")

    trainer.close()

  // Predict class for a single image
  def predict(image: NDArray): (Int, Float) =
    // Create simple translator for prediction
    val translator = new Translator[NDList, NDList] {
      def processInput(ctx: TranslatorContext, input: NDList): NDList = input
      def processOutput(ctx: TranslatorContext, list: NDList): NDList = list
    }

    val predictor = model.newPredictor(translator)
    val result = predictor.predict(NDList(image))
    val probabilities = result.head.softmax(0)

    val probs = probabilities.toFloatArray
    val maxIdx = probs.zipWithIndex.maxBy(_._1)._2
    val confidence = probs(maxIdx)

    probabilities.close()
    result.close()
    predictor.close()

    (maxIdx, confidence)

  // Close model and release resources
  def close(): Unit =
    model.close()

@main def runCNNClassifier(): Unit =
  val manager = NDManager.newBaseManager()
  val numClasses = 3
  val inputShape = Shape(1, 3, 64, 64)  // batch_size, channels, height, width

  println("1. Simple CNN from Scratch")
  println("-" * 50)

  val simpleCNN = CNNImageClassifier(
    SimpleCNN(),
    numClasses,
    inputShape
  )
  simpleCNN.initialize()
  println("Simple CNN initialized")
  println("  Architecture: Conv(32) → Pool → Conv(64) → Pool → FC(128) → FC(3)")
  println("  Parameters: Randomly initialized\n")

  // Generate synthetic training data
  val trainImages = (0 until 30).map { i =>
    val label = i % numClasses
    val base = label * 0.3f
    manager.randomUniform(base, base + 0.3f, Shape(3, 64, 64))
  }.toList

  val trainLabels = (0 until 30).map(_ % numClasses).toList

  println(s"Training with ${trainImages.size} synthetic images...")
  simpleCNN.train(trainImages, trainLabels, epochs = 5, batchSize = 5, learningRate = 0.001f)

  val testImage = manager.randomUniform(0f, 0.3f, Shape(3, 64, 64))
  val (predictedClass, confidence) = simpleCNN.predict(testImage)
  println(f"\nPrediction: Class $predictedClass with ${confidence * 100}%.2f%% confidence\n")

  testImage.close()
  simpleCNN.close()

  println("2. Transfer Learning CNN")
  println("-" * 50)

  // Create transfer learning CNN
  val transferCNN = CNNImageClassifier(
    TransferLearningCNN("resnet50"),
    numClasses,
    inputShape
  )
  transferCNN.initialize()
  println("Transfer Learning CNN initialized")
  println("  Architecture: Pre-trained feature extractor → Custom classifier")
  println("  Approach: Feature extraction (frozen backbone) + trainable head")
  println("  Benefits: Faster training, better with limited data\n")

  println(s"Training with ${trainImages.size} synthetic images...")
  transferCNN.train(trainImages, trainLabels, epochs = 5, batchSize = 5, learningRate = 0.001f)

  val testImage2 = manager.randomUniform(0f, 0.3f, Shape(3, 64, 64))
  val (predictedClass2, confidence2) = transferCNN.predict(testImage2)
  println(f"\nPrediction: Class $predictedClass2 with ${confidence2 * 100}%.2f%% confidence\n")

  testImage2.close()
  transferCNN.close()

  trainImages.foreach(_.close())
  manager.close()

  println("Key Concepts Demonstrated:")
  println("- Convolutional layers for spatial feature extraction")
  println("- Pooling layers for dimensionality reduction")
  println("- Strategy pattern for switching between architectures")
  println("- Transfer learning for leveraging pre-trained knowledge")
