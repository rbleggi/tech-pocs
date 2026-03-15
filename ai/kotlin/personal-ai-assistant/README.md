# **Personal AI Assistant**

## Overview

Personal AI assistant demonstrating the **Strategy Pattern** with specialized query handlers: mathematical operations, text analysis, and data lookup. Routes queries to appropriate handlers without actual AI integration.

---

## Tech Stack

- **Kotlin 2.2.20** → Modern JVM language with concise syntax and null safety.
- **Gradle** → Build automation tool with Kotlin DSL support.
- **JDK 25** → Required to run the application.
- **kotlin.test** → Testing framework.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Query {
        +text: String
        +type: String
    }

    class AssistantResponse {
        +query: Query
        +answer: String
        +handlerType: String
    }

    class QueryHandler {
        <<interface>>
        +handle(query: Query): AssistantResponse
    }

    class MathQueryHandler {
        +handle(query: Query): AssistantResponse
        -extractNumbers(text: String): List~Int~
    }

    class TextAnalysisQueryHandler {
        +handle(query: Query): AssistantResponse
        -extractQuotedText(text: String): String
    }

    class DataLookupQueryHandler {
        -database: Map~String, String~
        +handle(query: Query): AssistantResponse
    }

    class PersonalAIAssistant {
        -handlers: Map~String, QueryHandler~
        +ask(query: Query): AssistantResponse
        +askMultiple(queries: List~Query~): List~AssistantResponse~
    }

    QueryHandler <|.. MathQueryHandler
    QueryHandler <|.. TextAnalysisQueryHandler
    QueryHandler <|.. DataLookupQueryHandler
    PersonalAIAssistant --> QueryHandler
    QueryHandler --> AssistantResponse
    AssistantResponse --> Query
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd ai/kotlin/personal-ai-assistant
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run Tests
```bash
./gradlew test
```
