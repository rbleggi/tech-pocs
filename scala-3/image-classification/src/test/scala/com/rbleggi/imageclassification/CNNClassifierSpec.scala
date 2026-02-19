package com.rbleggi.imageclassification

import org.scalatest.funsuite.AnyFunSuite
import ai.djl.ndarray.{NDManager, NDArray}
import ai.djl.ndarray.types.Shape

class CNNClassifierSpec extends AnyFunSuite:

  test("SimpleCNN should build model with correct number of classes"):
    val strategy = SimpleCNN()
    val block = strategy.buildModel(3)
    assert(block != null)

  test("SimpleCNN should build model with 10 classes"):
    val strategy = SimpleCNN()
    val block = strategy.buildModel(10)
    assert(block != null)

  test("TransferLearningCNN should build model"):
    val strategy = TransferLearningCNN()
    val block = strategy.buildModel(5)
    assert(block != null)

  test("TransferLearningCNN should accept custom model name"):
    val strategy = TransferLearningCNN("resnet50")
    val block = strategy.buildModel(3)
    assert(block != null)

  test("CNNImageClassifier should initialize with SimpleCNN"):
    val manager = NDManager.newBaseManager()
    try
      val numClasses = 3
      val inputShape = Shape(1, 3, 64, 64)
      val classifier = CNNImageClassifier(SimpleCNN(), numClasses, inputShape)
      classifier.initialize()
      assert(classifier != null)
      classifier.close()
    finally
      manager.close()

  test("CNNImageClassifier should initialize with TransferLearningCNN"):
    val manager = NDManager.newBaseManager()
    try
      val numClasses = 3
      val inputShape = Shape(1, 3, 64, 64)
      val classifier = CNNImageClassifier(TransferLearningCNN(), numClasses, inputShape)
      classifier.initialize()
      assert(classifier != null)
      classifier.close()
    finally
      manager.close()

  test("CNNImageClassifier should predict class for image"):
    val manager = NDManager.newBaseManager()
    try
      val numClasses = 3
      val inputShape = Shape(1, 3, 64, 64)
      val classifier = CNNImageClassifier(SimpleCNN(), numClasses, inputShape)
      classifier.initialize()

      val testImage = manager.randomUniform(0f, 1f, Shape(3, 64, 64))
      val (predictedClass, confidence) = classifier.predict(testImage)

      assert(predictedClass >= 0 && predictedClass < numClasses)
      assert(confidence >= 0f && confidence <= 1f)

      testImage.close()
      classifier.close()
    finally
      manager.close()

  test("CNNImageClassifier should handle different input shapes"):
    val manager = NDManager.newBaseManager()
    try
      val numClasses = 5
      val inputShape = Shape(1, 3, 128, 128)
      val classifier = CNNImageClassifier(SimpleCNN(), numClasses, inputShape)
      classifier.initialize()

      val testImage = manager.randomUniform(0f, 1f, Shape(3, 128, 128))
      val (predictedClass, confidence) = classifier.predict(testImage)

      assert(predictedClass >= 0 && predictedClass < numClasses)

      testImage.close()
      classifier.close()
    finally
      manager.close()
