# **Logger Router**

## Overview

This project implements a lightweight and flexible log routing system using the Strategy Pattern. Logs can be sent to multiple destinations like FileSystem, ELK, or Console, with support for synchronous or asynchronous delivery.

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

    class LogLevel {
        <<enum>>
        +INFO
        +ERROR
    }

    class LogStrategy {
        <<interface>>
        +log(level: LogLevel, msg: String): void
    }

    class FileSystemStrategy {
        +log(level: LogLevel, msg: String): void
    }

    class ELKStrategy {
        +log(level: LogLevel, msg: String): void
    }

    class ConsoleStrategy {
        +log(level: LogLevel, msg: String): void
    }

    class AsyncStrategy {
        -inner: LogStrategy
        +log(level: LogLevel, msg: String): void
    }

    class LoggerRouter {
        - List[LogStrategy]
        +log(level: LogLevel, msg: String): void
    }

    LogStrategy <|-- FileSystemStrategy
    LogStrategy <|-- ELKStrategy
    LogStrategy <|-- ConsoleStrategy
    LogStrategy <|-- AsyncStrategy
    AsyncStrategy --> LogStrategy: wraps
    LoggerRouter --> LogStrategy: delegates
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/logger-router
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```