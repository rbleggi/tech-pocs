# **Domain Fine-Tuned LLM**

## Overview

Simulated domain-specific language model demonstrating the **Strategy Pattern** with specialized knowledge bases: medical, legal, and tech domains. Template-based responses without actual LLM integration.

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

    class DomainQuery {
        +question: String
        +domain: String
    }

    class DomainResponse {
        +query: DomainQuery
        +answer: String
        +confidence: Double
    }

    class DomainKnowledgeBase {
        <<interface>>
        +query(question: String): DomainResponse?
    }

    class MedicalKnowledgeBase {
        -knowledge: Map~String, String~
        +query(question: String): DomainResponse?
    }

    class LegalKnowledgeBase {
        -knowledge: Map~String, String~
        +query(question: String): DomainResponse?
    }

    class TechKnowledgeBase {
        -knowledge: Map~String, String~
        +query(question: String): DomainResponse?
    }

    class DomainFineTunedLLM {
        -domainStrategies: Map~String, DomainKnowledgeBase~
        +ask(query: DomainQuery): DomainResponse
        +askMultipleDomains(question: String): Map~String, DomainResponse?~
    }

    DomainKnowledgeBase <|.. MedicalKnowledgeBase
    DomainKnowledgeBase <|.. LegalKnowledgeBase
    DomainKnowledgeBase <|.. TechKnowledgeBase
    DomainFineTunedLLM --> DomainKnowledgeBase
    DomainKnowledgeBase --> DomainResponse
    DomainResponse --> DomainQuery
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/domain-fine-tuned-llm
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
