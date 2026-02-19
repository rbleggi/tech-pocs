# **Guitar Factory (Kotlin)**

## Overview

Guitar factory system demonstrating the **Builder Pattern** for creating customized guitars with configurable attributes and an inventory management system for tracking quantities.

---

## Tech Stack

- **Kotlin 2.2.20** → Modern JVM language with concise syntax and null safety
- **Gradle** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Guitar {
        -type: String
        -model: String
        -specs: String
        -os: String
        +toString(): String
    }

    class GuitarBuilder {
        -type: String
        -model: String
        -specs: String
        -os: String
        +type(type: String): Builder
        +model(model: String): Builder
        +specs(specs: String): Builder
        +os(os: String): Builder
        +build(): Guitar
    }

    class GuitarInventory {
        -inventory: MutableMap~Guitar, Int~
        +addGuitar(guitar: Guitar, quantity: Int)
        +removeGuitar(guitar: Guitar, quantity: Int)
        +getQuantity(guitar: Guitar): Int
        +contains(guitar: Guitar): Boolean
        +isEmpty(): Boolean
        +listInventory(): List~Pair~Guitar, Int~~
    }

    GuitarBuilder --> Guitar: builds
    GuitarInventory --> Guitar: manages
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/guitar-factory
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run the Application
```bash
./gradlew run
```

### 4 - Run Tests
```bash
./gradlew test
```
