# **Image Classification System**

## Overview

Image classification system demonstrating the **Strategy Pattern** with three different classification algorithms: threshold-based, K-Nearest Neighbors (KNN), and neural network-like classification using Brazilian fauna and food categories.

---

## Tech Stack

- **Kotlin 2.1.10** → Modern JVM language with concise syntax and null safety
- **Gradle** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Category {
        <<enumeration>>
        Onca
        Arara
        Capivara
        PaoDeQueijo
        Feijoada
        Acai
    }

    class Image {
        +id: String
        +name: String
        +features: List~Double~
        +actualCategory: Category?
    }

    class ClassificationResult {
        +category: Category
        +confidence: Double
        +details: String
    }

    class ClassificationStrategy {
        <<interface>>
        +classify(image: Image): ClassificationResult
    }

    class ThresholdStrategy {
        -thresholds: Map~Category, List~Double~~
        +classify(image: Image): ClassificationResult
    }

    class KNNStrategy {
        -trainingSet: List~Image~
        -k: Int
        +classify(image: Image): ClassificationResult
    }

    class NeuralNetworkLikeStrategy {
        -weights: Map~Category, List~Double~~
        +classify(image: Image): ClassificationResult
    }

    class ImageClassifier {
        -strategy: ClassificationStrategy
        +classify(image: Image): ClassificationResult
        +classifyBatch(images: List~Image~): List~ClassificationResult~
        +accuracy(images: List~Image~): Double
    }

    ClassificationStrategy <|.. ThresholdStrategy
    ClassificationStrategy <|.. KNNStrategy
    ClassificationStrategy <|.. NeuralNetworkLikeStrategy
    ImageClassifier --> ClassificationStrategy
    ClassificationStrategy --> ClassificationResult
    ClassificationResult --> Category
    Image --> Category
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/image-classification
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run the Application
```bash
./gradlew run
```

### 4 - Run Tests
```bash
./gradlew test
```
