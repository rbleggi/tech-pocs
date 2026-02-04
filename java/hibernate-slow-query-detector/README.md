# **Hibernate Slow Query Detector (Java)**

## **Overview**

This project demonstrates a Hibernate Slow Query Detector in Java. The solution uses the Observer Pattern to monitor query execution times and notify observers when a query exceeds a configurable threshold. All logic is contained in a single file.

## **Tech Stack**

- **Java 21** → Modern Java with functional programming features
- **Gradle** → Build tool

## **Architecture Diagram**

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

## **Implementation Details**

- The solution uses the Observer Pattern: observers are notified when a query is slow.
- The `QueryObserver` interface defines the contract for notification handlers.
- The `SlowQueryDetector` class manages observers and detects slow queries based on a threshold.
- Three observer implementations are provided:
  - `ConsoleLogger`: logs to console
  - `FileLogger`: writes to log file
  - `EmailNotifier`: simulates email notifications
- The main entry point demonstrates slow query detection with multiple queries.
- All logic is in a single Java file.

## **Setup Instructions**

### **1️ - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/hibernate-slow-query-detector
```

### **2️ - Compile & Run the Application**

```bash
./gradlew run
```

### **3️ - Run Tests**

```bash
./gradlew test
```