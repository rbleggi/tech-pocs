# **Task ThreadPool (Kotlin)**

## Overview

A modular Kotlin PoC for concurrent task execution using a custom thread pool. Demonstrates core features including task submission, thread pool management, and graceful shutdown. All logic is contained in a single file for simplicity and easy extensibility.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build tool for Kotlin projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class Task {
        <<interface>>
        +run(): Unit
    }
    class TaskThreadPool {
        -workers: List<Worker>
        -taskQueue: Queue<Task>
        +submit(task: Task): Unit
        +shutdown(): Unit
    }
    class Worker {
        -thread: Thread
        +start(): Unit
    }
    TaskThreadPool o-- Worker
    TaskThreadPool o-- Task
    Worker --> Task : executes
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/task-threadpool
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```

