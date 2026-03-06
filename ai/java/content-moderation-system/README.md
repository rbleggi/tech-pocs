# **Content Moderation System**

## Overview

Content moderation system demonstrating the **Strategy Pattern** with multiple filter strategies including keyword filtering, regex pattern matching, and length validation for text-based content moderation.

---

## Tech Stack

- **Java 25** → Latest JDK with modern language features including records and sealed interfaces.
- **Gradle** → Build automation and dependency management.
- **JUnit 5** → Testing framework for unit tests.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Content {
        +id: String
        +text: String
        +author: String
        +type: String
    }

    class ModerationResult {
        +contentId: String
        +flagged: boolean
        +reason: String
        +severity: double
        +filterType: String
    }

    class ModerationFilter {
        <<sealed interface>>
        +moderate(content: Content): ModerationResult
    }

    class KeywordFilter {
        -bannedWords: Set~String~
        -severityPerWord: double
        +moderate(content: Content): ModerationResult
    }

    class RegexFilter {
        -patterns: Map~String, Pattern~
        +moderate(content: Content): ModerationResult
    }

    class LengthFilter {
        -minLength: int
        -maxLength: int
        +moderate(content: Content): ModerationResult
    }

    class ContentModerationSystem {
        -filters: List~ModerationFilter~
        +moderateContent(content: Content): List~ModerationResult~
        +isContentApproved(content: Content): boolean
        +filterApprovedContent(contents: List~Content~): List~Content~
        +getModerationStats(contents: List~Content~): Map~Boolean, Long~
    }

    ModerationFilter <|.. KeywordFilter
    ModerationFilter <|.. RegexFilter
    ModerationFilter <|.. LengthFilter
    ContentModerationSystem --> ModerationFilter
    ModerationFilter --> ModerationResult
    ModerationFilter --> Content
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/content-moderation-system
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
