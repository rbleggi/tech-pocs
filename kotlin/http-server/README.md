# **Kotlin HTTP Server**

## Overview

This project implements a simple and extensible HTTP server in Kotlin. It supports custom GET endpoints and demonstrates how to compose route handlers in a clean, extensible way.

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

    class GetRouteHandler {
        <<interface>>
        +handle(path: String): String?
    }

    class HelloHandler {
        +handle(path: String): String?
    }

    class PingHandler {
        +handle(path: String): String?
    }

    class Main {
        +main(): Unit
    }

    GetRouteHandler <|.. HelloHandler
    GetRouteHandler <|.. PingHandler
    Main --> GetRouteHandler
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/http-server
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
