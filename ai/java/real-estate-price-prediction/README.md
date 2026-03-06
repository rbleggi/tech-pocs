# **Real Estate Price Prediction**

## Overview

Real estate price prediction system demonstrating the **Strategy Pattern** with multiple regression algorithms including linear regression, polynomial regression, and K-Nearest Neighbors using Brazilian cities and property data.

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

    class Property {
        +id: String
        +city: String
        +neighborhood: String
        +area: double
        +bedrooms: int
        +bathrooms: int
        +parkingSpaces: int
        +age: int
        +actualPrice: double
    }

    class PricePrediction {
        +propertyId: String
        +predictedPrice: double
        +confidence: double
        +method: String
    }

    class PredictionStrategy {
        <<sealed interface>>
        +predict(property: Property, trainingData: List~Property~): PricePrediction
    }

    class LinearRegressionStrategy {
        -cityMultipliers: Map~String, Double~
        -neighborhoodMultipliers: Map~String, Double~
        +predict(property: Property, trainingData: List~Property~): PricePrediction
    }

    class PolynomialRegressionStrategy {
        +predict(property: Property, trainingData: List~Property~): PricePrediction
    }

    class KNNRegressionStrategy {
        -k: int
        +predict(property: Property, trainingData: List~Property~): PricePrediction
    }

    class RealEstatePricingSystem {
        -strategy: PredictionStrategy
        +predictPrice(property: Property, trainingData: List~Property~): PricePrediction
        +predictBatch(properties: List~Property~, trainingData: List~Property~): List~PricePrediction~
        +calculateMeanAbsoluteError(testSet: List~Property~, trainingData: List~Property~): double
        +calculateAccuracyPercentage(testSet: List~Property~, trainingData: List~Property~, threshold: double): double
    }

    PredictionStrategy <|.. LinearRegressionStrategy
    PredictionStrategy <|.. PolynomialRegressionStrategy
    PredictionStrategy <|.. KNNRegressionStrategy
    RealEstatePricingSystem --> PredictionStrategy
    PredictionStrategy --> PricePrediction
    PredictionStrategy --> Property
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/real-estate-price-prediction
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
