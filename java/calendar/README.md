# **Calendar System**

## Overview

This project implements a meeting scheduling system using the Command Pattern. It supports booking meetings, detecting conflicts, managing attendees, and ensuring no double-booking occurs.

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

    class User {
        +id: String
        +name: String
    }

    class Meeting {
        +id: String
        +title: String
        +start: LocalDateTime
        +end: LocalDateTime
        +attendees: Set<User>
    }

    class Calendar {
        -meetings: Map<String, Meeting>
        +bookMeeting(meeting): boolean
        +removeMeeting(id): boolean
        +listMeetings(user): List<Meeting>
        +getMeetingsForUsers(users): List<Meeting>
    }

    class CalendarCommand {
        <<interface>>
        +execute(): T
    }

    class BookMeetingCommand {
        +execute(): Boolean
    }

    class RemoveMeetingCommand {
        +execute(): Boolean
    }

    class ListMeetingsCommand {
        +execute(): List<Meeting>
    }

    CalendarCommand <|-- BookMeetingCommand
    CalendarCommand <|-- RemoveMeetingCommand
    CalendarCommand <|-- ListMeetingsCommand
    BookMeetingCommand --> Calendar
    RemoveMeetingCommand --> Calendar
    ListMeetingsCommand --> Calendar
    Calendar --> Meeting
    Meeting --> User
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/calendar
```

### 2 - Compile & Run the Application
```bash
./gradlew build run
```

### 3 - Run Tests
```bash
./gradlew test
```