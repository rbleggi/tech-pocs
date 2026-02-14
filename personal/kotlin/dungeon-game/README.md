# **Dungeon Game (Kotlin)**

## Overview

This project solves the Dungeon Game problem using Dynamic Programming. Given a 2D grid representing a dungeon where negative values represent damage, positive values represent healing, and the goal is to find the minimum initial health needed to reach from top-left to bottom-right.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Kotlin's build tool for JVM projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class DungeonGame {
        +calculateMinimumHP(dungeon: Array<IntArray>): Int
    }
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/dungeon-game
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
