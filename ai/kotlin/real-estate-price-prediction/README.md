# **Real Estate Price Prediction**

## Overview

Real estate price prediction system demonstrating the **Strategy Pattern** with three different regression algorithms: linear regression, polynomial regression, and K-Nearest Neighbors (KNN) for Brazilian cities and neighborhoods.

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

    class Property {
        +id: String
        +city: String
        +neighborhood: String
        +area: Double
        +bedrooms: Int
        +age: Int
        +actualPrice: Double?
    }

    class PredictionResult {
        +predictedPrice: Double
        +confidence: Double
        +method: String
    }

    class PredictionStrategy {
        <<interface>>
        +predict(property: Property, trainingSet: List~Property~): PredictionResult
    }

    class LinearRegressionStrategy {
        +predict(property: Property, trainingSet: List~Property~): PredictionResult
    }

    class PolynomialRegressionStrategy {
        +predict(property: Property, trainingSet: List~Property~): PredictionResult
    }

    class KNNRegressionStrategy {
        -k: Int
        +predict(property: Property, trainingSet: List~Property~): PredictionResult
    }

    class PricePredictionSystem {
        -strategy: PredictionStrategy
        +predict(property: Property, trainingSet: List~Property~): PredictionResult
        +predictBatch(properties: List~Property~, trainingSet: List~Property~): List~PredictionResult~
        +meanAbsoluteError(testSet: List~Property~, trainingSet: List~Property~): Double
    }

    PredictionStrategy <|.. LinearRegressionStrategy
    PredictionStrategy <|.. PolynomialRegressionStrategy
    PredictionStrategy <|.. KNNRegressionStrategy
    PricePredictionSystem --> PredictionStrategy
    PredictionStrategy --> PredictionResult
    PredictionStrategy --> Property
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/real-estate-price-prediction
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
