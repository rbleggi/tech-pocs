# Image Classification

Image Classification System using CNNs to classify different categories of images with data augmentation and transfer learning.

## Overview

Single-file implementation demonstrating core machine learning concepts:
- Neural network with forward and backward propagation
- Matrix operations from scratch
- Activation functions (ReLU, Softmax)
- Cross-entropy loss
- Gradient descent optimization
- Image augmentation techniques
- Classification metrics (accuracy, precision, recall, F1-score)

## Tech Stack

- Scala 3.6.3
- SBT 1.10.11
- JDK 25
- No external ML libraries

## Architecture

Simple 2-layer neural network:
- Input layer: 28x28 pixels (784 features)
- Hidden layer: 64 neurons with ReLU activation
- Output layer: 3 classes with Softmax activation

## Design Pattern

This implementation uses the **Strategy Pattern** for image augmentation:
- **Strategy Interface**: `AugmentationStrategy` trait defines the contract
- **Concrete Strategies**: `NoiseAugmentation`, `BrightnessAugmentation`, `FlipAugmentation`
- **Context**: `Image` class applies strategies dynamically
- **Benefits**: Easy to add new augmentation strategies without modifying existing code

## Features

### Neural Network
- Forward propagation with matrix operations
- Backpropagation with gradient computation
- Mini-batch gradient descent
- Cross-entropy loss function

### Image Processing (Strategy Pattern)
- Noise augmentation (Gaussian noise)
- Brightness adjustment (multiply pixels)
- Horizontal flip (mirror image)
- Composable strategies (apply multiple in sequence)

### Evaluation
- Accuracy calculation
- Precision, recall, F1-score
- Confusion matrix support

## **Setup Instructions**

### **1️ - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/image-classification
```

### **2️ - Compile & Run the Application**

```bash
./sbtw compile run
```

### **3️ - Run Tests**

```bash
./sbtw test
```

## Strategy Pattern Usage Example

```scala
val image = Image(Array.fill(28 * 28)(0.5), 28, 28, 1)

// Apply single strategy
val noisyImage = image.augment(NoiseAugmentation(0.1))
val brightImage = image.augment(BrightnessAugmentation(1.3))
val flippedImage = image.augment(FlipAugmentation())

// Apply multiple strategies in sequence
val multiAugmented = image.augmentWith(List(
  NoiseAugmentation(0.1),
  BrightnessAugmentation(1.2),
  FlipAugmentation()
))
```
