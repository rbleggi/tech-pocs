# **Image Classification**

## Overview

Image classification system demonstrating the **Strategy Pattern** with multiple classification algorithms including threshold-based analysis, K-Nearest Neighbors (KNN), and neural network simulation using Brazilian animal categories.

---

## Tech Stack

- **Java 25** → Latest JDK with modern language features including records and sealed interfaces.
- **Gradle** → Build automation and dependency management.
- **JUnit 5** → Testing framework for unit tests.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class ImageFeatures {
        +redAverage: double
        +greenAverage: double
        +blueAverage: double
        +brightness: double
        +contrast: double
        +saturation: double
    }

    class Image {
        +id: String
        +name: String
        +features: ImageFeatures
        +actualCategory: String
    }

    class Classification {
        +imageId: String
        +predictedCategory: String
        +confidence: double
        +method: String
    }

    class ClassificationStrategy {
        <<sealed interface>>
        +classify(image: Image, trainingSet: List~Image~): Classification
    }

    class ThresholdStrategy {
        +classify(image: Image, trainingSet: List~Image~): Classification
    }

    class KNNStrategy {
        -k: int
        +classify(image: Image, trainingSet: List~Image~): Classification
    }

    class NeuralNetworkStrategy {
        -weights: Map~String, double[]~
        +classify(image: Image, trainingSet: List~Image~): Classification
    }

    class ImageClassificationSystem {
        -strategy: ClassificationStrategy
        +classify(image: Image, trainingSet: List~Image~): Classification
        +classifyBatch(images: List~Image~, trainingSet: List~Image~): List~Classification~
        +calculateAccuracy(testSet: List~Image~, trainingSet: List~Image~): double
    }

    ClassificationStrategy <|.. ThresholdStrategy
    ClassificationStrategy <|.. KNNStrategy
    ClassificationStrategy <|.. NeuralNetworkStrategy
    ImageClassificationSystem --> ClassificationStrategy
    ClassificationStrategy --> Classification
    Image --> ImageFeatures
    ClassificationStrategy --> Image
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/image-classification
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
