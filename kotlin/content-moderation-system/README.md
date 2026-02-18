# **Content Moderation System**

## Overview

Content moderation system demonstrating the **Strategy Pattern** with three filter types: keyword-based blocking, regex pattern matching, and length validation for user-generated content.

---

## Tech Stack

- **Kotlin 2.1.10** → Modern JVM language with concise syntax and null safety
- **Gradle 9.3.0** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Content {
        +id: String
        +text: String
        +author: String
    }

    class ModerationResult {
        +content: Content
        +isAllowed: Boolean
        +reason: String
        +filterType: String
    }

    class ModerationFilter {
        <<interface>>
        +moderate(content: Content): ModerationResult
    }

    class KeywordFilter {
        -blockedKeywords: Set~String~
        +moderate(content: Content): ModerationResult
    }

    class RegexFilter {
        -patterns: List~Regex~
        +moderate(content: Content): ModerationResult
    }

    class LengthFilter {
        -minLength: Int
        -maxLength: Int
        +moderate(content: Content): ModerationResult
    }

    class ContentModerationSystem {
        -filters: List~ModerationFilter~
        +moderate(content: Content): List~ModerationResult~
        +isContentAllowed(content: Content): Boolean
        +moderateBatch(contents: List~Content~): Map~String, List~ModerationResult~~
    }

    ModerationFilter <|.. KeywordFilter
    ModerationFilter <|.. RegexFilter
    ModerationFilter <|.. LengthFilter
    ContentModerationSystem --> ModerationFilter
    ModerationFilter --> ModerationResult
    ModerationResult --> Content
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/content-moderation-system
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
