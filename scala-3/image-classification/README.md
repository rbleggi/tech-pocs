# Image Classification with CNNs and Transfer Learning

Production-ready image classification using Convolutional Neural Networks and Transfer Learning in Scala 3.

## Overview

This project demonstrates modern image classification techniques:
- **Convolutional Neural Networks (CNNs)** for spatial feature extraction
- **Transfer Learning** with pre-trained models for faster training
- **Strategy Pattern** for swappable model architectures
- **DJL (Deep Java Library)** for deep learning in Scala/JVM

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

## Usage Examples

### Simple CNN from Scratch

```scala
import com.rbleggi.imageclassification.*
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.Shape

val manager = NDManager.newBaseManager()

// Create classifier with SimpleCNN strategy
val classifier = CNNImageClassifier(
  SimpleCNN(),
  numClasses = 3,
  inputShape = Shape(1, 3, 64, 64)  // batch, channels, height, width
)

classifier.initialize()

// Train on your images
classifier.train(
  trainImages,
  trainLabels,
  epochs = 10,
  batchSize = 16,
  learningRate = 0.001f
)

// Make predictions
val (predictedClass, confidence) = classifier.predict(testImage)
println(f"Predicted: Class $predictedClass with ${confidence * 100}%.2f%% confidence")

classifier.close()
manager.close()
```

### Transfer Learning CNN

```scala
// Create classifier with TransferLearningCNN strategy
val transferClassifier = CNNImageClassifier(
  TransferLearningCNN("resnet50"),
  numClasses = 3,
  inputShape = Shape(1, 3, 64, 64)
)

transferClassifier.initialize()

// Train with smaller learning rate (fine-tuning)
transferClassifier.train(
  trainImages,
  trainLabels,
  epochs = 5,
  batchSize = 16,
  learningRate = 0.0001f  // Lower LR for pre-trained weights
)

// Make predictions
val (predicted, confidence) = transferClassifier.predict(testImage)

transferClassifier.close()
```

### Strategy Pattern in Action

```scala
// Easily switch between strategies
val strategies = List(
  SimpleCNN(),
  TransferLearningCNN("resnet50")
)

strategies.foreach { strategy =>
  val classifier = CNNImageClassifier(strategy, numClasses = 3, inputShape)
  classifier.initialize()
  classifier.train(trainImages, trainLabels, epochs = 5, batchSize = 16, learningRate = 0.001f)
  // Evaluate...
  classifier.close()
}
```

## Key Concepts

### What are CNNs?

**Convolutional Neural Networks** are specialized for image data:

1. **Convolutional Layers**: Apply filters to detect patterns
   - Edge detection: Vertical, horizontal, diagonal edges
   - Texture recognition: Patterns, gradients
   - Shape detection: Circles, corners, curves

2. **Pooling Layers**: Downsample feature maps
   - Reduce computational cost
   - Add translation invariance
   - Highlight dominant features

3. **Hierarchical Learning**: Build complex from simple
   - Layer 1: Edges and colors
   - Layer 2: Textures and simple shapes
   - Layer 3: Object parts
   - Layer 4: Complete objects

4. **Parameter Efficiency**: Same filter across image
   - Fewer parameters than fully connected
   - Learn once, apply everywhere
   - Spatial invariance

### What is Transfer Learning?

**Transfer Learning** reuses knowledge from pre-trained models:

1. **Pre-training Phase**
   - Model trained on millions of images (e.g., ImageNet)
   - Learns general visual features
   - Universal patterns (edges, textures, shapes)

2. **Feature Extraction** (Recommended for small datasets)
   - Freeze pre-trained layers (weights unchanged)
   - Train only custom classifier head
   - Fast training, prevents overfitting
   - Good when target task similar to pre-training

3. **Fine-tuning** (Optional for large datasets)
   - Unfreeze some/all pre-trained layers
   - Update weights with small learning rate
   - Adapt features to specific task
   - Better accuracy, needs more data

4. **Benefits**
   - ✅ Faster training (start from good weights)
   - ✅ Better accuracy (leverage learned features)
   - ✅ Less data needed (pre-trained on millions)
   - ✅ Generalization (robust features)

### When to Use What?

| Scenario | Recommendation |
|----------|---------------|
| Small dataset (<1000 images/class) | Transfer Learning (frozen) |
| Medium dataset (1000-10000) | Transfer Learning + fine-tuning |
| Large dataset (>10000) | Train from scratch or fine-tune |
| Similar to ImageNet | Transfer Learning works great |
| Very different domain | Consider training from scratch |
| Limited compute | Transfer Learning (faster) |
| GPU available | Either approach works |

## Code Structure

```
src/main/scala/com/rbleggi/imageclassification/
└── CNNClassifier.scala
    ├── ModelStrategy trait
    ├── SimpleCNN (concrete strategy)
    ├── TransferLearningCNN (concrete strategy)
    ├── CNNImageClassifier (context)
    └── runCNNClassifier (demo)
```

## Demo Output

```
=== CNN and Transfer Learning Demo ===

1. Simple CNN from Scratch
--------------------------------------------------
✓ Simple CNN initialized
  Architecture: Conv(32) → Pool → Conv(64) → Pool → FC(128) → FC(3)
  Parameters: Randomly initialized

Training with 30 synthetic images...
Epoch   1: Loss = 1.0986
Epoch   2: Loss = 1.0234
Epoch   3: Loss = 0.8912
Epoch   4: Loss = 0.7456
Epoch   5: Loss = 0.6234

✓ Prediction: Class 1 with 78.34% confidence

2. Transfer Learning CNN
--------------------------------------------------
✓ Transfer Learning CNN initialized
  Architecture: Pre-trained feature extractor → Custom classifier
  Approach: Feature extraction (frozen backbone) + trainable head
  Benefits: Faster training, better with limited data

Training with 30 synthetic images...
Epoch   1: Loss = 0.9123
Epoch   2: Loss = 0.7845
Epoch   3: Loss = 0.6234
Epoch   4: Loss = 0.5123
Epoch   5: Loss = 0.4567

✓ Prediction: Class 2 with 85.67% confidence

Key Concepts Demonstrated:
- Convolutional layers for spatial feature extraction
- Pooling layers for dimensionality reduction
- Strategy pattern for switching between architectures
- Transfer learning for leveraging pre-trained knowledge
```

## Extension Ideas

### Model Architectures
- Add ResNet blocks with skip connections
- Implement MobileNet for mobile deployment
- Add Inception modules for multi-scale features
- Support EfficientNet architecture

### Transfer Learning
- Load actual pre-trained weights (ResNet, VGG, MobileNet)
- Implement layer freezing/unfreezing API
- Add learning rate schedulers
- Support progressive unfreezing

### Training Enhancements
- Implement data loaders for real datasets
- Add data augmentation strategies
- Support multiple GPUs
- Add checkpointing and early stopping
- Implement learning rate warm-up

### Production Features
- Model serialization/deserialization
- ONNX export for deployment
- Batch prediction API
- REST API wrapper
- Model ensembling

## Performance Tips

1. **Use Transfer Learning** for faster training
2. **Start with frozen backbone** to prevent overfitting
3. **Use smaller learning rate** when fine-tuning
4. **Batch size**: 16-32 for most GPUs
5. **Image size**: 224x224 standard for transfer learning
6. **Data augmentation**: Essential for small datasets
7. **Monitor validation loss**: Detect overfitting early

## Troubleshooting

### Out of Memory
- Reduce batch size
- Use smaller image dimensions
- Enable gradient checkpointing
- Use mixed precision training

### Slow Training
- Use GPU instead of CPU
- Increase batch size
- Use transfer learning
- Reduce image resolution

### Poor Accuracy
- More training data
- Data augmentation
- Transfer learning
- Longer training (more epochs)
- Learning rate tuning

## License

MIT
