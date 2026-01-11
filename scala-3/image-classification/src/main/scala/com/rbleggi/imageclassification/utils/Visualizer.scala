package com.rbleggi.imageclassification.utils

import com.rbleggi.imageclassification.model.{ClassificationResult, ModelMetrics}
import com.rbleggi.imageclassification.evaluation.MultiClassMetrics

class Visualizer:

  def printClassificationResult(result: ClassificationResult): Unit =
    println(s"\nPrediction: ${result.predictedLabel}")
    println(f"Confidence: ${result.confidence * 100}%.2f%%")
    println("\nAll class probabilities:")
    result.allProbabilities.toList.sortBy(-_._2).foreach { case (label, prob) =>
      val bar = "=" * (prob * 50).toInt
      println(f"  $label%-15s ${prob * 100}%6.2f%% $bar")
    }

  def printTrainingMetrics(metrics: List[ModelMetrics]): Unit =
    println("\nTraining Progress:")
    println("-" * 60)
    println(f"${"Epoch"}%-10s ${"Accuracy"}%-15s ${"Loss"}%-15s")
    println("-" * 60)

    metrics.foreach { m =>
      println(f"${m.epoch}%-10d ${m.accuracy * 100}%-14.2f%% ${m.loss}%-15.4f")
    }
    println("-" * 60)

  def printConfusionMatrix(matrix: Array[Array[Int]], classNames: List[String]): Unit =
    println("\nConfusion Matrix:")
    println("-" * (15 + classNames.size * 10))

    print(f"${"Actual\\Pred"}%-15s")
    classNames.foreach(name => print(f"${name.take(8)}%-10s"))
    println()
    println("-" * (15 + classNames.size * 10))

    matrix.zipWithIndex.foreach { case (row, idx) =>
      print(f"${classNames(idx).take(13)}%-15s")
      row.foreach(count => print(f"$count%-10d"))
      println()
    }
    println("-" * (15 + classNames.size * 10))

  def printMultiClassMetrics(metrics: MultiClassMetrics, classNames: List[String]): Unit =
    println("\nClassification Metrics:")
    println("=" * 70)
    println(f"Overall Accuracy: ${metrics.accuracy * 100}%.2f%%")
    println("-" * 70)

    println(f"\n${"Class"}%-15s ${"Precision"}%-15s ${"Recall"}%-15s ${"F1-Score"}%-15s")
    println("-" * 70)

    val precisions = metrics.precisionPerClass
    val recalls = metrics.recallPerClass
    val f1Scores = metrics.f1ScorePerClass

    classNames.zipWithIndex.foreach { case (name, idx) =>
      println(f"${name.take(13)}%-15s ${precisions(idx) * 100}%-14.2f%% ${recalls(idx) * 100}%-14.2f%% ${f1Scores(idx) * 100}%-14.2f%%")
    }

    println("-" * 70)
    println(f"${"Macro Avg"}%-15s ${metrics.macroAveragePrecision * 100}%-14.2f%% ${metrics.macroAverageRecall * 100}%-14.2f%% ${metrics.macroAverageF1 * 100}%-14.2f%%")
    println(f"${"Weighted Avg"}%-15s ${metrics.weightedAveragePrecision * 100}%-14.2f%% ${metrics.weightedAverageRecall * 100}%-14.2f%% ${metrics.weightedAverageF1 * 100}%-14.2f%%")
    println("=" * 70)

  def printDatasetInfo(numSamples: Int, numClasses: Int, imageShape: String): Unit =
    println("\nDataset Information:")
    println("-" * 50)
    println(f"  Number of samples: $numSamples")
    println(f"  Number of classes: $numClasses")
    println(f"  Image shape: $imageShape")
    println("-" * 50)

  def printModelArchitecture(layers: List[String]): Unit =
    println("\nModel Architecture:")
    println("=" * 50)
    layers.zipWithIndex.foreach { case (layer, idx) =>
      println(f"  Layer ${idx + 1}: $layer")
    }
    println("=" * 50)

object Visualizer:
  def apply(): Visualizer = new Visualizer
