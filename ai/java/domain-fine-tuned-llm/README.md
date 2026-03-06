# **Domain Fine-Tuned LLM**

## Overview

Simulated domain-specific language model demonstrating the **Strategy Pattern** with specialized knowledge bases for medical, legal, and technical domains using template-based generation without actual LLM integration.

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

    class Prompt {
        +id: String
        +text: String
        +domain: String
    }

    class LLMResponse {
        +promptId: String
        +generatedText: String
        +confidence: double
        +domain: String
    }

    class KnowledgeEntry {
        +topic: String
        +content: String
        +domain: String
    }

    class DomainStrategy {
        <<sealed interface>>
        +generate(prompt: Prompt, knowledgeBase: List~KnowledgeEntry~): LLMResponse
        +getDomainName(): String
    }

    class MedicalDomain {
        +generate(prompt: Prompt, knowledgeBase: List~KnowledgeEntry~): LLMResponse
        +getDomainName(): String
    }

    class LegalDomain {
        +generate(prompt: Prompt, knowledgeBase: List~KnowledgeEntry~): LLMResponse
        +getDomainName(): String
    }

    class TechDomain {
        +generate(prompt: Prompt, knowledgeBase: List~KnowledgeEntry~): LLMResponse
        +getDomainName(): String
    }

    class DomainFineTunedLLM {
        -domainStrategies: Map~String, DomainStrategy~
        -knowledgeBase: List~KnowledgeEntry~
        +generate(prompt: Prompt): LLMResponse
        +generateBatch(prompts: List~Prompt~): List~LLMResponse~
        +getAverageConfidenceByDomain(prompts: List~Prompt~): Map~String, Double~
    }

    DomainStrategy <|.. MedicalDomain
    DomainStrategy <|.. LegalDomain
    DomainStrategy <|.. TechDomain
    DomainFineTunedLLM --> DomainStrategy
    DomainFineTunedLLM --> KnowledgeEntry
    DomainStrategy --> LLMResponse
    DomainStrategy --> Prompt
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/domain-fine-tuned-llm
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
