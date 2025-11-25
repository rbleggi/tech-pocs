# Restaurant Queue

## Overview

This project implements a **modular restaurant queue system** using the **Command Pattern**. Each **dish preparation** is encapsulated in a separate command that knows how long it takes to prepare. The system queues these commands and reports the total and per-dish prep time.

## Tech Stack

- **Java 21** → Modern Java with enhanced features
- **Gradle** → Build tool
- **JUnit 5** → Testing framework

## Features

- **Command Pattern** → Every dish is a self-contained command
- **Per-Dish Time Calculation** → Each dish has its own logic
- **Queue Execution** → Prepare dishes in order and track timing
- **Easily Extensible** → Add more dishes without touching core logic

## Architecture

```mermaid
classDiagram
    direction TB

    class DishCommand {
        <<interface>>
        +name(): String
        +prepTime(): int
    }

    class BurgerCommand {
        +name(): String
        +prepTime(): int
    }

    class PastaCommand {
        +name(): String
        +prepTime(): int
    }

    class SaladCommand {
        +name(): String
        +prepTime(): int
    }

    class SushiCommand {
        +name(): String
        +prepTime(): int
    }

    class KitchenQueue {
        -commands: List<DishCommand>
        +addCommand(cmd: DishCommand): void
        +runQueue(): void
    }

    DishCommand <|-- BurgerCommand
    DishCommand <|-- PastaCommand
    DishCommand <|-- SaladCommand
    DishCommand <|-- SushiCommand
    KitchenQueue --> DishCommand: runs
```

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/restaurant-queue
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