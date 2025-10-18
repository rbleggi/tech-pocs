# Note Taking System

> **Type**: Kotlin Implementation  
> **Pattern**: Command Pattern  
> **Status**: ✅ Complete  
> **Location**: `kotlin/note-taking/`

---

## 🎯 Overview

This project implements a **flexible and maintainable note-taking system** using the **Command Pattern**. Users can add, edit, delete, save, and load notes. The system is designed to be extensible and supports action history for potential undo/redo functionality.

---

## 🏗️ Architecture

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(): Unit
    }

    class AddNoteCommand {
        -manager: NoteManager
        -title: String
        -content: String
        +execute(): Unit
    }

    class EditNoteCommand {
        -manager: NoteManager
        -id: Int
        -newTitle: String?
        -newContent: String?
        +execute(): Unit
    }

    class DeleteNoteCommand {
        -manager: NoteManager
        -id: Int
        +execute(): Unit
    }

    class SaveNotesCommand {
        -manager: NoteManager
        -filePath: String
        +execute(): Unit
    }

    class LoadNotesCommand {
        -manager: NoteManager
        -filePath: String
        +execute(): Unit
    }

    class Note {
        +id: Int
        +title: String
        +content: String
    }

    class NoteManager {
        -notes: MutableMap~Int, Note~
        -nextId: Int
        +addNote(title: String, content: String): Note
        +editNote(id: Int, newTitle: String?, newContent: String?): Note?
        +deleteNote(id: Int): Boolean
        +listNotes(): List~Note~
        +saveNotes(filePath: String): Unit
        +loadNotes(filePath: String): Unit
    }

    class CommandManager {
        -history: MutableList~Command~
        +executeCommand(command: Command): Unit
    }

    Command <|-- AddNoteCommand
    Command <|-- EditNoteCommand
    Command <|-- DeleteNoteCommand
    Command <|-- SaveNotesCommand
    Command <|-- LoadNotesCommand
    AddNoteCommand --> NoteManager: modifies
    EditNoteCommand --> NoteManager: modifies
    DeleteNoteCommand --> NoteManager: modifies
    SaveNotesCommand --> NoteManager: modifies
    LoadNotesCommand --> NoteManager: modifies
    CommandManager --> Command: invokes
    NoteManager o-- Note: contains
```

---

## 💻 Implementation

### Domain Model
```kotlin
data class Note(val id: Int, val title: String, val content: String)
```

### Command Interface
```kotlin
interface Command {
    fun execute()
}
```

### Key Classes
- **NoteManager** - Manages note CRUD operations and persistence
- **CommandManager** - Executes commands and maintains history
- **AddNoteCommand** - Command to add a new note
- **EditNoteCommand** - Command to edit an existing note
- **DeleteNoteCommand** - Command to delete a note
- **SaveNotesCommand** - Command to save notes to file
- **LoadNotesCommand** - Command to load notes from file

---

## 🧪 Tests

**Total Tests**: 15  
**Coverage**: >90%

### NoteManagerTest (10 tests)
- Add single and multiple notes
- Edit notes (title, content, both)
- Delete notes (existing and non-existent)
- List notes
- Save and load notes with file persistence

### CommandTest (5 tests)
- Execute AddNoteCommand
- Execute EditNoteCommand
- Execute DeleteNoteCommand
- Execute SaveNotesCommand
- Execute LoadNotesCommand

---

## 🚀 Usage

### Build
```bash
./gradlew clean build
```

### Run
```bash
./gradlew run
```

### Test
```bash
./gradlew test
```

### Example Output
```
Notes saved to file.
Notes loaded from file:
Note(id=1, title=First Note, content=This is the content of the first note.)
Note(id=2, title=Second Note, content=This is the content of the second note.)
```

---

## 📊 Tech Stack

- **Language**: Kotlin 2.2.20
- **Build Tool**: Gradle 8.10
- **Testing**: JUnit 5
- **JDK**: 24

---

## ✨ Features

- **Note Management** - Add, edit, delete, save, and load notes
- **Command Pattern** - Actions are encapsulated as commands for easy extension
- **Action History** - Commands are stored in a list for potential undo/redo functionality
- **File Persistence** - Notes can be saved to and loaded from a file
- **Type Safety** - Kotlin's null safety prevents common errors

---

## 🎓 Command Pattern

The **Command Pattern** encapsulates requests as objects, allowing:
- Each note management action is implemented as a separate `Command`
- `CommandManager` maintains a history of executed commands for potential undo/redo functionality
- Commands know how to execute themselves
- Easy extension with new commands without modifying existing code
- Clean separation between the invoker (`CommandManager`) and the receiver (`NoteManager`)

---

#poc #kotlin #command-pattern #notes #crud
