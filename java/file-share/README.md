# **FileShare System**

## Overview

This project implements a **flexible and maintainable file-sharing system** using the **Command Pattern**. Users can save files, restore files, delete files, list all files, and search for specific files. The system also supports undo and redo functionality for all actions.

## Tech Stack

- **Java 25** → Modern JVM-based language with records and enhanced features.
- **Gradle** → Build automation tool.

## Architecture Diagram

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

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/file-share
```

### 2 - Compile & Run the Application

```bash
./gradlew run
```

### 3 - Run Tests

```bash
./gradlew test
```
