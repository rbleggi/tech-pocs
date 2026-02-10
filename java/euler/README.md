# **Project Euler Problems**

## Overview

This repository contains solutions to mathematical and computational problems from Project Euler, implemented in Java.

---

## Tech Stack

- **Java 25** → Modern Java with streams and functional programming features.
- **Gradle** → Build automation tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Problem {
        <<interface>>
        +solve(): long
    }

    class P01 {
        +solve(): long
    }

    class P02 {
        +solve(): long
    }

    class Main {
        +main(): void
    }

    Problem <|.. P01
    Problem <|.. P02
    Main --> Problem
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/euler
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```