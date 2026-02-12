# **Guitar Factory (Kotlin)**

## Overview

This project implements a custom guitar factory system that allows users to flexibly create personalized guitars by specifying attributes such as type, model, specifications, and operating system. It also includes an inventory management system, tracking available guitars and their quantities.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build automation tool for Kotlin projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Guitar {
        - Guitar(type, model, specs, os)
        + String guitarType
        + String model
        + String specs
        + String os
        + toString(): String
    }

    class GuitarBuilder {
        - String guitarType
        - String model
        - String specs
        - String os
        + guitarType(type: String): GuitarBuilder
        + model(model: String): GuitarBuilder
        + specs(specs: String): GuitarBuilder
        + os(os: String): GuitarBuilder
        + build(): Guitar
    }

    class GuitarInventory {
        + addGuitar(guitar: Guitar, quantity: Int)
        + removeGuitar(guitar: Guitar, quantity: Int)
        + listInventory()
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

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```