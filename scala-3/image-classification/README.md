# Image Classification with CNNs and Transfer Learning

Image Classification System using CNNs to classify different categories of images with data augmentation and transfer learning.

## Overview

This project demonstrates modern image classification techniques:
- **Convolutional Neural Networks (CNNs)** for spatial feature extraction
- **Transfer Learning** with pre-trained models for faster training

## Tech Stack

- Scala 3.6.3
- SBT 1.10.11
- JDK 25
- DJL 0.30.0 (Deep Java Library)
- PyTorch Engine 0.30.0

## Design Pattern

This implementation uses the **Strategy Pattern** for model architectures:

- **Strategy Interface**: `ModelStrategy` trait defines the contract
- **Concrete Strategies**:
  - `SimpleCNN` - Train CNN from scratch
  - `TransferLearningCNN` - Use pre-trained feature extractor
- **Context**: `CNNImageClassifier` executes the selected strategy
- **Benefits**: Easy to add new architectures without modifying existing code

## Architecture

### SimpleCNN (From Scratch)
```
Conv2D(32 filters, 3x3 kernel) → ReLU → MaxPool(2x2)
Conv2D(64 filters, 3x3 kernel) → ReLU → MaxPool(2x2)
Flatten → Linear(128) → ReLU → Linear(numClasses)
```

### TransferLearningCNN (Pre-trained)
```
Pre-trained Feature Extractor (frozen)
  ├─ Conv2D(64, 7x7) → ReLU → MaxPool(3x3)
  └─ Conv2D(128, 3x3) → ReLU → MaxPool(2x2)
Custom Classifier Head (trainable)
  └─ Flatten → Linear(256) → ReLU → Linear(numClasses)
```

## Features

### Convolutional Layers
- Extract spatial patterns (edges, textures, shapes)
- Parameter sharing across image regions
- Translation invariance
- Hierarchical feature learning

### Pooling Layers
- Reduce spatial dimensions
- Improve computational efficiency
- Add robustness to small translations
- MaxPooling preserves dominant features

### Transfer Learning
- Leverage pre-trained knowledge from large datasets
- Freeze feature extractor, train only classifier
- Faster convergence with less data
- Better accuracy on small datasets
- Option to fine-tune all layers

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/image-classification
```

### 2. Compile

```bash
./sbtw compile
```

### 3. Run Application

```bash
./sbtw run
```