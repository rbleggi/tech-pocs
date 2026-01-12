# Image Classification

Two implementations of image classification: educational pure Scala and production-ready CNN with transfer learning.

## Overview

This project provides **two complementary implementations**:

### 1. **Educational Implementation** (`ImageClassifier.scala`)
Pure Scala implementation with no external ML libraries, perfect for understanding fundamentals:
- Neural network with forward and backward propagation
- Matrix operations from scratch
- Activation functions (ReLU, Softmax)
- Cross-entropy loss and gradient descent
- Strategy Pattern for image augmentation

### 2. **CNN & Transfer Learning** (`CNNClassifier.scala`)
Production-ready implementation using Deep Java Library (DJL):
- Convolutional Neural Networks (CNNs)
- Transfer Learning with pre-trained models
- Strategy Pattern for model architectures
- Real-world performance optimizations

## Tech Stack

- Scala 3.6.3
- SBT 1.10.11
- JDK 25
- DJL 0.30.0 (for CNN implementation)
- PyTorch Engine (for DJL backend)

## Design Patterns

Both implementations demonstrate the **Strategy Pattern**:

### Educational Version
- **Strategy Interface**: `AugmentationStrategy` trait
- **Concrete Strategies**: `NoiseAugmentation`, `BrightnessAugmentation`, `FlipAugmentation`
- **Context**: `Image` class applies strategies dynamically

### CNN Version
- **Strategy Interface**: `ModelStrategy` trait
- **Concrete Strategies**: `SimpleCNN`, `TransferLearningCNN`
- **Context**: `CNNImageClassifier` uses different architectures

## **Setup Instructions**

### **1. Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/image-classification
```

### **2. Compile**

```bash
./sbtw compile
```

### **3. Run Educational Implementation**

```bash
./sbtw "runMain com.rbleggi.imageclassification.runImageClassifier"
```

### **4. Run CNN Implementation**

```bash
./sbtw "runMain com.rbleggi.imageclassification.runCNNClassifier"
```

## Educational Implementation Details

### Architecture
Simple 2-layer neural network:
- Input: 28x28 pixels (784 features)
- Hidden: 64 neurons with ReLU
- Output: 3 classes with Softmax

### Features
- Matrix operations implemented from scratch
- Manual backpropagation and gradient descent
- Image augmentation with Strategy Pattern
- Evaluation metrics (accuracy, precision, recall, F1)

### Example Usage

```scala
// Create and train network
val network = SimpleNeuralNetwork(inputSize = 784, hiddenSize = 64, numClasses = 3)
network.train(trainData, epochs = 20, learningRate = 0.01)

// Apply augmentation strategies
val image = Image(pixels, 28, 28, 1)
val augmented = image.augmentWith(List(
  NoiseAugmentation(0.1),
  BrightnessAugmentation(1.2),
  FlipAugmentation()
))

// Make predictions
val (predictedClass, confidence) = network.predict(testImage)
```

## CNN Implementation Details

### Architectures

#### Simple CNN from Scratch
```
Conv2D(32, 3x3) → ReLU → MaxPool(2x2)
Conv2D(64, 3x3) → ReLU → MaxPool(2x2)
Flatten → FC(128) → ReLU → FC(3)
```

#### Transfer Learning CNN
```
Pre-trained Feature Extractor (frozen)
  Conv2D(64, 7x7) → ReLU → MaxPool
  Conv2D(128, 3x3) → ReLU → MaxPool
Custom Classifier Head (trainable)
  Flatten → FC(256) → ReLU → FC(3)
```

### Features

#### Convolutional Layers
- Spatial feature extraction
- Parameter sharing
- Translation invariance

#### Pooling Layers
- Dimensionality reduction
- Computational efficiency
- Robustness to small shifts

#### Transfer Learning
- Leverages pre-trained knowledge
- Faster training
- Better accuracy with limited data
- Feature extraction + fine-tuning

### Example Usage

```scala
// Simple CNN from scratch
val simpleCNN = CNNImageClassifier(
  SimpleCNN(),
  numClasses = 3,
  inputShape = Shape(1, 3, 64, 64)
)
simpleCNN.initialize()
simpleCNN.train(trainImages, trainLabels, epochs = 10, batchSize = 16, learningRate = 0.001f)

// Transfer Learning CNN
val transferCNN = CNNImageClassifier(
  TransferLearningCNN("resnet50"),
  numClasses = 3,
  inputShape = Shape(1, 3, 64, 64)
)
transferCNN.initialize()
transferCNN.train(trainImages, trainLabels, epochs = 5, batchSize = 16, learningRate = 0.0001f)

// Make predictions
val (predictedClass, confidence) = transferCNN.predict(testImage)
```

## Key Concepts

### What are CNNs?
Convolutional Neural Networks are specialized for image data:
- **Convolutional layers**: Detect patterns (edges, textures, shapes)
- **Pooling layers**: Reduce spatial dimensions
- **Hierarchical learning**: Simple → complex features
- **Parameter efficiency**: Weight sharing across image

### What is Transfer Learning?
Reusing knowledge from pre-trained models:
- **Pre-training**: Model learns on millions of images (ImageNet)
- **Feature extraction**: Freeze backbone, train only classifier
- **Fine-tuning**: Optionally update some/all layers
- **Benefits**: Faster training, better accuracy, needs less data

## Comparison

| Aspect | Educational | CNN & Transfer Learning |
|--------|------------|-------------------------|
| **Purpose** | Learn fundamentals | Production-ready |
| **Dependencies** | None (pure Scala) | DJL + PyTorch |
| **Architecture** | 2-layer dense | Multi-layer CNN |
| **Training Speed** | Fast (simple) | Slower (complex) |
| **Accuracy** | Good for simple data | Better for complex images |
| **Code Complexity** | ~300 lines | ~300 lines |
| **Use Case** | Education | Real applications |

## When to Use What?

### Use Educational Implementation When:
- Learning ML fundamentals
- Understanding backpropagation
- Teaching neural networks
- Simple classification tasks
- No external dependencies allowed

### Use CNN Implementation When:
- Working with real images
- Need state-of-the-art accuracy
- Have limited training data (transfer learning)
- Production applications
- Access to pre-trained models

## Benefits of This Dual Approach

1. **Educational Value**: Understand concepts from first principles
2. **Practical Application**: See how it's done in production
3. **Pattern Consistency**: Strategy Pattern used in both
4. **Progressive Learning**: Start simple, move to advanced
5. **Single Codebase**: Compare implementations side-by-side

## Extension Ideas

### Educational Implementation
- Add momentum to gradient descent
- Implement dropout regularization
- Add batch normalization
- Support multiple hidden layers

### CNN Implementation
- Load real pre-trained models (ResNet, MobileNet)
- Implement data loaders for real datasets
- Add learning rate schedulers
- Support multi-GPU training
- Implement model ensembles

## License

MIT
