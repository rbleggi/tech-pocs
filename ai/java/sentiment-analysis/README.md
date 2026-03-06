# **Sentiment Analysis**

## Overview

Text sentiment analysis system demonstrating the **Strategy Pattern** with multiple analysis algorithms including lexicon-based scoring, rule-based pattern matching, and hybrid approaches using Portuguese product reviews.

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

    class Review {
        +id: String
        +text: String
        +product: String
    }

    class Sentiment {
        <<enumeration>>
        POSITIVE
        NEGATIVE
        NEUTRAL
    }

    class SentimentResult {
        +reviewId: String
        +sentiment: Sentiment
        +score: double
        +method: String
    }

    class SentimentStrategy {
        <<sealed interface>>
        +analyze(review: Review): SentimentResult
    }

    class LexiconStrategy {
        -positiveWords: Map~String, Double~
        -negativeWords: Map~String, Double~
        +analyze(review: Review): SentimentResult
    }

    class RuleBasedStrategy {
        +analyze(review: Review): SentimentResult
    }

    class HybridStrategy {
        -lexicon: LexiconStrategy
        -ruleBased: RuleBasedStrategy
        +analyze(review: Review): SentimentResult
    }

    class SentimentAnalysisSystem {
        -strategy: SentimentStrategy
        +analyze(review: Review): SentimentResult
        +analyzeBatch(reviews: List~Review~): List~SentimentResult~
        +getSentimentDistribution(reviews: List~Review~): Map~Sentiment, Long~
        +getAverageSentiment(reviews: List~Review~): double
    }

    SentimentStrategy <|.. LexiconStrategy
    SentimentStrategy <|.. RuleBasedStrategy
    SentimentStrategy <|.. HybridStrategy
    SentimentAnalysisSystem --> SentimentStrategy
    SentimentStrategy --> SentimentResult
    SentimentResult --> Sentiment
    HybridStrategy --> LexiconStrategy
    HybridStrategy --> RuleBasedStrategy
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/sentiment-analysis
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
