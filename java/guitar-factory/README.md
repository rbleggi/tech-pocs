# **Guitar Factory**

## Overview

This project implements a custom guitar builder system that allows users to flexibly create personalized guitars by specifying attributes such as type, model, specifications, and operating system. It also includes an inventory management system, tracking available guitars and their quantities.

---

## Tech Stack

- **Java 25** → Modern JVM-based language with enhanced features.
- **Gradle** → Build automation tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Guitar {
        - guitarType: String
        - model: String
        - specs: String
        - os: String
        + getGuitarType(): String
        + getModel(): String
        + getSpecs(): String
        + getOs(): String
        + toString(): String
    }

    class Builder {
        - guitarType: String
        - model: String
        - specs: String
        - os: String
        + guitarType(type: String): Builder
        + model(model: String): Builder
        + specs(specs: String): Builder
        + os(os: String): Builder
        + build(): Guitar
    }

    class GuitarInventory {
        - inventory: Map~Guitar, Integer~
        + getInstance(): GuitarInventory
        + addGuitar(guitar: Guitar, quantity: int): void
        + removeGuitar(guitar: Guitar, quantity: int): void
        + getQuantity(guitar: Guitar): int
        + listInventory(): void
        + clearInventory(): void
    }

    Builder --> Guitar: builds
    GuitarInventory --> Guitar: manages
    Guitar -- Builder: inner class
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/guitar-factory
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
