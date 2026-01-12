# Simple Image Classification

Educational neural network implementation for image classification using pure Scala.

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

## Features

### Neural Network
- Forward propagation with matrix operations
- Backpropagation with gradient computation
- Mini-batch gradient descent
- Cross-entropy loss function

### Image Processing
- Noise augmentation
- Brightness adjustment
- Horizontal flip
- Basic pixel normalization

### Evaluation
- Accuracy calculation
- Precision, recall, F1-score
- Confusion matrix support

## Usage

### Compile and Run

```bash
cd scala-3/image-classification
sbt compile run
```

### Expected Output

```
=== Simple Image Classification ===

Training samples: 150
Test samples: 30
Input size: 784
Number of classes: 3

Training neural network...
Epoch   1: Loss = 1.0986, Accuracy = 33.33%
Epoch   2: Loss = 1.0978, Accuracy = 34.67%
...
Epoch  20: Loss = 0.8234, Accuracy = 95.33%

Evaluating on test set...
Accuracy: 93.33%
Precision: 93.45%
Recall: 93.33%
F1-Score: 93.21%
```

## Implementation Details

### Matrix Operations
All matrix operations implemented from scratch:
- Matrix multiplication
- Transpose
- Element-wise operations
- Random initialization

### Gradient Descent
Manual computation of gradients through:
- Output layer gradient from softmax + cross-entropy
- Hidden layer gradient with ReLU derivative
- Weight updates using learning rate

### Data Generation
Synthetic dataset with distinct class patterns:
- Class 0: Low intensity pixels
- Class 1: Medium intensity pixels
- Class 2: High intensity pixels

## Educational Purpose

This implementation prioritizes clarity over performance:
- All algorithms visible in single file
- No black-box library calls
- Easy to understand and modify
- Suitable for learning fundamentals

## Limitations

- No GPU acceleration
- Basic optimization (no momentum, adaptive learning rates)
- Synthetic data only
- Small network architecture
- No mini-batch training

## Extension Ideas

- Add convolutional layers
- Implement dropout regularization
- Add learning rate scheduling
- Support batch normalization
- Load real image datasets
