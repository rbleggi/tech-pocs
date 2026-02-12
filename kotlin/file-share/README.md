# **File Share (Kotlin)**

## Overview

A secure file sharing system in Kotlin. Supports file upload, download, delete, list, and search, with encrypted storage and event notifications.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build automation tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class FileStorage {
        <<interface>>
        +upload(file: File)
        +download(id: String): File
        +delete(id: String)
        +list(): List<File>
        +search(query: String): List<File>
    }
    class EncryptedFileStorage {
        +upload(file: File)
        +download(id: String): File
        +delete(id: String)
        +list(): List<File>
        +search(query: String): List<File>
    }
    class FileStorageFactory {
        +create(type: String): FileStorage
    }
    class FileEventObserver {
        <<interface>>
        +onEvent(event: FileEvent)
    }
    class FileShareSystem {
        -storage: FileStorage
        -observers: List<FileEventObserver>
        +addObserver(observer: FileEventObserver)
        +removeObserver(observer: FileEventObserver)
        +notify(event: FileEvent)
    }
    FileStorage <|.. EncryptedFileStorage
    FileShareSystem --> FileStorage
    FileShareSystem --> FileEventObserver
    FileStorageFactory --> FileStorage
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/file-share
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
