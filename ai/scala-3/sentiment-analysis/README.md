# **Sentiment Analysis**

## Overview

This project implements a sentiment analysis system for Brazilian Portuguese product reviews using the Strategy Pattern. It includes three analysis strategies: Lexicon-based, Rule-based, and Hybrid, enabling flexible classification of text as positive, negative, or neutral sentiment.

---

## Tech Stack

- **Language** -> Scala 3
- **Build Tool** -> sbt
- **Testing** -> ScalaTest 3.2.16
- **JDK** -> 25

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class SentimentStrategy {
        <<trait>>
        +analisar(texto: String): (Sentiment, Double)
    }

    class LexiconStrategy {
        -palavrasPositivas: Set[String]
        -palavrasNegativas: Set[String]
        +analisar(texto: String): (Sentiment, Double)
    }

    class RuleBasedStrategy {
        -regrasPositivas: List[(String, Double)]
        -regrasNegativas: List[(String, Double)]
        +analisar(texto: String): (Sentiment, Double)
    }

    class HybridStrategy {
        -lexicon: LexiconStrategy
        -ruleBased: RuleBasedStrategy
        +analisar(texto: String): (Sentiment, Double)
    }

    class AnalisadorSentimento {
        -strategy: SentimentStrategy
        +analisar(avaliacao: Avaliacao): ResultadoAnalise
        +analisarLote(avaliacoes: List[Avaliacao]): List[ResultadoAnalise]
    }

    class Sentiment {
        <<enum>>
        Positivo
        Negativo
        Neutro
    }

    class Avaliacao {
        +texto: String
        +produto: String
    }

    class ResultadoAnalise {
        +avaliacao: Avaliacao
        +sentiment: Sentiment
        +score: Double
    }

    SentimentStrategy <|-- LexiconStrategy
    SentimentStrategy <|-- RuleBasedStrategy
    SentimentStrategy <|-- HybridStrategy
    HybridStrategy --> LexiconStrategy
    HybridStrategy --> RuleBasedStrategy
    AnalisadorSentimento --> SentimentStrategy
    AnalisadorSentimento --> Avaliacao
    AnalisadorSentimento --> ResultadoAnalise
    ResultadoAnalise --> Sentiment
    ResultadoAnalise --> Avaliacao
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/sentiment-analysis
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
