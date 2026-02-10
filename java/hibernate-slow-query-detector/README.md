# **Hibernate Slow Query Detector**

## Overview

This project demonstrates a Hibernate Slow Query Detector in Java. The solution uses the Observer Pattern to monitor query execution times and notify observers when a query exceeds a configurable threshold. All logic is contained in a single file.

---

## Tech Stack

- **Java 25** → Modern Java with functional programming features.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class QueryObserver {
        <<interface>>
        +notify(query: String, durationMs: long): void
    }
    class SlowQueryDetector {
        -thresholdMs: long
        -observers: List[QueryObserver]
        +addObserver(observer: QueryObserver): void
        +removeObserver(observer: QueryObserver): void
        +executeQuery(query: String, simulatedDurationMs: long): void
    }
    class ConsoleLogger {
        +notify(query: String, durationMs: long): void
    }
    class FileLogger {
        -filename: String
        +notify(query: String, durationMs: long): void
    }
    class EmailNotifier {
        -emailAddress: String
        +notify(query: String, durationMs: long): void
    }

    SlowQueryDetector --> QueryObserver
    ConsoleLogger ..|> QueryObserver
    FileLogger ..|> QueryObserver
    EmailNotifier ..|> QueryObserver
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/hibernate-slow-query-detector
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```