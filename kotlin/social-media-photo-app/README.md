# **Social Media Sharing Photo App (Kotlin)**

## Overview

A modular social media photo sharing PoC in Kotlin, demonstrating core features including publish, tag, timeline, comments, and event-driven notifications. Followers are notified when users publish, tag, or comment on photos.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build automation tool for Kotlin projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Subject {
        <<interface>>
        +registerObserver(observer: Observer): Unit
        +removeObserver(observer: Observer): Unit
        +notifyObservers(event: Event): Unit
    }

    class Observer {
        <<interface>>
        +update(event: Event): Unit
    }

    class User {
        -name: String
        -observers: MutableList~Observer~
        -timeline: MutableList~Photo~
        +publishPhoto(photo: Photo): Unit
        +tagUser(photo: Photo, user: String): Unit
        +commentPhoto(photo: Photo, comment: Comment): Unit
        +registerObserver(observer: Observer): Unit
        +removeObserver(observer: Observer): Unit
        +notifyObservers(event: Event): Unit
    }

    class Follower {
        -followerName: String
        +update(event: Event): Unit
    }

    class Photo {
        +id: String
        +url: String
        +tags: List~String~
    }

    class Comment {
        +user: String
        +text: String
    }

    class Event {
        <<sealed class>>
    }

    class PhotoPublished {
        +photo: Photo
    }

    class UserTagged {
        +photo: Photo
        +taggedUser: String
    }

    class PhotoCommented {
        +photo: Photo
        +comment: Comment
    }

    Subject <|.. User
    Observer <|.. Follower
    User o-- Photo
    User o-- Comment
    User --> Observer : notifies
    Event <|-- PhotoPublished
    Event <|-- UserTagged
    Event <|-- PhotoCommented
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/social-media-photo-app
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```