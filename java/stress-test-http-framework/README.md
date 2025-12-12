# HTTP Stress Test Framework

## Overview

This project implements a stress test framework for HTTP endpoints using Java. It allows users to configure the number of requests, concurrency level, and target URL to evaluate the performance and reliability of HTTP services under load.

The solution utilizes the Template Method Pattern to structure the stress test workflow, ensuring extensibility and clear separation of preparation, execution, and reporting phases.

---

## Tech Stack

- **Java 21** - Modern Java with virtual threads for efficient concurrency.
- **Gradle** - Build tool for Java projects.
- **JDK 21** - Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class StressTestTemplate {
        <<abstract>>
        +prepare()
        +execute()
        +report()
        +runTest()
    }

    class HttpStressTest {
        -url: String
        -requests: int
        -concurrency: int
        -results: List~Long~
        -client: HttpClient
        +prepare()
        +execute()
        +report()
    }

    StressTestTemplate <|-- HttpStressTest
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/stress-test-http-framework
```

### 2 - Compile & Run the Application

```bash
./gradlew build
./gradlew run
```

### 3 - Run Tests

```bash
./gradlew test
```
