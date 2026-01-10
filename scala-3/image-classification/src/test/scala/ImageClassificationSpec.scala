package com.rbleggi.imageclassification

import ai.djl.ndarray.NDManager
import com.rbleggi.imageclassification.augmentation.DataAugmentation
import com.rbleggi.imageclassification.core.CNNClassifier
import com.rbleggi.imageclassification.model.{AugmentationType, TrainingConfig}
import com.rbleggi.imageclassification.transfer.{PretrainedModel, TransferLearning}
import org.scalatest.funsuite.AnyFunSuite

class ImageClassificationSpec extends AnyFunSuite:

  test("CNNClassifier should initialize model with correct architecture"):
    val manager = NDManager.newBaseManager()
    val classes = List("cat", "dog", "bird")
    val classifier = CNNClassifier(
      numClasses = classes.size,
      inputShape = Array(3, 64, 64),
      classes = classes
    )

    classifier.initializeModel()
    val block = classifier.buildModel()

    assert(block != null)
    classifier.close()
    manager.close()

  test("CNNClassifier should train on synthetic data"):
    val manager = NDManager.newBaseManager()
    val classes = List("class1", "class2")
    val classifier = CNNClassifier(
      numClasses = classes.size,
      inputShape = Array(3, 32, 32),
      classes = classes
    )

    classifier.initializeModel()

    val trainData = (1 to 8).map { i =>
      val image = manager.randomUniform(0f, 1f, ai.djl.ndarray.types.Shape.create(3, 32, 32))
      val label = i % 2
      (image, label)
    }.toList

    val config = TrainingConfig(
      epochs = 1,
      batchSize = 4,
      learningRate = 0.01f,
      useAugmentation = false
    )

    val metrics = classifier.train(trainData, config, manager)

    assert(metrics.nonEmpty)
    assert(metrics.head.epoch == 0)

    trainData.foreach(_._1.close())
    classifier.close()
    manager.close()

  test("CNNClassifier should predict class for input image"):
    val manager = NDManager.newBaseManager()
    val classes = List("cat", "dog")
    val classifier = CNNClassifier(
      numClasses = classes.size,
      inputShape = Array(3, 32, 32),
      classes = classes
    )

    classifier.initializeModel()

    val trainData = (1 to 8).map { i =>
      val image = manager.randomUniform(0f, 1f, ai.djl.ndarray.types.Shape.create(3, 32, 32))
      val label = i % 2
      (image, label)
    }.toList

    val config = TrainingConfig(
      epochs = 1,
      batchSize = 4,
      learningRate = 0.01f,
      useAugmentation = false
    )

    classifier.train(trainData, config, manager)

    val testImage = manager.randomUniform(0f, 1f, ai.djl.ndarray.types.Shape.create(3, 32, 32))
    val result = classifier.predict(testImage, manager)

    assert(classes.contains(result.predictedLabel))
    assert(result.confidence >= 0f && result.confidence <= 1f)
    assert(result.allProbabilities.size == classes.size)

    trainData.foreach(_._1.close())
    testImage.close()
    classifier.close()
    manager.close()

  test("DataAugmentation should apply horizontal flip"):
    val manager = NDManager.newBaseManager()
    val augmentation = DataAugmentation(manager)

    val image = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val flipped = augmentation.applyAugmentation(image, AugmentationType.HorizontalFlip)

    assert(flipped.getShape == image.getShape)

    image.close()
    flipped.close()
    manager.close()

  test("DataAugmentation should apply vertical flip"):
    val manager = NDManager.newBaseManager()
    val augmentation = DataAugmentation(manager)

    val image = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val flipped = augmentation.applyAugmentation(image, AugmentationType.VerticalFlip)

    assert(flipped.getShape == image.getShape)

    image.close()
    flipped.close()
    manager.close()

  test("DataAugmentation should apply brightness adjustment"):
    val manager = NDManager.newBaseManager()
    val augmentation = DataAugmentation(manager)

    val image = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val adjusted = augmentation.applyAugmentation(image, AugmentationType.Brightness)

    assert(adjusted.getShape == image.getShape)

    image.close()
    adjusted.close()
    manager.close()

  test("DataAugmentation should apply contrast adjustment"):
    val manager = NDManager.newBaseManager()
    val augmentation = DataAugmentation(manager)

    val image = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val adjusted = augmentation.applyAugmentation(image, AugmentationType.Contrast)

    assert(adjusted.getShape == image.getShape)

    image.close()
    adjusted.close()
    manager.close()

  test("DataAugmentation should apply multiple augmentations"):
    val manager = NDManager.newBaseManager()
    val augmentation = DataAugmentation(manager)

    val image = manager.randomUniform(0f, 255f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val augmentations = List(
      AugmentationType.HorizontalFlip,
      AugmentationType.Brightness
    )

    val result = augmentation.applyMultipleAugmentations(image, augmentations)

    assert(result != null)

    image.close()
    result.close()
    manager.close()

  test("TransferLearning should initialize with ResNet50"):
    val classes = List("flower", "tree")
    val transferLearning = TransferLearning(
      numClasses = classes.size,
      classes = classes,
      pretrainedModel = PretrainedModel.ResNet50
    )

    assert(transferLearning.numClasses == 2)
    assert(transferLearning.classes == classes)

    transferLearning.close()

  test("TransferLearning should initialize with MobileNetV2"):
    val classes = List("cat", "dog", "bird")
    val transferLearning = TransferLearning(
      numClasses = classes.size,
      classes = classes,
      pretrainedModel = PretrainedModel.MobileNetV2
    )

    assert(transferLearning.numClasses == 3)
    assert(transferLearning.classes == classes)

    transferLearning.close()

  test("TransferLearning should build classifier block"):
    val classes = List("class1", "class2")
    val transferLearning = TransferLearning(
      numClasses = classes.size,
      classes = classes,
      pretrainedModel = PretrainedModel.ResNet50
    )

    val block = transferLearning.buildClassifier()

    assert(block != null)

    transferLearning.close()

  test("TrainingConfig should be created with correct parameters"):
    val config = TrainingConfig(
      epochs = 10,
      batchSize = 32,
      learningRate = 0.001f,
      useAugmentation = true
    )

    assert(config.epochs == 10)
    assert(config.batchSize == 32)
    assert(config.learningRate == 0.001f)
    assert(config.useAugmentation == true)

  test("AugmentationType should have all expected types"):
    val types = AugmentationType.values.toList

    assert(types.contains(AugmentationType.HorizontalFlip))
    assert(types.contains(AugmentationType.VerticalFlip))
    assert(types.contains(AugmentationType.Rotation))
    assert(types.contains(AugmentationType.Brightness))
    assert(types.contains(AugmentationType.Contrast))
    assert(types.contains(AugmentationType.Crop))
    assert(types.contains(AugmentationType.Zoom))

  test("PretrainedModel should have all expected models"):
    val models = PretrainedModel.values.toList

    assert(models.contains(PretrainedModel.ResNet50))
    assert(models.contains(PretrainedModel.MobileNetV2))
    assert(models.contains(PretrainedModel.SqueezeNet))
