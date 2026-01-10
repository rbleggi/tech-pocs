package com.rbleggi.imageclassification.transfer

import ai.djl.Model
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.transform.{Resize, ToTensor}
import ai.djl.ndarray.{NDArray, NDList, NDManager}
import ai.djl.nn.{Block, SequentialBlock}
import ai.djl.nn.core.Linear
import ai.djl.repository.zoo.{Criteria, ModelZoo, ZooModel}
import ai.djl.training.{DefaultTrainingConfig, Trainer}
import ai.djl.training.evaluator.Accuracy
import ai.djl.training.loss.Loss
import ai.djl.training.optimizer.Optimizer
import ai.djl.training.tracker.Tracker
import ai.djl.translate.{Batchifier, Pipeline, TranslatorContext}
import com.rbleggi.imageclassification.model.{ClassificationResult, ModelMetrics, TrainingConfig}
import scala.collection.mutable.ListBuffer

enum PretrainedModel:
  case ResNet50
  case MobileNetV2
  case SqueezeNet

class TransferLearning(
  val numClasses: Int,
  val classes: List[String],
  val pretrainedModel: PretrainedModel
):

  private var model: Model = _
  private var featureExtractor: ZooModel[Image, NDList] = _

  def loadPretrainedModel(manager: NDManager): Unit =
    val modelName = pretrainedModel match
      case PretrainedModel.ResNet50 => "resnet"
      case PretrainedModel.MobileNetV2 => "mobilenet"
      case PretrainedModel.SqueezeNet => "squeezenet"

    val criteria = Criteria.builder()
      .setTypes(classOf[Image], classOf[NDList])
      .optModelName(modelName)
      .optEngine("PyTorch")
      .build()

    featureExtractor = ModelZoo.loadModel(criteria)

  def buildClassifier(): Block =
    val block = new SequentialBlock()

    val featureSize = pretrainedModel match
      case PretrainedModel.ResNet50 => 2048
      case PretrainedModel.MobileNetV2 => 1280
      case PretrainedModel.SqueezeNet => 512

    block
      .add(Linear.builder().setUnits(512).build())
      .add(ai.djl.nn.Activation.reluBlock())
      .add(ai.djl.nn.core.Dropout.builder().optRate(0.5f).build())
      .add(Linear.builder().setUnits(numClasses).build())

    block

  def freezeFeatureExtractor(): Unit =
    if featureExtractor != null then
      val block = featureExtractor.getBlock
      block.freezeParameters(true)

  def unfreezeFeatureExtractor(): Unit =
    if featureExtractor != null then
      val block = featureExtractor.getBlock
      block.freezeParameters(false)

  def fineTune(
    trainData: List[(Image, Int)],
    config: TrainingConfig,
    manager: NDManager,
    freezeLayers: Boolean = true
  ): List[ModelMetrics] =
    if featureExtractor == null then loadPretrainedModel(manager)

    if freezeLayers then freezeFeatureExtractor()

    model = Model.newInstance("transfer-learning-classifier")
    model.setBlock(buildClassifier())

    val trainingConfig = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
      .addEvaluator(new Accuracy())
      .optOptimizer(
        Optimizer.adam()
          .optLearningRateTracker(Tracker.fixed(config.learningRate))
          .build()
      )

    val trainer = model.newTrainer(trainingConfig)

    val featureSize = pretrainedModel match
      case PretrainedModel.ResNet50 => 2048
      case PretrainedModel.MobileNetV2 => 1280
      case PretrainedModel.SqueezeNet => 512

    val inputShape = ai.djl.ndarray.types.Shape.create(config.batchSize.toLong, featureSize.toLong)
    trainer.initialize(inputShape)

    val metrics = ListBuffer[ModelMetrics]()

    for epoch <- 0 until config.epochs do
      var totalLoss = 0.0f
      var correct = 0
      var total = 0

      trainData.grouped(config.batchSize).foreach { batch =>
        val features = batch.map { case (image, _) =>
          extractFeatures(image, manager)
        }

        val batchFeatures = manager.create(features.map(_.toFloatArray).toArray)
        val batchLabels = manager.create(batch.map(_._2.toFloat).toArray)

        val gradientCollector = trainer.newGradientCollector()
        val predictions = trainer.forward(new NDList(batchFeatures)).singletonOrThrow()
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

        batchFeatures.close()
        batchLabels.close()
        loss.close()
        predictedClasses.close()
        features.foreach(_.close())
      }

      val accuracy = correct.toFloat / total
      val avgLoss = totalLoss / (trainData.size / config.batchSize)

      metrics += ModelMetrics(accuracy, avgLoss, epoch)

    metrics.toList

  def extractFeatures(image: Image, manager: NDManager): NDArray =
    if featureExtractor == null then loadPretrainedModel(manager)

    val predictor = featureExtractor.newPredictor()
    val features = predictor.predict(image)
    predictor.close()

    features.singletonOrThrow()

  def predict(image: Image, manager: NDManager): ClassificationResult =
    if model == null then
      throw new IllegalStateException("Model not initialized")

    val features = extractFeatures(image, manager)
    val trainer = model.newTrainer(new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss()))
    val input = new NDList(features.expandDims(0))
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
    if model != null then model.close()
    if featureExtractor != null then featureExtractor.close()

object TransferLearning:
  def apply(numClasses: Int, classes: List[String], pretrainedModel: PretrainedModel): TransferLearning =
    new TransferLearning(numClasses, classes, pretrainedModel)
