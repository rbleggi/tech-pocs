# **Grocery TODO List**

## **Overview**

This project implements a **flexible and maintainable grocery TODO list system** using the **Command Pattern**. Users can add items, remove items, mark items as done, undo actions, redo actions, and list all items - all through a simple, unified interface.

### **Tech Stack**

- **Scala 3.6** → Modern JVM-based language with functional programming support.
- **SBT** → Scala's official build tool.
- **JDK 21** → Required to run the application.

---

## **Features**

- **Comprehensive Grocery Management** → Add, remove, and mark items as done
- **Action History** → Undo and redo functionality
- **Command Pattern** → Actions are encapsulated as commands for easy extension
- **No Boilerplate** → Clean, straightforward implementation
- **Immutable Data** → Leverages Scala's immutability for safer code

---

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(): Unit
        +undo(): Unit
    }

    class AddItemCommand {
        -item: GroceryItem
        +execute(): Unit
        +undo(): Unit
    }

    class RemoveItemCommand {
        -item: GroceryItem
        +execute(): Unit
        +undo(): Unit
    }

    class MarkAsDoneCommand {
        -item: GroceryItem
        +execute(): Unit
        +undo(): Unit
    }

    class GroceryItem {
        +name: String
        +isDone: Boolean
    }

    class GroceryList {
        -items: List[GroceryItem]
        +addItem(item: GroceryItem): Unit
        +removeItem(item: GroceryItem): Unit
        +markAsDone(item: GroceryItem): Unit
        +listAll(): Unit
    }

    class CommandInvoker {
        -history: Stack[Command]
        -redoStack: Stack[Command]
        +executeCommand(command: Command): Unit
        +undo(): Unit
        +redo(): Unit
    }

    Command <|-- AddItemCommand
    Command <|-- RemoveItemCommand
    Command <|-- MarkAsDoneCommand
    AddItemCommand --> GroceryList: modifies
    RemoveItemCommand --> GroceryList: modifies
    MarkAsDoneCommand --> GroceryList: modifies
    CommandInvoker --> Command: invokes
    GroceryList o-- GroceryItem: contains
```

---

## **Command Pattern**

The **Command Pattern** encapsulates requests as objects, allowing:

- Each grocery list action is implemented as a separate `Command`
- `CommandInvoker` maintains history for undo/redo functionality
- Commands know how to execute and undo themselves
- Easy extension with new commands without modifying existing code
- Clean separation between the invoker and the receiver

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/grocery-todo-list
```

### **2️ - Compile & Run the Application**

```shell
./sbtw compile run
```

### **3️ - Run Tests**

```shell
./sbtw compile test
```