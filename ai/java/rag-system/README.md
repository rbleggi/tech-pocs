# **RAG System**

## Overview

Retrieval-Augmented Generation system demonstrating the **Strategy Pattern** with multiple retrieval strategies including keyword-based, semantic similarity, and hybrid retrieval using Brazilian document examples. Template-based response generation without actual LLM integration.

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

    class Document {
        +id: String
        +content: String
        +metadata: Map~String, String~
    }

    class RetrievalResult {
        +documentId: String
        +score: double
        +snippet: String
    }

    class QueryResult {
        +query: String
        +results: List~RetrievalResult~
        +response: String
    }

    class RetrievalStrategy {
        <<sealed interface>>
        +retrieve(query: String, documents: List~Document~, topK: int): List~RetrievalResult~
    }

    class KeywordRetrievalStrategy {
        +retrieve(query: String, documents: List~Document~, topK: int): List~RetrievalResult~
        -extractSnippet(content: String, term: String): String
    }

    class SemanticRetrievalStrategy {
        +retrieve(query: String, documents: List~Document~, topK: int): List~RetrievalResult~
    }

    class HybridRetrievalStrategy {
        -keywordWeight: double
        -semanticWeight: double
        +retrieve(query: String, documents: List~Document~, topK: int): List~RetrievalResult~
    }

    class ResponseGenerator {
        +generate(query: String, retrievedDocs: List~RetrievalResult~, allDocs: List~Document~): String
    }

    class RAGSystem {
        -retrievalStrategy: RetrievalStrategy
        -responseGenerator: ResponseGenerator
        -knowledgeBase: List~Document~
        +query(query: String, topK: int): QueryResult
        +batchQuery(queries: List~String~, topK: int): List~QueryResult~
    }

    RetrievalStrategy <|.. KeywordRetrievalStrategy
    RetrievalStrategy <|.. SemanticRetrievalStrategy
    RetrievalStrategy <|.. HybridRetrievalStrategy
    RAGSystem --> RetrievalStrategy
    RAGSystem --> ResponseGenerator
    RAGSystem --> Document
    RetrievalStrategy --> RetrievalResult
    ResponseGenerator --> QueryResult
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/rag-system
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
