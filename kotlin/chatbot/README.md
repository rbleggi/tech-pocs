# **Chatbot System**

## Overview

Simple chatbot system demonstrating the **Strategy Pattern** with three different response strategies: keyword-matching, pattern-matching, and intent-based processing for Portuguese customer service.

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

    class Message {
        +user: String
        +text: String
    }

    class ChatResponse {
        +response: String
        +confidence: Double
        +strategy: String
    }

    class ChatStrategy {
        <<interface>>
        +respond(message: Message): ChatResponse
    }

    class KeywordMatchingStrategy {
        -keywords: Map~String, String~
        +respond(message: Message): ChatResponse
    }

    class PatternMatchingStrategy {
        +respond(message: Message): ChatResponse
    }

    class IntentBasedStrategy {
        -intents: List~Intent~
        +respond(message: Message): ChatResponse
    }

    class Chatbot {
        -strategy: ChatStrategy
        -conversationHistory: List~Pair~Message, ChatResponse~~
        +chat(message: Message): ChatResponse
        +getHistory(): List~Pair~Message, ChatResponse~~
        +clearHistory()
    }

    ChatStrategy <|.. KeywordMatchingStrategy
    ChatStrategy <|.. PatternMatchingStrategy
    ChatStrategy <|.. IntentBasedStrategy
    Chatbot --> ChatStrategy
    ChatStrategy --> ChatResponse
    Chatbot --> Message
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/chatbot
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
