# **Redis Clone**

## Overview

This project implements an in-memory key-value store inspired by Redis, supporting string and map operations. The system follows the Command Pattern, where each operation is encapsulated as a command that can be executed against the store.

---

## Tech Stack

- **Java 25** → Modern Java with records and enhanced features.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(store: RedisStore): String
    }

    class RedisStore {
        +strings: Map~String,String~
        +maps: Map~String,Map~String,String~~
    }

    class SetCommand {
        -key: String
        -value: String
        +execute(store: RedisStore): String
    }

    class GetCommand {
        -key: String
        +execute(store: RedisStore): String
    }

    class RemoveCommand {
        -key: String
        +execute(store: RedisStore): String
    }

    class AppendCommand {
        -key: String
        -value: String
        +execute(store: RedisStore): String
    }

    class MapSetCommand {
        -map: String
        -key: String
        -value: String
        +execute(store: RedisStore): String
    }

    class MapGetCommand {
        -map: String
        -key: String
        +execute(store: RedisStore): String
    }

    class MapKeysCommand {
        -map: String
        +execute(store: RedisStore): String
    }

    class MapValuesCommand {
        -map: String
        +execute(store: RedisStore): String
    }

    class CommandInvoker {
        -store: RedisStore
        +execute(command: Command): String
    }

    Command <|-- SetCommand
    Command <|-- GetCommand
    Command <|-- RemoveCommand
    Command <|-- AppendCommand
    Command <|-- MapSetCommand
    Command <|-- MapGetCommand
    Command <|-- MapKeysCommand
    Command <|-- MapValuesCommand
    CommandInvoker --> Command: executes
    CommandInvoker --> RedisStore: uses
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/redis-clone
```

### 2 - Compile & Run the Application
```bash
./gradlew build run
```

### 3 - Run Tests
```bash
./gradlew test
```
