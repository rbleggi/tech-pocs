# **Grocery TODO List**

## Overview

This project implements a **flexible and maintainable grocery TODO list system** using the **Command Pattern**. Users can add items, remove items, mark items as done, undo actions, redo actions, and list all items - all through a simple, unified interface.

## Tech Stack

- **Java 25** → Modern JVM-based language with enhanced features.
- **Gradle** → Build automation tool.

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(items: List~GroceryItem~): List~GroceryItem~
        +undo(items: List~GroceryItem~): List~GroceryItem~
    }

    class AddItemCommand {
        -item: GroceryItem
        +execute(items: List~GroceryItem~): List~GroceryItem~
        +undo(items: List~GroceryItem~): List~GroceryItem~
    }

    class RemoveItemCommand {
        -item: GroceryItem
        -removedItem: GroceryItem
        +execute(items: List~GroceryItem~): List~GroceryItem~
        +undo(items: List~GroceryItem~): List~GroceryItem~
    }

    class MarkAsDoneCommand {
        -item: GroceryItem
        -previousState: GroceryItem
        +execute(items: List~GroceryItem~): List~GroceryItem~
        +undo(items: List~GroceryItem~): List~GroceryItem~
    }

    class GroceryItem {
        +name: String
        +isDone: boolean
        +markAsDone(): GroceryItem
        +markAsUndone(): GroceryItem
        +toString(): String
    }

    class GroceryManager {
        -items: List~GroceryItem~
        +getItems(): List~GroceryItem~
        +applyChanges(newItems: List~GroceryItem~): void
        +listAll(): void
    }

    class CommandInvoker {
        -history: Stack~Command~
        -redoStack: Stack~Command~
        +executeCommand(command: Command, items: List~GroceryItem~): List~GroceryItem~
        +undo(items: List~GroceryItem~): List~GroceryItem~
        +redo(items: List~GroceryItem~): List~GroceryItem~
    }

    Command <|.. AddItemCommand
    Command <|.. RemoveItemCommand
    Command <|.. MarkAsDoneCommand
    CommandInvoker --> Command: invokes
    GroceryManager o-- GroceryItem: manages
    CommandInvoker --> GroceryManager: modifies
```

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/grocery-todo-list
```

### 2 - Compile & Run the Application

```bash
./gradlew run
```

### 3 - Run Tests

```bash
./gradlew test
```
