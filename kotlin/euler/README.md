# **Project Euler Problems (Kotlin)**

## Overview

This repository contains solutions to mathematical and computational problems from Project Euler, implemented in Kotlin. Each solution aims for clarity, efficiency, and correctness.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with functional programming features and null safety.
- **Gradle** → Build automation tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class P1 {
        +solve(): Long
    }
    class P2 {
        +solve(): Long
    }
    class P3 {
        +solve(): Long
    }
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/euler
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
