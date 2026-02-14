# **Sentiment Analysis System**

## Overview

Text sentiment analysis system demonstrating the **Strategy Pattern** with three different analysis approaches: lexicon-based, rule-based, and hybrid analysis for Portuguese product reviews.

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

    class Sentiment {
        <<enumeration>>
        Positive
        Negative
        Neutral
    }

    class Review {
        +id: String
        +text: String
        +product: String
    }

    class SentimentResult {
        +sentiment: Sentiment
        +score: Double
        +confidence: Double
        +details: String
    }

    class SentimentStrategy {
        <<interface>>
        +analyze(review: Review): SentimentResult
    }

    class LexiconBasedStrategy {
        -positiveWords: Set~String~
        -negativeWords: Set~String~
        +analyze(review: Review): SentimentResult
    }

    class RuleBasedStrategy {
        +analyze(review: Review): SentimentResult
    }

    class HybridStrategy {
        -lexiconStrategy: LexiconBasedStrategy
        -ruleStrategy: RuleBasedStrategy
        +analyze(review: Review): SentimentResult
    }

    class SentimentAnalyzer {
        -strategy: SentimentStrategy
        +analyze(review: Review): SentimentResult
        +analyzeBatch(reviews: List~Review~): List~SentimentResult~
        +sentimentDistribution(reviews: List~Review~): Map~Sentiment, Int~
    }

    SentimentStrategy <|.. LexiconBasedStrategy
    SentimentStrategy <|.. RuleBasedStrategy
    SentimentStrategy <|.. HybridStrategy
    SentimentAnalyzer --> SentimentStrategy
    SentimentStrategy --> SentimentResult
    SentimentResult --> Sentiment
    HybridStrategy --> LexiconBasedStrategy
    HybridStrategy --> RuleBasedStrategy
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/sentiment-analysis
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
