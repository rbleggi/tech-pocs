# **Chatbot**

## Overview

Customer service chatbot demonstrating the **Strategy Pattern** with multiple response generation algorithms including keyword matching, pattern matching, and intent-based classification using Portuguese conversations.

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

    class Message {
        +text: String
        +timestamp: long
    }

    class ChatResponse {
        +text: String
        +confidence: double
        +strategy: String
    }

    class ResponseStrategy {
        <<sealed interface>>
        +respond(userMessage: String, history: List~Message~): ChatResponse
    }

    class KeywordMatchingStrategy {
        -keywordResponses: Map~String, String~
        +respond(userMessage: String, history: List~Message~): ChatResponse
    }

    class PatternMatchingStrategy {
        -rules: List~PatternRule~
        +respond(userMessage: String, history: List~Message~): ChatResponse
    }

    class IntentBasedStrategy {
        -intentKeywords: Map~Intent, List~String~~
        -intentResponses: Map~Intent, String~
        +respond(userMessage: String, history: List~Message~): ChatResponse
    }

    class ChatbotSystem {
        -strategy: ResponseStrategy
        -conversationHistory: List~Message~
        +sendMessage(userMessage: String): ChatResponse
        +getHistory(): List~Message~
        +clearHistory(): void
    }

    ResponseStrategy <|.. KeywordMatchingStrategy
    ResponseStrategy <|.. PatternMatchingStrategy
    ResponseStrategy <|.. IntentBasedStrategy
    ChatbotSystem --> ResponseStrategy
    ResponseStrategy --> ChatResponse
    ChatbotSystem --> Message
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/chatbot
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
