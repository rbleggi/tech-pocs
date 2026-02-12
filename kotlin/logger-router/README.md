# **Logger Router (Kotlin)**

## Overview

A flexible logging system in Kotlin. Supports multiple logging backends (File System, ELK, Console, Memory) with unified API and automatic fallback.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class LoggingStrategy {
        <<interface>>
        +log(level: LogLevel, msg: String, ...)
    }
    class LoggerRouter {
        +info(msg)
        +warn(msg)
        +error(msg)
        +debug(msg)
        +fatal(msg)
    }
    LoggingStrategy <|.. ConsoleStrategy
    LoggingStrategy <|.. FileStrategy
    LoggingStrategy <|.. ELKStrategy
    LoggingStrategy <|.. MemoryStrategy
    LoggerRouter --> LoggingStrategy
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/logger-router
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```