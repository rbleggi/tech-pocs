# **Redis Clone System (Kotlin)**

## Overview

This project implements a simple in-memory Redis-like key-value store in Kotlin. It supports basic string and map operations, similar to a minimal Redis server.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with advanced type safety and functional programming features.
- **Gradle** → Kotlin's official build tool.
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
        +strings: Map[String, String]
        +maps: Map[String, Map[String, String]]
    }
    Command <|.. SetCommand
    Command <|.. GetCommand
    Command <|.. RemoveCommand
    Command <|.. AppendCommand
    Command <|.. MapSetCommand
    Command <|.. MapGetCommand
    Command <|.. MapKeysCommand
    Command <|.. MapValuesCommand
    RedisStore o-- Command
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/redis-clone
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```