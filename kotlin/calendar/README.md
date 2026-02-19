# **Calendar System (Kotlin)**

## Overview

Calendar system demonstrating the **Command Pattern** with meeting booking, conflict detection, and undo/redo functionality for all operations.

---

## Tech Stack

- **Kotlin 2.2.20** → Modern JVM language with concise syntax and null safety
- **Gradle** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Command {
        <<interface>>
        +execute(): Any?
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
        +execute(): List~Meeting~
    }

    class SuggestBestTimeCommand {
        -user1: User
        -user2: User
        -durationMinutes: Long
        +execute(): Pair~LocalDateTime, LocalDateTime~?
    }

    class Meeting {
        +id: String
        +title: String
        +start: LocalDateTime
        +end: LocalDateTime
        +attendees: Set~User~
    }

    class Calendar {
        -meetings: MutableMap~String, Meeting~
        +bookMeeting(meeting: Meeting): Boolean
        +removeMeeting(meetingId: String): Boolean
        +listMeetings(user: User): List~Meeting~
        +getMeetingsForUsers(users: Set~User~): List~Meeting~
        +getAllMeetings(): List~Meeting~
    }

    class CalendarInvoker {
        +execute(command: Command): Any?
        +undo(): Any?
        +redo(): Any?
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

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/calendar
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run the Application
```bash
./gradlew run
```

### 4 - Run Tests
```bash
./gradlew test
```
