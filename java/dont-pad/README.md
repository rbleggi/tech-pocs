# **DontPad Command App (Java)**

## **Overview**

A minimal local clone of dontpad.com. Each URL (e.g., `/mypage`) represents a separate document. When you open a document, all its text is loaded and shown for copying/editing. Any new text you enter is appended after the existing content.

## **Tech Stack**

- **Java 25** → Modern Java with records and pattern matching.
- **Gradle** → Build tool.

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
    class SetAllTextCommand {
        + execute()
    }
    class ClearNoteCommand {
        + execute()
    }
    class NoOpCommand {
        + execute()
    }
    NotePad <.. LoadNoteCommand
    NotePad <.. AppendNoteCommand
    NotePad <.. SetAllTextCommand
    NotePad <.. ClearNoteCommand
    Command <|.. LoadNoteCommand
    Command <|.. AppendNoteCommand
    Command <|.. SetAllTextCommand
    Command <|.. ClearNoteCommand
    Command <|.. NoOpCommand
    class main {
        + run(): void
    }
    main ..> Command
```

## **Setup Instructions**

### **1 - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/dont-pad
```

### **2 - Compile & Run the Application**

```shell
./gradlew run
```

### **3 - Run Tests**

```shell
./gradlew test
```