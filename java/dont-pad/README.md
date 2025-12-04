# **DontPad Command App (Java)**

## **Overview**

A minimal local clone of dontpad.com. Each URL (e.g., `/mypage`) represents a separate document. When you open a document, all its text is loaded and shown for copying/editing. Any new text you enter is appended after the existing content.

## **Tech Stack**

- **Java 21** → Modern Java with records and pattern matching.
- **Gradle** → Build tool.

## **Features**

- URL-based documents: Each URL (e.g., `/mypage`) is a separate note.
- Load all text: When you open a document, all its text is loaded and shown.
- Append new text: New input is appended after the existing text.
- Minimal interface: No add/remove/list options—just a single editable note per URL (the URL is used as a key for the note file).
- **Command Pattern**: All main actions (load, append, no-op) are encapsulated as command objects for extensibility.
- The note is loaded and displayed when you open the document, allowing you to copy and continue editing. New text is appended after the existing content.

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB
    class NotePad {
        - key: String
        - notes: List[String]
        + getAllText(): String
        + setAllText(text: String): void
        + appendText(text: String): void
    }
    class Command {
        <<interface>>
        + execute()
    }
    class LoadNoteCommand {
        + execute()
    }
    class AppendNoteCommand {
        + execute()
    }
    class NoOpCommand {
        + execute()
    }
    NotePad <.. LoadNoteCommand
    NotePad <.. AppendNoteCommand
    Command <|.. LoadNoteCommand
    Command <|.. AppendNoteCommand
    Command <|.. NoOpCommand
    class main {
        + run(): void
    }
    main ..> Command
```

## **Setup Instructions**

### **1️ - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/dont-pad
```

### **2️ - Compile & Run the Application**

```shell
./gradlew run
```

### **3️ - Run Tests**

```shell
./gradlew test
```