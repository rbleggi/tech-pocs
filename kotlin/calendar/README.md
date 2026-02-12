# **Calendar System (Kotlin)**

## Overview

This project implements a flexible and maintainable calendar system in Kotlin. Users can book meetings, remove meetings, list meetings, and suggest the best time for two people to meet. The system also supports undo and redo functionality for all actions.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Kotlin's build tool for JVM projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

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

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/calendar
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
