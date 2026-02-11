# **Murder Mystery Game**

## Overview

This project implements a simple terminal-based Murder Mystery game in Java. The player interacts via commands to find clues and accuse suspects. The solution uses the Command pattern to handle user actions and game logic.

---

## Tech Stack

- **Java 25** → Modern Java with records and pattern matching features.
- **Gradle** → Official build tool for Java projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(game: Game): Game
    }

    class Game {
        -suspects: List<String>
        -clues: List<String>
        -foundClues: List<String>
        -accused: String
        -finished: boolean
    }

    class Parser {
        +parse(input: String): Command
    }

    Command <|.. Parser
    Game --> Command
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/murder-mistery-game
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
