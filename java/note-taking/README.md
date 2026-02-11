# **Note Taking System**

## Overview

This project implements a note management system using the Command Pattern. It supports creating, editing, deleting notes, and persisting them to disk with file-based storage.

---

## Tech Stack

- **Java 25** → Modern Java with records and enhanced features.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Note {
        +id: int
        +title: String
        +content: String
    }

    class NoteManager {
        -notes: Map<Integer, Note>
        -nextId: int
        +addNote(title, content): Note
        +editNote(id, title, content): Optional<Note>
        +deleteNote(id): boolean
        +listNotes(): List<Note>
        +saveNotes(path): void
        +loadNotes(path): void
    }

    class Command {
        <<interface>>
        +execute(): void
    }

    class AddNoteCommand {
        +execute(): void
    }

    class SaveNotesCommand {
        +execute(): void
    }

    class LoadNotesCommand {
        +execute(): void
    }

    Command <|-- AddNoteCommand
    Command <|-- SaveNotesCommand
    Command <|-- LoadNotesCommand
    AddNoteCommand --> NoteManager
    SaveNotesCommand --> NoteManager
    LoadNotesCommand --> NoteManager
    NoteManager --> Note
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/note-taking
```

### 2 - Compile & Run the Application
```bash
./gradlew build run
```

### 3 - Run Tests
```bash
./gradlew test
```