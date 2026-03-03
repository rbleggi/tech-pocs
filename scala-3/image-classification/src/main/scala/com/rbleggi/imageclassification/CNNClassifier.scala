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

    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(3, 3))
        .setFilters(32)
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(2, 2), Shape(2, 2)))

    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(3, 3))
        .setFilters(64)
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(2, 2), Shape(2, 2)))

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
    val criteria = Criteria.builder()
      .optApplication(Application.CV.IMAGE_CLASSIFICATION)
      .setTypes(classOf[NDList], classOf[NDList])
      .optFilter("layers", "50")
      .optFilter("flavor", "v1")
      .build()

    val block = SequentialBlock()

    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(7, 7))
        .setFilters(64)
        .optStride(Shape(2, 2))
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(3, 3), Shape(2, 2)))

    block
      .add(Conv2d.builder()
        .setKernelShape(Shape(3, 3))
        .setFilters(128)
        .build())
      .add(Activation.reluBlock())
      .add(Pool.maxPool2dBlock(Shape(2, 2), Shape(2, 2)))

    block
      .add(LambdaBlock(arr => {
        val reshaped = arr.head.flatten()
        new NDList(reshaped)
      }))
      .add(Linear.builder().setUnits(256).build())
      .add(Activation.reluBlock())
      .add(Linear.builder().setUnits(numClasses).build())

    block

class CNNImageClassifier(
  strategy: ModelStrategy,
  numClasses: Int,
  inputShape: Shape
):
  private val model: Model = Model.newInstance("cnn-classifier")
  private val block: SequentialBlock = strategy.buildModel(numClasses)

  model.setBlock(block)

  def initialize(): Unit =
    model.getBlock.initialize(model.getNDManager, ai.djl.ndarray.types.DataType.FLOAT32, inputShape)

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

  def predict(image: NDArray): (Int, Float) =
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

  def close(): Unit =
    model.close()

@main def runCNNClassifier(): Unit =
  println("Image Classification")
