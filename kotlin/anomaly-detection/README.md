# **Anomaly Detection System**

## Overview

Financial transaction anomaly detection system demonstrating the **Strategy Pattern** with three different detection algorithms: Z-Score, Interquartile Range (IQR), and Moving Average for Brazilian financial data in BRL.

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

    class Transaction {
        +id: String
        +amount: Double
        +account: String
        +timestamp: Long
    }

    class AnomalyResult {
        +isAnomaly: Boolean
        +score: Double
        +method: String
        +details: String
    }

    class AnomalyDetectionStrategy {
        <<interface>>
        +detect(transaction: Transaction, history: List~Transaction~): AnomalyResult
    }

    class ZScoreStrategy {
        -threshold: Double
        +detect(transaction: Transaction, history: List~Transaction~): AnomalyResult
    }

    class IQRStrategy {
        -multiplier: Double
        +detect(transaction: Transaction, history: List~Transaction~): AnomalyResult
    }

    class MovingAverageStrategy {
        -windowSize: Int
        -threshold: Double
        +detect(transaction: Transaction, history: List~Transaction~): AnomalyResult
    }

    class AnomalyDetector {
        -strategy: AnomalyDetectionStrategy
        +detect(transaction: Transaction, history: List~Transaction~): AnomalyResult
        +detectBatch(transactions: List~Transaction~, history: List~Transaction~): List~AnomalyResult~
        +anomalyCount(transactions: List~Transaction~, history: List~Transaction~): Int
        +falsePositiveRate(transactions: List~Transaction~, history: List~Transaction~, knownAnomalies: Set~String~): Double
    }

    AnomalyDetectionStrategy <|.. ZScoreStrategy
    AnomalyDetectionStrategy <|.. IQRStrategy
    AnomalyDetectionStrategy <|.. MovingAverageStrategy
    AnomalyDetector --> AnomalyDetectionStrategy
    AnomalyDetectionStrategy --> AnomalyResult
    AnomalyDetectionStrategy --> Transaction
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/anomaly-detection
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
