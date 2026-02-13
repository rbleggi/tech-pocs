# **Restaurant Queue (Kotlin)**

## Overview

This project implements a modular restaurant queue system in Kotlin. Each dish preparation is encapsulated in a separate command that knows how long it takes to prepare. The system queues these commands and reports the total and per-dish prep time.

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

    class DishCommand {
        <<interface>>
        +name: String
        +prepTime(): Int
        +execute(): Unit
    }

    class BurgerCommand {
        +name: String
        +prepTime(): Int
        +execute(): Unit
    }

    class PastaCommand {
        +name: String
        +prepTime(): Int
        +execute(): Unit
    }

    class SaladCommand {
        +name: String
        +prepTime(): Int
        +execute(): Unit
    }

    class KitchenQueue {
        -commands: MutableList~DishCommand~
        +addCommand(cmd: DishCommand): Unit
        +runQueue(): Unit
    }

    DishCommand <|-- BurgerCommand
    DishCommand <|-- PastaCommand
    DishCommand <|-- SaladCommand
    KitchenQueue --> DishCommand: runs
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/restaurant-queue
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```