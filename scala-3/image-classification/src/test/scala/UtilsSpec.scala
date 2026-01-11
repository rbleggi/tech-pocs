package com.rbleggi.imageclassification

import ai.djl.ndarray.NDManager
import com.rbleggi.imageclassification.utils._
import org.scalatest.funsuite.AnyFunSuite

class UtilsSpec extends AnyFunSuite:

  test("DataLoader should create synthetic dataset"):
    val manager = NDManager.newBaseManager()
    val loader = DataLoader(manager)

    val dataset = loader.loadSyntheticDataset(
      numSamples = 10,
      numClasses = 3,
      imageShape = ai.djl.ndarray.types.Shape.create(3, 32, 32)
    )

    assert(dataset.images.size == 10)
    assert(dataset.labels.size == 10)
    assert(dataset.classNames.size == 3)

    dataset.images.foreach(_.close())
    manager.close()

  test("DataLoader should split dataset"):
    val manager = NDManager.newBaseManager()
    val loader = DataLoader(manager)

    val dataset = loader.loadSyntheticDataset(
      numSamples = 100,
      numClasses = 2,
      imageShape = ai.djl.ndarray.types.Shape.create(3, 32, 32)
    )

    val (trainSet, testSet) = loader.splitDataset(dataset, 0.8f)

    assert(trainSet.images.size == 80)
    assert(testSet.images.size == 20)

    dataset.images.foreach(_.close())
    manager.close()

  test("DataLoader should create batches"):
    val manager = NDManager.newBaseManager()
    val loader = DataLoader(manager)

    val images = (1 to 10).map(_ => manager.randomUniform(0f, 1f, ai.djl.ndarray.types.Shape.create(3, 32, 32))).toList
    val labels = (1 to 10).map(_ % 2).toList

    val batches = loader.createBatches(images, labels, batchSize = 4)

    assert(batches.size == 3)
    assert(batches(0)._1.size == 4)
    assert(batches(1)._1.size == 4)
    assert(batches(2)._1.size == 2)

    images.foreach(_.close())
    manager.close()

  test("LearningRateScheduler constant should return same rate"):
    val scheduler = LearningRateScheduler.constant(0.01f)

    assert(scheduler.getLearningRate(0) == 0.01f)
    assert(scheduler.getLearningRate(10) == 0.01f)
    assert(scheduler.getLearningRate(100) == 0.01f)

  test("LearningRateScheduler stepDecay should decrease rate"):
    val scheduler = LearningRateScheduler.stepDecay(0.1f, dropRate = 0.5f, epochsDrop = 10)

    val lr0 = scheduler.getLearningRate(0)
    val lr10 = scheduler.getLearningRate(10)
    val lr20 = scheduler.getLearningRate(20)

    assert(lr0 == 0.1f)
    assert(lr10 < lr0)
    assert(lr20 < lr10)

  test("LearningRateScheduler exponentialDecay should decrease exponentially"):
    val scheduler = LearningRateScheduler.exponentialDecay(0.1f, decayRate = 0.1f)

    val lr0 = scheduler.getLearningRate(0)
    val lr5 = scheduler.getLearningRate(5)
    val lr10 = scheduler.getLearningRate(10)

    assert(lr0 == 0.1f)
    assert(lr5 < lr0)
    assert(lr10 < lr5)

  test("EarlyStopping should stop after patience epochs without improvement"):
    val earlyStopping = EarlyStopping(patience = 3, minDelta = 0.01f)

    assert(!earlyStopping.checkImprovement(0.5f, higherIsBetter = true))
    assert(!earlyStopping.checkImprovement(0.51f, higherIsBetter = true))
    assert(!earlyStopping.checkImprovement(0.52f, higherIsBetter = true))
    assert(!earlyStopping.checkImprovement(0.53f, higherIsBetter = true))

    assert(earlyStopping.getCounter >= 3)

  test("EarlyStopping should reset counter on improvement"):
    val earlyStopping = EarlyStopping(patience = 3, minDelta = 0.01f)

    assert(!earlyStopping.checkImprovement(0.5f, higherIsBetter = true))
    assert(!earlyStopping.checkImprovement(0.51f, higherIsBetter = true))
    assert(!earlyStopping.checkImprovement(0.7f, higherIsBetter = true))

    assert(earlyStopping.getCounter == 0)

  test("EarlyStopping should work with lower is better"):
    val earlyStopping = EarlyStopping(patience = 3, minDelta = 0.01f)

    assert(!earlyStopping.checkImprovement(1.0f, higherIsBetter = false))
    assert(!earlyStopping.checkImprovement(0.9f, higherIsBetter = false))
    assert(!earlyStopping.checkImprovement(0.8f, higherIsBetter = false))

    assert(earlyStopping.getBestScore.isDefined)

  test("SimpleImageClassifier should train and predict"):
    val manager = NDManager.newBaseManager()
    val classes = List("cat", "dog")

    val classifier = SimpleImageClassifier(classes, manager)

    val trainImages = (1 to 8).map(_ => manager.randomUniform(0f, 1f, ai.djl.ndarray.types.Shape.create(3, 64, 64))).toList
    val trainLabels = List(0, 1, 0, 1, 0, 1, 0, 1)

    classifier.train(trainImages, trainLabels, epochs = 2, learningRate = 0.01f)

    val testImage = manager.randomUniform(0f, 1f, ai.djl.ndarray.types.Shape.create(3, 64, 64))
    val result = classifier.predict(testImage)

    assert(classes.contains(result.label))
    assert(result.confidence >= 0f && result.confidence <= 1f)

    trainImages.foreach(_.close())
    testImage.close()
    classifier.close()
    manager.close()
