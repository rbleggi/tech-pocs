package com.rbleggi.imageclassification

import scala.util.Random
import scala.math._

case class Matrix(data: Array[Array[Double]]):
  val rows: Int = data.length
  val cols: Int = if rows > 0 then data(0).length else 0

  def apply(i: Int, j: Int): Double = data(i)(j)
  def update(i: Int, j: Int, value: Double): Unit = data(i)(j) = value

  def +(other: Matrix): Matrix =
    Matrix(data.zip(other.data).map((r1, r2) => r1.zip(r2).map(_ + _)))

  def -(other: Matrix): Matrix =
    Matrix(data.zip(other.data).map((r1, r2) => r1.zip(r2).map(_ - _)))

  def *(scalar: Double): Matrix =
    Matrix(data.map(_.map(_ * scalar)))

  def dot(other: Matrix): Matrix =
    val result = Array.ofDim[Double](rows, other.cols)
    for
      i <- 0 until rows
      j <- 0 until other.cols
      k <- 0 until cols
    do result(i)(j) += this(i, k) * other(k, j)
    Matrix(result)

  def transpose: Matrix =
    Matrix(Array.tabulate(cols, rows)((j, i) => this(i, j)))

  def map(f: Double => Double): Matrix =
    Matrix(data.map(_.map(f)))

object Matrix:
  def zeros(rows: Int, cols: Int): Matrix =
    Matrix(Array.fill(rows, cols)(0.0))

  def random(rows: Int, cols: Int, scale: Double = 0.01): Matrix =
    val rng = Random()
    Matrix(Array.fill(rows, cols)((rng.nextGaussian() * scale)))

case class Image(pixels: Array[Double], width: Int, height: Int, channels: Int):
  def toMatrix: Matrix =
    Matrix(Array(pixels))

object Activation:
  def relu(x: Double): Double = max(0.0, x)

  def reluDerivative(x: Double): Double = if x > 0 then 1.0 else 0.0

  def softmax(logits: Array[Double]): Array[Double] =
    val maxLogit = logits.max
    val exps = logits.map(x => exp(x - maxLogit))
    val sumExps = exps.sum
    exps.map(_ / sumExps)

class SimpleNeuralNetwork(inputSize: Int, hiddenSize: Int, outputSize: Int):
  var weightsHidden: Matrix = Matrix.random(inputSize, hiddenSize, sqrt(2.0 / inputSize))
  var biasHidden: Array[Double] = Array.fill(hiddenSize)(0.0)
  var weightsOutput: Matrix = Matrix.random(hiddenSize, outputSize, sqrt(2.0 / hiddenSize))
  var biasOutput: Array[Double] = Array.fill(outputSize)(0.0)

  var hiddenActivation: Matrix = Matrix.zeros(1, hiddenSize)
  var hiddenPreActivation: Matrix = Matrix.zeros(1, hiddenSize)

  def forward(input: Matrix): Array[Double] =
    val hiddenPre = input.dot(weightsHidden)
    hiddenPreActivation = hiddenPre

    val hiddenPostData = Array.tabulate(hiddenPre.rows, hiddenPre.cols) { (i, j) =>
      Activation.relu(hiddenPre(i, j) + biasHidden(j))
    }
    hiddenActivation = Matrix(hiddenPostData)

    val outputPre = hiddenActivation.dot(weightsOutput)
    val outputLogits = (0 until outputPre.cols).map(j => outputPre(0, j) + biasOutput(j)).toArray

    Activation.softmax(outputLogits)

  def crossEntropyLoss(predictions: Array[Double], label: Int): Double =
    -log(max(predictions(label), 1e-10))

  def accuracy(predictions: Array[Double], label: Int): Double =
    if predictions.zipWithIndex.maxBy(_._1)._2 == label then 1.0 else 0.0

  def backward(input: Matrix, predictions: Array[Double], label: Int, learningRate: Double): Unit =
    val outputGrad = predictions.clone()
    outputGrad(label) -= 1.0

    val weightsOutputGradData = Array.tabulate(hiddenSize, outputSize) { (i, j) =>
      hiddenActivation(0, i) * outputGrad(j)
    }
    val weightsOutputGrad = Matrix(weightsOutputGradData)

    val biasOutputGrad = outputGrad.clone()

    val hiddenGradData = Array.tabulate(1, hiddenSize) { (i, j) =>
      (0 until outputSize).map(k => outputGrad(k) * weightsOutput(j, k)).sum
    }
    val hiddenGrad = Matrix(hiddenGradData)

    val hiddenGradWithRelu = Matrix(Array.tabulate(1, hiddenSize) { (i, j) =>
      hiddenGrad(i, j) * Activation.reluDerivative(hiddenPreActivation(i, j) + biasHidden(j))
    })

    val weightsHiddenGrad = input.transpose.dot(hiddenGradWithRelu)
    val biasHiddenGrad = (0 until hiddenSize).map(j => hiddenGradWithRelu(0, j)).toArray

    weightsHidden = weightsHidden - (weightsHiddenGrad * learningRate)
    biasHidden = biasHidden.zip(biasHiddenGrad).map((b, g) => b - g * learningRate)
    weightsOutput = weightsOutput - (weightsOutputGrad * learningRate)
    biasOutput = biasOutput.zip(biasOutputGrad).map((b, g) => b - g * learningRate)

  def train(trainData: List[(Matrix, Int)], epochs: Int, learningRate: Double): Unit =
    for epoch <- 0 until epochs do
      var totalLoss = 0.0
      var totalAccuracy = 0.0

      trainData.foreach { case (input, label) =>
        val predictions = forward(input)
        val loss = crossEntropyLoss(predictions, label)
        val acc = accuracy(predictions, label)

        backward(input, predictions, label, learningRate)

        totalLoss += loss
        totalAccuracy += acc
      }

      val avgLoss = totalLoss / trainData.size
      val avgAccuracy = totalAccuracy / trainData.size
      println(f"Epoch ${epoch + 1}%3d: Loss = $avgLoss%.4f, Accuracy = ${avgAccuracy * 100}%.2f%%")

  def predict(input: Matrix): (Int, Double) =
    val predictions = forward(input)
    val (confidence, predictedClass) = predictions.zipWithIndex.maxBy(_._1)
    (predictedClass, confidence)

@main def runSimpleImageClassifier(): Unit =
  println("=== Simple Image Classification ===\n")
  println("Basic structure created with Matrix and Image classes")

  val testLogits = Array(1.0, 2.0, 3.0)
  val probs = Activation.softmax(testLogits)
  println(s"Softmax test: ${probs.mkString(", ")}")
  println(s"ReLU test: relu(-1) = ${Activation.relu(-1)}, relu(2) = ${Activation.relu(2)}")

  val network = SimpleNeuralNetwork(784, 128, 10)
  val testInput = Matrix.random(1, 784)
  val output = network.forward(testInput)
  println(s"\nForward pass test: output shape = ${output.length}, sum = ${output.sum}")
