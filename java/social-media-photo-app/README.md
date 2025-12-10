# **Social Media Sharing Photo App**

## **Overview**

A modular social media photo sharing PoC in Java, demonstrating core features (publish, tag, timeline, comments) and event-driven notifications using the **Observer Pattern**. Followers are notified when users publish, tag, or comment on photos.

---

## **Tech Stack**

- **Java 21** → Modern Java with sealed interfaces and pattern matching.
- **Gradle** → Build automation tool for Java projects.
- **JDK 21** → Required to run the application.

---

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB

    class Subject {
        <<interface>>
        +registerObserver(observer: Observer): void
        +removeObserver(observer: Observer): void
        +notifyObservers(event: Event): void
    }

    class Observer {
        <<interface>>
        +update(event: Event): void
    }

    class User {
        -name: String
        -observers: List~Observer~
        -timeline: List~Photo~
        +publishPhoto(photo: Photo): void
        +tagUser(photo: Photo, user: String): void
        +commentPhoto(photo: Photo, comment: Comment): void
        +registerObserver(observer: Observer): void
        +removeObserver(observer: Observer): void
        +notifyObservers(event: Event): void
    }

    class Follower {
        -followerName: String
        +update(event: Event): void
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
        <<sealed interface>>
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

## **Setup Instructions**

### **1 - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/social-media-photo-app
```

### **2 - Compile & Run the Application**

```bash
./gradlew build
./gradlew run
```

### **3 - Run Tests**

```bash
./gradlew test
```