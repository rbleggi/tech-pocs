# **Own String Implementation (Kotlin)**

## Overview

This project demonstrates a custom String implementation in Kotlin. The solution extends and encapsulates string operations in a single class, providing methods like toArray, forEach, reverse, iterator, length, charAt, equals, isEmpty, replace, substring, trim, toJson, indexOf, and hashCode.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with advanced type safety and functional programming features.
- **Gradle** → Kotlin's official build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB
    class MyString {
        -value: String
        +toArray(): CharArray
        +forEach(action: (Char) -> Unit)
        +reverse(): MyString
        +iterator(): Iterator~Char~
        +length(): Int
        +charAt(index: Int): Char
        +equals(other: Any?): Boolean
        +isEmpty(): Boolean
        +replace(old: Char, new: Char): MyString
        +substring(start: Int, end: Int): MyString
        +trim(): MyString
        +toJson(): String
        +indexOf(char: Char): Int
        +hashCode(): Int
    }
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/own-string-impl
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
