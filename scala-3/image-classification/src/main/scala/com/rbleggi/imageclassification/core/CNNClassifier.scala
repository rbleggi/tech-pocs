package com.rbleggi.imageclassification.core

import ai.djl.Model
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.transform.{Resize, ToTensor}
import ai.djl.ndarray.{NDArray, NDList, NDManager}
import ai.djl.nn.{Block, SequentialBlock}
import ai.djl.nn.convolutional.Conv2d
import ai.djl.nn.core.Linear
import ai.djl.nn.pooling.Pool
import ai.djl.training.{DefaultTrainingConfig, Trainer}
import ai.djl.training.dataset.{ArrayDataset, Batch}
import ai.djl.training.evaluator.Accuracy
import ai.djl.training.listener.TrainingListener
import ai.djl.training.loss.Loss
import ai.djl.training.optimizer.Optimizer
import ai.djl.training.tracker.Tracker
import ai.djl.translate.{Batchifier, TranslatorContext}
import com.rbleggi.imageclassification.model.{ClassificationResult, ModelMetrics, TrainingConfig}
import scala.collection.mutable.ListBuffer

class CNNClassifier(
  val numClasses: Int,
  val inputShape: Array[Int],
  val classes: List[String]
):

  private var model: Model = _

  def buildModel(): Block =
    val block = new SequentialBlock()

    block
      .add(Conv2d.builder()
        .setFilters(32)
        .setKernelShape(ai.djl.ndarray.types.Shape.create(3, 3))
        .optPadding(ai.djl.ndarray.types.Shape.create(1, 1))
        .build())
      .add(ai.djl.nn.Activation.reluBlock())
      .add(Pool.maxPool2dBlock(ai.djl.ndarray.types.Shape.create(2, 2), ai.djl.ndarray.types.Shape.create(2, 2)))

      .add(Conv2d.builder()
        .setFilters(64)
        .setKernelShape(ai.djl.ndarray.types.Shape.create(3, 3))
        .optPadding(ai.djl.ndarray.types.Shape.create(1, 1))
        .build())
      .add(ai.djl.nn.Activation.reluBlock())
      .add(Pool.maxPool2dBlock(ai.djl.ndarray.types.Shape.create(2, 2), ai.djl.ndarray.types.Shape.create(2, 2)))

      .add(Conv2d.builder()
        .setFilters(128)
        .setKernelShape(ai.djl.ndarray.types.Shape.create(3, 3))
        .optPadding(ai.djl.ndarray.types.Shape.create(1, 1))
        .build())
      .add(ai.djl.nn.Activation.reluBlock())
      .add(Pool.maxPool2dBlock(ai.djl.ndarray.types.Shape.create(2, 2), ai.djl.ndarray.types.Shape.create(2, 2)))

      .add(ai.djl.nn.core.Flatten.block())
      .add(Linear.builder().setUnits(256).build())
      .add(ai.djl.nn.Activation.reluBlock())
      .add(ai.djl.nn.core.Dropout.builder().optRate(0.5f).build())
      .add(Linear.builder().setUnits(numClasses).build())

    block

  def initializeModel(): Unit =
    model = Model.newInstance("cnn-classifier")
    model.setBlock(buildModel())

  def train(
    trainData: List[(NDArray, Int)],
    config: TrainingConfig,
    manager: NDManager
  ): List[ModelMetrics] =
    if model == null then initializeModel()

    val trainingConfig = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
      .addEvaluator(new Accuracy())
      .optOptimizer(
        Optimizer.adam()
          .optLearningRateTracker(Tracker.fixed(config.learningRate))
          .build()
      )
      .addTrainingListeners(TrainingListener.Defaults.logging(): _*)

    val trainer = model.newTrainer(trainingConfig)

    val inputShape = ai.djl.ndarray.types.Shape.create(
      config.batchSize.toLong,
      inputShape(0).toLong,
      inputShape(1).toLong,
      inputShape(2).toLong
    )
    trainer.initialize(inputShape)

    val metrics = ListBuffer[ModelMetrics]()

    for epoch <- 0 until config.epochs do
      var totalLoss = 0.0f
      var correct = 0
      var total = 0

      trainData.grouped(config.batchSize).foreach { batch =>
        val batchData = manager.create(batch.map(_._1.toFloatArray).toArray)
        val batchLabels = manager.create(batch.map(_._2.toFloat).toArray)

        val gradientCollector = trainer.newGradientCollector()
        val predictions = trainer.forward(new NDList(batchData)).singletonOrThrow()
        val loss = Loss.softmaxCrossEntropyLoss().evaluate(
          new NDList(batchLabels),
          new NDList(predictions)
        )

        gradientCollector.backward(loss)
        gradientCollector.close()

        trainer.step()

        totalLoss += loss.getFloat()
        val predictedClasses = predictions.argMax(1)
        correct += predictedClasses.eq(batchLabels).sum().getInt()
        total += batch.size

        batchData.close()
        batchLabels.close()
        loss.close()
        predictedClasses.close()
      }

      val accuracy = correct.toFloat / total
      val avgLoss = totalLoss / (trainData.size / config.batchSize)

      metrics += ModelMetrics(accuracy, avgLoss, epoch)

    metrics.toList

  def predict(image: NDArray, manager: NDManager): ClassificationResult =
    if model == null then
      throw new IllegalStateException("Model not initialized")

    val trainer = model.newTrainer(new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss()))
    val input = new NDList(image.expandDims(0))
    val output = trainer.forward(input).singletonOrThrow()

    val probabilities = output.softmax(0).toFloatArray
    val predictedClass = probabilities.zipWithIndex.maxBy(_._1)._2

    val allProbs = classes.zip(probabilities).toMap

    ClassificationResult(
      predictedLabel = classes(predictedClass),
      confidence = probabilities(predictedClass),
      allProbabilities = allProbs
    )

  def close(): Unit =
    if model != null then
      model.close()

object CNNClassifier:
  def apply(numClasses: Int, inputShape: Array[Int], classes: List[String]): CNNClassifier =
    new CNNClassifier(numClasses, inputShape, classes)
