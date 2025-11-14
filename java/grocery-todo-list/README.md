# **Grocery TODO List**

## **Overview**

This project implements a **flexible and maintainable grocery TODO list system** using the **Command Pattern**. Users can add items, remove items, mark items as done, undo actions, redo actions, and list all items - all through a simple, unified interface.

---

## **Tech Stack**

- **Java 21** → Modern JVM-based language with enhanced features.
- **Gradle** → Build automation tool.
- **JUnit 5** → Testing framework.

---

## **Features**

- **Comprehensive Grocery Management** → Add, remove, and mark items as done.
- **Action History** → Undo and redo functionality for better control.
- **Command Pattern** → Actions are encapsulated as commands for easy extension and maintainability.
- **No Boilerplate** → Clean, straightforward implementation.
- **Immutable Data** → Leverages immutability for safer and more predictable code.

---

## **Architecture Diagram**

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

---

## **Command Pattern**

The **Command Pattern** encapsulates requests as objects, allowing:

- Each grocery list action to be implemented as a separate `Command`.
- `CommandInvoker` to maintain history for undo/redo functionality.
- Commands to know how to execute and undo themselves, ensuring encapsulation.
- Easy extension with new commands without modifying existing code, adhering to the Open/Closed Principle.
- Clean separation between the invoker (`CommandInvoker`) and the receiver (`GroceryManager`), improving modularity and testability.

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd java/grocery-todo-list
```

### **2️ - Compile & Run the Application**

```shell
./gradlew run
```

### **3️ - Run Tests**

```shell
./gradlew test
```
