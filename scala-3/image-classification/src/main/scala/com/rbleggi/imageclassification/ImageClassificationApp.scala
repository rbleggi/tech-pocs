package com.rbleggi.imageclassification

import ai.djl.ndarray.{NDArray, NDManager}
import ai.djl.modality.cv.{Image, ImageFactory}
import com.rbleggi.imageclassification.augmentation.DataAugmentation
import com.rbleggi.imageclassification.core.CNNClassifier
import com.rbleggi.imageclassification.model.{AugmentationType, TrainingConfig}
import com.rbleggi.imageclassification.transfer.{PretrainedModel, TransferLearning}

@main def runImageClassification(): Unit =
  println("=== Image Classification System with CNNs ===\n")

  val manager = NDManager.newBaseManager()

  demonstrateCNNClassifier(manager)
  demonstrateDataAugmentation(manager)
  demonstrateTransferLearning(manager)

  manager.close()

def demonstrateCNNClassifier(manager: NDManager): Unit =
  println("1. CNN Classifier Demo")
  println("-" * 50)

  val classes = List("cat", "dog", "bird")
  val classifier = CNNClassifier(
    numClasses = classes.size,
    inputShape = Array(3, 64, 64),
    classes = classes
  )

  classifier.initializeModel()
  println(s"✓ Initialized CNN model with ${classes.size} classes")
  println(s"✓ Input shape: [3, 64, 64] (RGB 64x64 images)")
  println("✓ Architecture: 3 Conv layers (32, 64, 128 filters) + 2 FC layers")

  val syntheticData = (1 to 10).map { i =>
    val image = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val label = i % classes.size
    (image, label)
  }.toList

  val config = TrainingConfig(
    epochs = 2,
    batchSize = 4,
    learningRate = 0.001f,
    useAugmentation = false
  )

  println(s"\n✓ Training on ${syntheticData.size} synthetic samples...")
  val metrics = classifier.train(syntheticData, config, manager)

  metrics.foreach { m =>
    println(f"  Epoch ${m.epoch}: Accuracy=${m.accuracy}%.4f, Loss=${m.loss}%.4f")
  }

  val testImage = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
  val result = classifier.predict(testImage, manager)

  println(s"\n✓ Prediction: ${result.predictedLabel} (${(result.confidence * 100).toInt}% confidence)")
  println("  All probabilities:")
  result.allProbabilities.foreach { case (label, prob) =>
    println(f"    $label: ${prob * 100}%.2f%%")
  }

  syntheticData.foreach(_._1.close())
  testImage.close()
  classifier.close()

  println()

def demonstrateDataAugmentation(manager: NDManager): Unit =
  println("2. Data Augmentation Demo")
  println("-" * 50)

  val augmentation = DataAugmentation(manager)

  val originalImage = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
  println(s"✓ Original image shape: ${originalImage.getShape}")

  val augmentations = List(
    AugmentationType.HorizontalFlip,
    AugmentationType.Rotation,
    AugmentationType.Brightness,
    AugmentationType.Contrast
  )

  println("\n✓ Applying augmentations:")
  augmentations.foreach { aug =>
    val augmented = augmentation.applyAugmentation(originalImage, aug)
    println(s"  - ${aug.toString}: shape=${augmented.getShape}")
    augmented.close()
  }

  val multiAugmented = augmentation.applyMultipleAugmentations(originalImage, augmentations)
  println(s"\n✓ Multiple augmentations applied: ${augmentations.size} transformations")
  println(s"  Final shape: ${multiAugmented.getShape}")

  originalImage.close()
  multiAugmented.close()

  println()

def demonstrateTransferLearning(manager: NDManager): Unit =
  println("3. Transfer Learning Demo")
  println("-" * 50)

  val classes = List("flower", "tree", "grass")
  val transferLearning = TransferLearning(
    numClasses = classes.size,
    classes = classes,
    pretrainedModel = PretrainedModel.ResNet50
  )

  println(s"✓ Initialized transfer learning with ResNet50")
  println(s"✓ Number of classes: ${classes.size}")
  println("✓ Feature extractor: ResNet50 (2048 features)")
  println("✓ Classifier: 2-layer FC network")

  println("\n✓ Transfer learning workflow:")
  println("  1. Load pretrained model (ResNet50)")
  println("  2. Freeze feature extractor layers")
  println("  3. Train only the classifier head")
  println("  4. Optional: Unfreeze and fine-tune all layers")

  println("\n✓ Available pretrained models:")
  PretrainedModel.values.foreach { model =>
    val features = model match
      case PretrainedModel.ResNet50 => 2048
      case PretrainedModel.MobileNetV2 => 1280
      case PretrainedModel.SqueezeNet => 512
    println(s"  - ${model.toString}: ${features} features")
  }

  transferLearning.close()

  println()
