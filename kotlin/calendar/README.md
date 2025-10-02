# **Calendar System (Kotlin)**

## **Overview**

This project implements a **flexible and maintainable calendar system** using the **Command Pattern** in **Kotlin**. Users can book meetings, remove meetings, list meetings, and suggest the best time for two people to meet. The system also supports undo and redo functionality for all actions.

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Kotlin's build tool for JVM projects.
- **JDK 21** → Required to run the application.

---

## **Features**

- **Meeting Management** → Book, remove, list, and suggest meeting times.
- **Command Pattern** → Actions are encapsulated as commands for easy extension.
- **Conflict Detection** → Prevents booking conflicting meetings.
- **Suggestion Engine** → Suggests optimal meeting times based on availability.
- **Undo/Redo** → All actions support undo and redo.

---

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(): Any
        +undo(): Any
    }

    class BookMeetingCommand {
        -meeting: Meeting
        +execute(): Boolean
        +undo(): Boolean
    }

    class RemoveMeetingCommand {
        -meetingId: String
        +execute(): Boolean
        +undo(): Boolean
    }

    class ListMeetingsCommand {
        -user: User
        +execute(): List<Meeting>
    }

    class SuggestBestTimeCommand {
        -user1: User
        -user2: User
        -durationMinutes: Long
        +execute(): Pair<LocalDateTime, LocalDateTime>?
    }

    class Meeting {
        +id: String
        +title: String
        +start: LocalDateTime
        +end: LocalDateTime
        +attendees: Set<User>
    }

    class Calendar {
        -meetings: MutableMap<String, Meeting>
        +bookMeeting(meeting: Meeting): Boolean
        +removeMeeting(meetingId: String): Boolean
        +listMeetings(user: User): List<Meeting>
        +getMeetingsForUsers(users: Set<User>): List<Meeting>
        +getAllMeetings(): List<Meeting>
    }

    class CalendarInvoker {
        +execute(command: Command): Any
        +undo()
        +redo()
    }

    Command <|-- BookMeetingCommand
    Command <|-- RemoveMeetingCommand
    Command <|-- ListMeetingsCommand
    Command <|-- SuggestBestTimeCommand
    BookMeetingCommand --> Calendar: modifies
    RemoveMeetingCommand --> Calendar: modifies
    ListMeetingsCommand --> Calendar: queries
    SuggestBestTimeCommand --> Calendar: queries
    CalendarInvoker --> Command: invokes
    Calendar o-- Meeting: contains
```

---

## **Command Pattern**

The **Command Pattern** encapsulates requests as objects, allowing:

- Each calendar action is implemented as a separate `Command`
- `CalendarInvoker` executes commands and manages undo/redo
- Commands know how to execute and undo themselves
- Easy extension with new commands without modifying existing code
- Clean separation between the invoker and the receiver

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/calendar
```

### **2️ - Compile & Run the Application**

```shell
./gradlew build
./gradlew run
```

### **3️ - Run Tests**

```shell
./gradlew test
```
