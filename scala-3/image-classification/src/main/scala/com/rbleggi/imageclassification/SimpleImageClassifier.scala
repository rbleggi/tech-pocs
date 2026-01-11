package com.rbleggi.imageclassification

import ai.djl.ndarray.{NDArray, NDManager}
import ai.djl.ndarray.types.Shape

case class SimpleClassificationResult(label: String, confidence: Float)

class SimpleImageClassifier(classes: List[String], manager: NDManager):

  private val weights = manager.randomUniform(-1f, 1f, new Shape(3 * 64 * 64, classes.size))
  private val bias = manager.zeros(new Shape(classes.size))

  def train(images: List[NDArray], labels: List[Int], epochs: Int, learningRate: Float): Unit =
    for epoch <- 0 until epochs do
      var totalLoss = 0.0f

      images.zip(labels).foreach { case (image, label) =>
        val flattened = image.flatten()
        val logits = flattened.dot(weights).add(bias)
        val probs = softmax(logits)

        val target = manager.zeros(new Shape(classes.size))
        target.set(Array(label.toLong), 1f)

        val loss = crossEntropyLoss(probs, target)
        totalLoss += loss.getFloat()

        val gradient = probs.sub(target)
        val weightsGradient = flattened.expandDims(1).dot(gradient.expandDims(0))

        weights.subi(weightsGradient.transpose().mul(learningRate))
        bias.subi(gradient.mul(learningRate))

        flattened.close()
        logits.close()
        probs.close()
        target.close()
        loss.close()
        gradient.close()
        weightsGradient.close()
      }

      val avgLoss = totalLoss / images.size
      println(f"Epoch $epoch: Loss = $avgLoss%.4f")

  def predict(image: NDArray): SimpleClassificationResult =
    val flattened = image.flatten()
    val logits = flattened.dot(weights).add(bias)
    val probs = softmax(logits)

    val probsArray = probs.toFloatArray
    val maxIdx = probsArray.zipWithIndex.maxBy(_._1)._2
    val confidence = probsArray(maxIdx)

    flattened.close()
    logits.close()
    probs.close()

    SimpleClassificationResult(classes(maxIdx), confidence)

  private def softmax(logits: NDArray): NDArray =
    val maxLogit = logits.max()
    val exps = logits.sub(maxLogit).exp()
    val sumExps = exps.sum()
    val result = exps.div(sumExps)

    maxLogit.close()
    exps.close()
    sumExps.close()

    result

  private def crossEntropyLoss(probs: NDArray, target: NDArray): NDArray =
    probs.add(1e-10f).log().mul(target).sum().neg()

  def close(): Unit =
    weights.close()
    bias.close()

object SimpleImageClassifier:
  def apply(classes: List[String], manager: NDManager): SimpleImageClassifier =
    new SimpleImageClassifier(classes, manager)

@main def runSimpleClassifier(): Unit =
  println("=== Simple Image Classifier ===\n")

  val manager = NDManager.newBaseManager()
  val classes = List("cat", "dog", "bird")

  val classifier = SimpleImageClassifier(classes, manager)

  val trainImages = (1 to 12).map { _ =>
    manager.randomUniform(0f, 1f, new Shape(3, 64, 64))
  }.toList

  val trainLabels = List(0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2)

  println("Training simple classifier...")
  classifier.train(trainImages, trainLabels, epochs = 5, learningRate = 0.01f)

  println("\nTesting predictions:")
  val testImage = manager.randomUniform(0f, 1f, new Shape(3, 64, 64))
  val result = classifier.predict(testImage)

  println(f"Predicted: ${result.label} (${result.confidence * 100}%.2f%% confidence)")

  trainImages.foreach(_.close())
  testImage.close()
  classifier.close()
  manager.close()
