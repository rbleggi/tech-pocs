# Redis Clone

## Overview

This project implements an **in-memory key-value store** inspired by Redis, supporting **string and map operations**. The system follows the **Command Pattern**, where each operation is encapsulated as a command that can be executed against the store, enabling **extensibility and clean separation of concerns**.

## Tech Stack

- **Java 21** → Modern Java with records and enhanced features
- **Gradle** → Build tool
- **JUnit 5** → Testing framework

## Features

- **String Operations** → SET, GET, REMOVE, APPEND commands
- **Map Operations** → HSET, HGET, HKEYS, HVALS commands
- **Command Pattern** → Each operation is a self-contained command
- **In-Memory Storage** → Fast HashMap-based data structures
- **Easily Extensible** → Add new commands without modifying core logic

## Architecture

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

## Command Pattern

The **Command Pattern** is used to **encapsulate requests as objects**, allowing:
- **Each operation is a separate command** implementing the `Command` interface
- **`CommandInvoker` executes commands** against the `RedisStore`
- **Commands know how to execute themselves**, ensuring encapsulation
- **Easy extension with new commands** without touching existing code
- **Clean separation** between the invoker and the receiver (RedisStore)

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/redis-clone
```

### 2 - Build & Run the Application
```bash
./gradlew build
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
