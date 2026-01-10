package com.rbleggi.imageclassification.evaluation

import scala.collection.mutable

case class ConfusionMatrix(
  truePositives: Int,
  trueNegatives: Int,
  falsePositives: Int,
  falseNegatives: Int
):
  def accuracy: Float =
    val total = truePositives + trueNegatives + falsePositives + falseNegatives
    if total == 0 then 0f
    else (truePositives + trueNegatives).toFloat / total

  def precision: Float =
    val denominator = truePositives + falsePositives
    if denominator == 0 then 0f
    else truePositives.toFloat / denominator

  def recall: Float =
    val denominator = truePositives + falseNegatives
    if denominator == 0 then 0f
    else truePositives.toFloat / denominator

  def f1Score: Float =
    val p = precision
    val r = recall
    if p + r == 0 then 0f
    else 2 * (p * r) / (p + r)

  def specificity: Float =
    val denominator = trueNegatives + falsePositives
    if denominator == 0 then 0f
    else trueNegatives.toFloat / denominator

case class MultiClassMetrics(
  predictions: List[Int],
  actuals: List[Int],
  numClasses: Int
):
  require(predictions.length == actuals.length,
    "Predictions and actuals must have the same length")

  def accuracy: Float =
    val correct = predictions.zip(actuals).count { case (pred, actual) => pred == actual }
    correct.toFloat / predictions.length

  def confusionMatrix: Array[Array[Int]] =
    val matrix = Array.ofDim[Int](numClasses, numClasses)
    predictions.zip(actuals).foreach { case (pred, actual) =>
      matrix(actual)(pred) += 1
    }
    matrix

  def precisionPerClass: Array[Float] =
    val matrix = confusionMatrix
    (0 until numClasses).map { classIdx =>
      val truePositives = matrix(classIdx)(classIdx)
      val predicted = matrix.map(_(classIdx)).sum
      if predicted == 0 then 0f
      else truePositives.toFloat / predicted
    }.toArray

  def recallPerClass: Array[Float] =
    val matrix = confusionMatrix
    (0 until numClasses).map { classIdx =>
      val truePositives = matrix(classIdx)(classIdx)
      val actual = matrix(classIdx).sum
      if actual == 0 then 0f
      else truePositives.toFloat / actual
    }.toArray

  def f1ScorePerClass: Array[Float] =
    val precision = precisionPerClass
    val recall = recallPerClass
    precision.zip(recall).map { case (p, r) =>
      if p + r == 0 then 0f
      else 2 * (p * r) / (p + r)
    }

  def macroAveragePrecision: Float =
    val precisions = precisionPerClass
    precisions.sum / precisions.length

  def macroAverageRecall: Float =
    val recalls = recallPerClass
    recalls.sum / recalls.length

  def macroAverageF1: Float =
    val f1Scores = f1ScorePerClass
    f1Scores.sum / f1Scores.length

  def weightedAveragePrecision: Float =
    val matrix = confusionMatrix
    val precisions = precisionPerClass
    val weights = matrix.map(_.sum)
    val totalSamples = weights.sum

    if totalSamples == 0 then 0f
    else precisions.zip(weights).map { case (p, w) => p * w }.sum / totalSamples

  def weightedAverageRecall: Float =
    val matrix = confusionMatrix
    val recalls = recallPerClass
    val weights = matrix.map(_.sum)
    val totalSamples = weights.sum

    if totalSamples == 0 then 0f
    else recalls.zip(weights).map { case (r, w) => r * w }.sum / totalSamples

  def weightedAverageF1: Float =
    val matrix = confusionMatrix
    val f1Scores = f1ScorePerClass
    val weights = matrix.map(_.sum)
    val totalSamples = weights.sum

    if totalSamples == 0 then 0f
    else f1Scores.zip(weights).map { case (f, w) => f * w }.sum / totalSamples

class MetricsCalculator:

  def calculateBinaryMetrics(predictions: List[Int], actuals: List[Int]): ConfusionMatrix =
    require(predictions.length == actuals.length,
      "Predictions and actuals must have the same length")

    var tp = 0
    var tn = 0
    var fp = 0
    var fn = 0

    predictions.zip(actuals).foreach {
      case (1, 1) => tp += 1
      case (0, 0) => tn += 1
      case (1, 0) => fp += 1
      case (0, 1) => fn += 1
      case _ => throw new IllegalArgumentException("Binary classification expects 0 or 1")
    }

    ConfusionMatrix(tp, tn, fp, fn)

  def calculateMultiClassMetrics(
    predictions: List[Int],
    actuals: List[Int],
    numClasses: Int
  ): MultiClassMetrics =
    MultiClassMetrics(predictions, actuals, numClasses)

  def topKAccuracy(
    predictions: List[Array[Float]],
    actuals: List[Int],
    k: Int
  ): Float =
    require(predictions.length == actuals.length,
      "Predictions and actuals must have the same length")

    val correct = predictions.zip(actuals).count { case (probs, actual) =>
      val topK = probs.zipWithIndex.sortBy(-_._1).take(k).map(_._2)
      topK.contains(actual)
    }

    correct.toFloat / predictions.length

object MetricsCalculator:
  def apply(): MetricsCalculator = new MetricsCalculator
