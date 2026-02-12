# **Grocery TODO List System (Kotlin)**

## Overview

This project implements a flexible and maintainable Grocery TODO List system in Kotlin. Users can add items, remove items, mark items as done, list all items, and use undo/redo for all actions.

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

    class Command {
        <<interface>>
        +execute(): Boolean
        +undo(): Boolean
    }

    class AddItemCommand {
        -item: GroceryItem
        +execute(): Boolean
        +undo(): Boolean
    }

    class RemoveItemCommand {
        -itemId: UUID
        +execute(): Boolean
        +undo(): Boolean
    }

    class MarkAsDoneCommand {
        -itemId: UUID
        +execute(): Boolean
        +undo(): Boolean
    }

    class ListAllItemsCommand {
        +execute(): Boolean
    }

    class GroceryItem {
        +id: UUID
        +name: String
        +done: Boolean
    }

    class GroceryList {
        -items: MutableMap<UUID, GroceryItem>
        +addItem(item: GroceryItem): Boolean
        +removeItem(itemId: UUID): GroceryItem?
        +markAsDone(itemId: UUID): Boolean
        +markAsUndone(itemId: UUID): Boolean
        +listAll(): List<GroceryItem>
    }

    class GroceryInvoker {
        +execute(command: Command)
        +undo()
        +redo()
    }

    Command <|-- AddItemCommand
    Command <|-- RemoveItemCommand
    Command <|-- MarkAsDoneCommand
    Command <|-- ListAllItemsCommand
    AddItemCommand --> GroceryList: modifies
    RemoveItemCommand --> GroceryList: modifies
    MarkAsDoneCommand --> GroceryList: modifies
    ListAllItemsCommand --> GroceryList: queries
    GroceryInvoker --> Command: invokes
    GroceryList o-- GroceryItem: contains
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/grocery-todo-list
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
