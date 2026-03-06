# **Personal AI Assistant**

## Overview

Personal AI assistant system demonstrating the **Strategy Pattern** with query handlers for mathematical operations, text analysis, and data lookup using rule-based processing without actual AI/ML integration.

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

    class Query {
        +id: String
        +question: String
        +type: String
    }

    class Response {
        +queryId: String
        +answer: String
        +metadata: Map~String, Object~
    }

    class QueryHandler {
        <<sealed interface>>
        +handle(query: Query): Response
        +canHandle(query: Query): boolean
    }

    class MathHandler {
        +handle(query: Query): Response
        +canHandle(query: Query): boolean
        -extractNumbers(text: String): List~Double~
    }

    class TextAnalysisHandler {
        +handle(query: Query): Response
        +canHandle(query: Query): boolean
        -extractText(question: String): String
    }

    class DataLookupHandler {
        -database: Map~String, String~
        +handle(query: Query): Response
        +canHandle(query: Query): boolean
    }

    class PersonalAIAssistant {
        -handlers: List~QueryHandler~
        +processQuery(query: Query): Response
        +processBatch(queries: List~Query~): List~Response~
        +getQueryTypeDistribution(queries: List~Query~): Map~String, Long~
    }

    QueryHandler <|.. MathHandler
    QueryHandler <|.. TextAnalysisHandler
    QueryHandler <|.. DataLookupHandler
    PersonalAIAssistant --> QueryHandler
    QueryHandler --> Response
    QueryHandler --> Query
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/personal-ai-assistant
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
