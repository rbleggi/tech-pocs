# **Anomaly Detection**

## Overview

Financial transaction anomaly detection system demonstrating the **Strategy Pattern** with multiple detection algorithms including Z-Score, Interquartile Range (IQR), and Moving Average methods using Brazilian transaction data.

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

    class Transaction {
        +id: String
        +accountId: String
        +amount: double
        +type: String
        +timestamp: long
        +location: String
    }

    class AnomalyResult {
        +transactionId: String
        +isAnomaly: boolean
        +score: double
        +reason: String
        +method: String
    }

    class AnomalyDetectionStrategy {
        <<sealed interface>>
        +detect(transaction: Transaction, historicalData: List~Transaction~): AnomalyResult
    }

    class ZScoreStrategy {
        -threshold: double
        +detect(transaction: Transaction, historicalData: List~Transaction~): AnomalyResult
    }

    class IQRStrategy {
        -multiplier: double
        +detect(transaction: Transaction, historicalData: List~Transaction~): AnomalyResult
    }

    class MovingAverageStrategy {
        -windowSize: int
        -threshold: double
        +detect(transaction: Transaction, historicalData: List~Transaction~): AnomalyResult
    }

    class AnomalyDetectionSystem {
        -strategy: AnomalyDetectionStrategy
        +detectAnomaly(transaction: Transaction, historicalData: List~Transaction~): AnomalyResult
        +detectBatch(transactions: List~Transaction~, historicalData: List~Transaction~): List~AnomalyResult~
        +getAnomalyDistribution(transactions: List~Transaction~, historicalData: List~Transaction~): Map~Boolean, Long~
        +getAnomalousTransactions(transactions: List~Transaction~, historicalData: List~Transaction~): List~Transaction~
    }

    AnomalyDetectionStrategy <|.. ZScoreStrategy
    AnomalyDetectionStrategy <|.. IQRStrategy
    AnomalyDetectionStrategy <|.. MovingAverageStrategy
    AnomalyDetectionSystem --> AnomalyDetectionStrategy
    AnomalyDetectionStrategy --> AnomalyResult
    AnomalyDetectionStrategy --> Transaction
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/anomaly-detection
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
