# **Code Generation Assistant**

## Overview

Code generation assistant demonstrating the **Strategy Pattern** with different generation strategies for classes, functions, and test files using template-based text generation without LLM integration.

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

    class CodeRequest {
        +name: String
        +type: String
        +parameters: List~String~
        +options: Map~String, String~
    }

    class GeneratedCode {
        +code: String
        +language: String
        +type: String
    }

    class CodeGenerationStrategy {
        <<sealed interface>>
        +generate(request: CodeRequest): GeneratedCode
    }

    class ClassGeneratorStrategy {
        +generate(request: CodeRequest): GeneratedCode
    }

    class FunctionGeneratorStrategy {
        +generate(request: CodeRequest): GeneratedCode
    }

    class TestGeneratorStrategy {
        +generate(request: CodeRequest): GeneratedCode
    }

    class CodeGenerationAssistant {
        -strategy: CodeGenerationStrategy
        +generateCode(request: CodeRequest): GeneratedCode
        +generateBatch(requests: List~CodeRequest~): List~GeneratedCode~
    }

    CodeGenerationStrategy <|.. ClassGeneratorStrategy
    CodeGenerationStrategy <|.. FunctionGeneratorStrategy
    CodeGenerationStrategy <|.. TestGeneratorStrategy
    CodeGenerationAssistant --> CodeGenerationStrategy
    CodeGenerationStrategy --> GeneratedCode
    CodeGenerationStrategy --> CodeRequest
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/code-generation-assistant
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
