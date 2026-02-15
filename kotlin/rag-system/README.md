# **RAG System**

## Overview

Retrieval-Augmented Generation (RAG) system demonstrating the **Strategy Pattern** with three retrieval methods: keyword-based, semantic word overlap, and hybrid retrieval. Template-based response generation without actual LLM integration.

---

## Tech Stack

- **Kotlin 2.1.10** → Modern JVM language with concise syntax and null safety
- **Gradle 9.3.0** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Document {
        +id: String
        +content: String
        +keywords: Set~String~
    }

    class Query {
        +text: String
        +keywords: Set~String~
    }

    class RetrievalResult {
        +document: Document
        +score: Double
        +method: String
    }

    class RetrievalStrategy {
        <<interface>>
        +retrieve(query: Query, documents: List~Document~, topK: Int): List~RetrievalResult~
    }

    class KeywordRetrievalStrategy {
        +retrieve(query: Query, documents: List~Document~, topK: Int): List~RetrievalResult~
    }

    class SemanticRetrievalStrategy {
        +retrieve(query: Query, documents: List~Document~, topK: Int): List~RetrievalResult~
    }

    class HybridRetrievalStrategy {
        -keywordWeight: Double
        -semanticWeight: Double
        -keywordStrategy: KeywordRetrievalStrategy
        -semanticStrategy: SemanticRetrievalStrategy
        +retrieve(query: Query, documents: List~Document~, topK: Int): List~RetrievalResult~
    }

    class RAGSystem {
        -strategy: RetrievalStrategy
        +generateResponse(query: Query, documents: List~Document~, topK: Int): String
        +search(query: Query, documents: List~Document~, topK: Int): List~RetrievalResult~
    }

    RetrievalStrategy <|.. KeywordRetrievalStrategy
    RetrievalStrategy <|.. SemanticRetrievalStrategy
    RetrievalStrategy <|.. HybridRetrievalStrategy
    RAGSystem --> RetrievalStrategy
    RetrievalStrategy --> RetrievalResult
    RetrievalResult --> Document
    RetrievalStrategy --> Query
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/rag-system
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
