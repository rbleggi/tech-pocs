# **AI Chatbot**

## Overview

Chatbot using traditional NLP techniques (intent recognition, entity extraction) with conversation management and context tracking.

---

## Tech Stack

- **Language** -> Scala 3.6.3
- **Build Tool** -> sbt 1.10.11
- **Runtime** -> JDK 25
- **Testing** -> ScalaTest 3.2.16

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Command {
        <<trait>>
        +matches(input: String): Boolean
        +execute(input: String, context: ConversationContext): String
    }

    class GreetingCommand {
        +matches(input: String): Boolean
        +execute(input: String, context: ConversationContext): String
    }

    class WeatherCommand {
        +matches(input: String): Boolean
        +execute(input: String, context: ConversationContext): String
    }

    class ReminderCommand {
        +matches(input: String): Boolean
        +execute(input: String, context: ConversationContext): String
    }

    class UnknownCommand {
        +matches(input: String): Boolean
        +execute(input: String, context: ConversationContext): String
    }

    class ConversationContext {
        +userName: Option[String]
        +history: List[String]
        +entities: Map[String, String]
    }

    class Chatbot {
        -commands: List[Command]
        +respond(input: String, context: ConversationContext): String
    }

    Command <|-- GreetingCommand
    Command <|-- WeatherCommand
    Command <|-- ReminderCommand
    Command <|-- UnknownCommand
    Chatbot --> Command
    Chatbot --> ConversationContext
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/chatbot
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
