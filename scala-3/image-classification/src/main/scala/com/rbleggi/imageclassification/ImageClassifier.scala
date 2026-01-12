package com.rbleggi.imageclassification

import scala.util.Random
import scala.math._

// Matrix data structure for neural network computations
case class Matrix(data: Array[Array[Double]]):
  val rows: Int = data.length
  val cols: Int = if rows > 0 then data(0).length else 0

  // Access element at position (i, j)
  def apply(i: Int, j: Int): Double = data(i)(j)
  def update(i: Int, j: Int, value: Double): Unit = data(i)(j) = value

  // Element-wise addition of two matrices
  def +(other: Matrix): Matrix =
    Matrix(data.zip(other.data).map((r1, r2) => r1.zip(r2).map(_ + _)))

  // Element-wise subtraction of two matrices
  def -(other: Matrix): Matrix =
    Matrix(data.zip(other.data).map((r1, r2) => r1.zip(r2).map(_ - _)))

  // Scalar multiplication
  def *(scalar: Double): Matrix =
    Matrix(data.map(_.map(_ * scalar)))

  // Matrix multiplication (dot product)
  def dot(other: Matrix): Matrix =
    val result = Array.ofDim[Double](rows, other.cols)
    for
      i <- 0 until rows
      j <- 0 until other.cols
      k <- 0 until cols
    do result(i)(j) += this(i, k) * other(k, j)
    Matrix(result)

  // Transpose matrix (swap rows and columns)
  def transpose: Matrix =
    Matrix(Array.tabulate(cols, rows)((j, i) => this(i, j)))

  // Apply function to each element
  def map(f: Double => Double): Matrix =
    Matrix(data.map(_.map(f)))

object Matrix:
  // Create matrix filled with zeros
  def zeros(rows: Int, cols: Int): Matrix =
    Matrix(Array.fill(rows, cols)(0.0))

  // Create matrix with random values (He initialization)
  def random(rows: Int, cols: Int, scale: Double = 0.01): Matrix =
    val rng = Random()
    Matrix(Array.fill(rows, cols)((rng.nextGaussian() * scale)))

// Image representation with pixel data and dimensions
case class Image(pixels: Array[Double], width: Int, height: Int, channels: Int):
  // Convert image pixels to a matrix row for neural network input
  def toMatrix: Matrix =
    Matrix(Array(pixels))

  // Add random Gaussian noise to pixels (data augmentation)
  def addNoise(scale: Double = 0.1): Image =
    val rng = Random()
    val noisyPixels = pixels.map(p => max(0.0, min(1.0, p + rng.nextGaussian() * scale)))
    Image(noisyPixels, width, height, channels)

  // Increase brightness by multiplying pixel values (data augmentation)
  def brighten(factor: Double = 1.2): Image =
    val brightenedPixels = pixels.map(p => min(1.0, p * factor))
    Image(brightenedPixels, width, height, channels)

  // Flip image horizontally (data augmentation)
  def flip: Image =
    val flippedPixels = Array.ofDim[Double](pixels.length)
    for
      c <- 0 until channels
      h <- 0 until height
      w <- 0 until width
    do
      val srcIdx = c * height * width + h * width + w
      val dstIdx = c * height * width + h * width + (width - 1 - w)
      flippedPixels(dstIdx) = pixels(srcIdx)
    Image(flippedPixels, width, height, channels)

// Activation functions for neural network layers
object Activation:
  // ReLU activation: outputs x if positive, 0 otherwise
  def relu(x: Double): Double = max(0.0, x)

  // Derivative of ReLU for backpropagation
  def reluDerivative(x: Double): Double = if x > 0 then 1.0 else 0.0

  // Softmax activation: converts logits to probability distribution
  def softmax(logits: Array[Double]): Array[Double] =
    val maxLogit = logits.max
    val exps = logits.map(x => exp(x - maxLogit))
    val sumExps = exps.sum
    exps.map(_ / sumExps)

// 2-layer neural network: input -> hidden -> output
class SimpleNeuralNetwork(inputSize: Int, hiddenSize: Int, outputSize: Int):
  // Network parameters initialized with He initialization
  var weightsHidden: Matrix = Matrix.random(inputSize, hiddenSize, sqrt(2.0 / inputSize))
  var biasHidden: Array[Double] = Array.fill(hiddenSize)(0.0)
  var weightsOutput: Matrix = Matrix.random(hiddenSize, outputSize, sqrt(2.0 / hiddenSize))
  var biasOutput: Array[Double] = Array.fill(outputSize)(0.0)

  // Cache activations for backpropagation
  var hiddenActivation: Matrix = Matrix.zeros(1, hiddenSize)
  var hiddenPreActivation: Matrix = Matrix.zeros(1, hiddenSize)

  // Forward pass: compute predictions from input
  def forward(input: Matrix): Array[Double] =
    // Hidden layer: linear transformation
    val hiddenPre = input.dot(weightsHidden)
    hiddenPreActivation = hiddenPre

    // Apply ReLU activation to hidden layer
    val hiddenPostData = Array.tabulate(hiddenPre.rows, hiddenPre.cols) { (i, j) =>
      Activation.relu(hiddenPre(i, j) + biasHidden(j))
    }
    hiddenActivation = Matrix(hiddenPostData)

    // Output layer: linear transformation
    val outputPre = hiddenActivation.dot(weightsOutput)
    val outputLogits = (0 until outputPre.cols).map(j => outputPre(0, j) + biasOutput(j)).toArray

    // Apply softmax to get class probabilities
    Activation.softmax(outputLogits)

  // Compute cross-entropy loss for a single prediction
  def crossEntropyLoss(predictions: Array[Double], label: Int): Double =
    -log(max(predictions(label), 1e-10))

  // Check if prediction matches true label (1.0 if correct, 0.0 otherwise)
  def accuracy(predictions: Array[Double], label: Int): Double =
    if predictions.zipWithIndex.maxBy(_._1)._2 == label then 1.0 else 0.0

  // Backward pass: compute gradients and update weights
  def backward(input: Matrix, predictions: Array[Double], label: Int, learningRate: Double): Unit =
    // Gradient of softmax + cross-entropy: (prediction - target)
    val outputGrad = predictions.clone()
    outputGrad(label) -= 1.0

    // Gradient for output layer weights
    val weightsOutputGradData = Array.tabulate(hiddenSize, outputSize) { (i, j) =>
      hiddenActivation(0, i) * outputGrad(j)
    }
    val weightsOutputGrad = Matrix(weightsOutputGradData)

    // Gradient for output layer bias
    val biasOutputGrad = outputGrad.clone()

    // Backpropagate gradient to hidden layer
    val hiddenGradData = Array.tabulate(1, hiddenSize) { (i, j) =>
      (0 until outputSize).map(k => outputGrad(k) * weightsOutput(j, k)).sum
    }
    val hiddenGrad = Matrix(hiddenGradData)

    // Apply ReLU derivative
    val hiddenGradWithRelu = Matrix(Array.tabulate(1, hiddenSize) { (i, j) =>
      hiddenGrad(i, j) * Activation.reluDerivative(hiddenPreActivation(i, j) + biasHidden(j))
    })

    // Gradient for hidden layer weights and bias
    val weightsHiddenGrad = input.transpose.dot(hiddenGradWithRelu)
    val biasHiddenGrad = (0 until hiddenSize).map(j => hiddenGradWithRelu(0, j)).toArray

    // Update all parameters using gradient descent
    weightsHidden = weightsHidden - (weightsHiddenGrad * learningRate)
    biasHidden = biasHidden.zip(biasHiddenGrad).map((b, g) => b - g * learningRate)
    weightsOutput = weightsOutput - (weightsOutputGrad * learningRate)
    biasOutput = biasOutput.zip(biasOutputGrad).map((b, g) => b - g * learningRate)

  // Training loop: iterate through epochs and update weights
  def train(trainData: List[(Matrix, Int)], epochs: Int, learningRate: Double): Unit =
    for epoch <- 0 until epochs do
      var totalLoss = 0.0
      var totalAccuracy = 0.0

      // Process each training sample
      trainData.foreach { case (input, label) =>
        val predictions = forward(input)
        val loss = crossEntropyLoss(predictions, label)
        val acc = accuracy(predictions, label)

        // Update weights based on gradients
        backward(input, predictions, label, learningRate)

        totalLoss += loss
        totalAccuracy += acc
      }

      // Report epoch statistics
      val avgLoss = totalLoss / trainData.size
      val avgAccuracy = totalAccuracy / trainData.size
      println(f"Epoch ${epoch + 1}%3d: Loss = $avgLoss%.4f, Accuracy = ${avgAccuracy * 100}%.2f%%")

  // Make prediction on single input: returns (predicted class, confidence)
  def predict(input: Matrix): (Int, Double) =
    val predictions = forward(input)
    val (confidence, predictedClass) = predictions.zipWithIndex.maxBy(_._1)
    (predictedClass, confidence)

  // Evaluate model performance on test set
  def evaluate(testData: List[(Matrix, Int)]): Map[String, Double] =
    var correct = 0
    var total = 0
    val confusionMatrix = Array.fill(outputSize, outputSize)(0)

    // Build confusion matrix
    testData.foreach { case (input, label) =>
      val (predicted, _) = predict(input)
      if predicted == label then correct += 1
      confusionMatrix(label)(predicted) += 1
      total += 1
    }

    // Calculate metrics from confusion matrix
    val accuracy = correct.toDouble / total
    val precision = (0 until outputSize).map { cls =>
      val tp = confusionMatrix(cls)(cls)
      val fp = (0 until outputSize).filter(_ != cls).map(confusionMatrix(_)(cls)).sum
      if tp + fp > 0 then tp.toDouble / (tp + fp) else 0.0
    }.sum / outputSize

    val recall = (0 until outputSize).map { cls =>
      val tp = confusionMatrix(cls)(cls)
      val fn = (0 until outputSize).filter(_ != cls).map(confusionMatrix(cls)(_)).sum
      if tp + fn > 0 then tp.toDouble / (tp + fn) else 0.0
    }.sum / outputSize

    val f1 = if precision + recall > 0 then 2 * precision * recall / (precision + recall) else 0.0

    Map("accuracy" -> accuracy, "precision" -> precision, "recall" -> recall, "f1" -> f1)

// Main entry point: demonstrates complete image classification workflow
@main def runImageClassifier(): Unit =
  println("=== Image Classification ===\n")

  val rng = Random()
  val inputSize = 28 * 28
  val numClasses = 3

  // Generate synthetic training data with distinct class patterns
  val trainData = (0 until 150).map { i =>
    val label = i % numClasses
    val base = label * 0.3  // Each class has different base intensity
    val pixels = Array.fill(inputSize)(base + rng.nextDouble() * 0.2)
    (Matrix(Array(pixels)), label)
  }.toList

  // Generate synthetic test data
  val testData = (0 until 30).map { i =>
    val label = i % numClasses
    val base = label * 0.3
    val pixels = Array.fill(inputSize)(base + rng.nextDouble() * 0.2)
    (Matrix(Array(pixels)), label)
  }.toList

  println(s"Training samples: ${trainData.size}")
  println(s"Test samples: ${testData.size}")
  println(s"Input size: $inputSize")
  println(s"Number of classes: $numClasses\n")

  // Create neural network with 64 hidden neurons
  val network = SimpleNeuralNetwork(inputSize, 64, numClasses)

  // Train network for 20 epochs
  println("Training neural network...")
  network.train(trainData, epochs = 20, learningRate = 0.01)

  // Evaluate model performance
  println("\nEvaluating on test set...")
  val metrics = network.evaluate(testData)
  println(f"Accuracy: ${metrics("accuracy") * 100}%.2f%%")
  println(f"Precision: ${metrics("precision") * 100}%.2f%%")
  println(f"Recall: ${metrics("recall") * 100}%.2f%%")
  println(f"F1-Score: ${metrics("f1") * 100}%.2f%%")

  // Test predictions on individual samples
  println("\nMaking predictions on individual samples...")
  (0 until 3).foreach { i =>
    val (input, actualLabel) = testData(i)
    val (predicted, confidence) = network.predict(input)
    println(f"Sample $i: Predicted=$predicted, Actual=$actualLabel, Confidence=${confidence * 100}%.2f%%")
  }

  // Demonstrate image augmentation capabilities
  println("\nTesting image augmentation...")
  val testImage = Image(Array.fill(28 * 28)(0.5), 28, 28, 1)
  val noisyImage = testImage.addNoise(0.1)
  val brightImage = testImage.brighten(1.3)
  val flippedImage = testImage.flip
  println("Noise augmentation applied")
  println("Brightness augmentation applied")
  println("Flip augmentation applied")
