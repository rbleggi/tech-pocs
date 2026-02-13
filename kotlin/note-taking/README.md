# **Note Taking System (Kotlin)**

## Overview

This project implements a flexible and maintainable note-taking system in Kotlin. Users can add, edit, delete, save, and load notes with file persistence support.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with advanced type safety and functional programming features.
- **Gradle** → Kotlin's official build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class Command {
        <<interface>>
        +execute(): Unit
    }
    class NoteManager {
        +notes: Map[Int, Note]
        +nextId: Int
        +addNote(title, content): Note
        +editNote(id, title?, content?): Note?
        +deleteNote(id): Boolean
        +listNotes(): List[Note]
        +saveNotes(filePath): Unit
        +loadNotes(filePath): Unit
    }
    class Note {
        +id: Int
        +title: String
        +content: String
    }
    class CommandManager {
        +history: List[Command]
        +executeCommand(command): Unit
    }
    Command <|.. AddNoteCommand
    Command <|.. EditNoteCommand
    Command <|.. DeleteNoteCommand
    Command <|.. SaveNotesCommand
    Command <|.. LoadNotesCommand
    NoteManager o-- Note
    CommandManager o-- Command
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/note-taking
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```

