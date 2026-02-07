# **Teacher's Class Organizer/Optimizer (Java)**

## Overview

This project implements a **flexible and maintainable class organizer/optimizer** for teachers using the **Observer Pattern** in **Java**. Teachers and admins are notified of schedule changes, and the system helps optimize class allocations.

## Tech Stack

- **Java 21** → Modern Java with records and pattern matching.
- **Gradle** → Build tool for JVM projects.

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Observer {
        <<interface>>
        +update(schedule: Schedule)
    }

    class Teacher {
        +update(schedule: Schedule)
    }

    class Subject {
        <<interface>>
        +registerObserver(observer: Observer)
        +removeObserver(observer: Observer)
        +notifyObservers()
    }

    class Schedule {
        -observers: List<Observer>
        -classSessions: List<ClassSession>
        +addClassSession(session: ClassSession)
        +removeClassSession(session: ClassSession)
        +optimizeSchedule()
        +registerObserver(observer: Observer)
        +removeObserver(observer: Observer)
        +notifyObservers()
    }

    class ClassSession {
        +id: String
        +subject: String
        +start: LocalDateTime
        +end: LocalDateTime
        +teacher: Teacher
    }

    Observer <|-- Teacher
    Subject <|-- Schedule
    Schedule o-- ClassSession: contains
    Schedule o-- Observer: notifies
    Teacher --> Schedule: observes
```

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/class-organizer
```

### 2 - Compile & Run the Application

```bash
./gradlew build
./gradlew run
```

### 3 - Run Tests

```bash
./gradlew test
```
