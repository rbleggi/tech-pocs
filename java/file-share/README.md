# **FileShare System**

## **Overview**

This project implements a **flexible and maintainable file-sharing system** using the **Command Pattern**. Users can save files, restore files, delete files, list all files, and search for specific files. The system also supports undo and redo functionality for all actions.

---

## **Tech Stack**

- **Java 21** → Modern JVM-based language with records and enhanced features.
- **Gradle** → Build automation tool.
- **JUnit 5** → Testing framework.

---

## **Features**

- **File Management** → Save, restore, delete, list, and search files
- **Action History** → Undo and redo functionality
- **Command Pattern** → Actions are encapsulated as commands for easy extension
- **Encryption** → Files are encrypted for secure storage
- **Immutable Data** → Leverages Java records for safer code

---

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(): void
        +undo(): void
    }

    class SaveFileCommand {
        -file: File
        +execute(): void
        +undo(): void
    }

    class RestoreFileCommand {
        -file: File
        +execute(): void
        +undo(): void
    }

    class DeleteFileCommand {
        -file: File
        +execute(): void
        +undo(): void
    }

    class ListFilesCommand {
        +execute(): void
        +undo(): void
    }

    class SearchFileCommand {
        -query: String
        +execute(): void
        +undo(): void
    }

    class File {
        +name: String
        +content: String
        +isEncrypted: boolean
    }

    class FileManager {
        -files: Map~String, File~
        +saveFile(file: File): void
        +restoreFile(file: File): void
        +deleteFile(file: File): void
        +listFiles(): void
        +searchFile(query: String): void
    }

    class CommandInvoker {
        -history: Stack~Command~
        -redoStack: Stack~Command~
        +executeCommand(command: Command): void
        +undo(): void
        +redo(): void
    }

    Command <|-- SaveFileCommand
    Command <|-- RestoreFileCommand
    Command <|-- DeleteFileCommand
    Command <|-- ListFilesCommand
    Command <|-- SearchFileCommand
    SaveFileCommand --> FileManager: modifies
    RestoreFileCommand --> FileManager: modifies
    DeleteFileCommand --> FileManager: modifies
    ListFilesCommand --> FileManager: queries
    SearchFileCommand --> FileManager: queries
    CommandInvoker --> Command: invokes
```

---

## **Command Pattern**

The **Command Pattern** encapsulates requests as objects, allowing:

- Each file operation to be implemented as a separate `Command`.
- `CommandInvoker` to maintain history for undo/redo functionality.
- Commands to know how to execute and undo themselves, ensuring encapsulation.
- Easy extension with new commands without modifying existing code, adhering to the Open/Closed Principle.
- Clean separation between the invoker (`CommandInvoker`) and the receiver (`FileManager`), improving modularity and testability.

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd java/file-share
```

### **2️ - Compile & Run the Application**

```shell
./gradlew run
```

### **3️ - Run Tests**

```shell
./gradlew test
```
