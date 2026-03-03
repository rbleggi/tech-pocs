# **FileShare System**

## Overview

This project implements a flexible and maintainable file-sharing system using the Command Pattern. Users can save files, restore files, delete files, list all files, and search for specific files. The system also supports undo and redo functionality for all actions.

---

## Tech Stack

- **Language** -> Scala 3.6.3
- **Build Tool** -> sbt 1.10.11
- **Runtime** -> JDK 25
- **Testing** -> ScalaTest 3.2.16

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(): Unit
        +undo(): Unit
    }

    class SaveFileCommand {
        -file: File
        +execute(): Unit
        +undo(): Unit
    }

    class RestoreFileCommand {
        -file: File
        +execute(): Unit
        +undo(): Unit
    }

    class DeleteFileCommand {
        -file: File
        +execute(): Unit
        +undo(): Unit
    }

    class ListFilesCommand {
        +execute(): Unit
        +undo(): Unit
    }

    class SearchFileCommand {
        -query: String
        +execute(): Unit
        +undo(): Unit
    }

    class File {
        +name: String
        +content: String
        +isEncrypted: Boolean
    }

    class FileManager {
        -files: List[File]
        +saveFile(file: File): Unit
        +restoreFile(file: File): Unit
        +deleteFile(file: File): Unit
        +listFiles(): Unit
        +searchFile(query: String): Unit
    }

    class CommandInvoker {
        -history: Stack[Command]
        -redoStack: Stack[Command]
        +executeCommand(command: Command): Unit
        +undo(): Unit
        +redo(): Unit
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
    FileManager o-- File: contains
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/file-share
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
