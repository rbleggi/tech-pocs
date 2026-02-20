# **Code Generation Assistant**

## Overview

Code generation assistant demonstrating the **Strategy Pattern** with three generators: class, function, and test generators. Text-based code generation without actual compilation or LLM integration.

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

    class CodeRequest {
        +type: String
        +name: String
        +parameters: Map~String, String~
    }

    class GeneratedCode {
        +code: String
        +language: String
        +type: String
    }

    class CodeGenerationStrategy {
        <<interface>>
        +generate(request: CodeRequest): GeneratedCode
    }

    class ClassGenerationStrategy {
        +generate(request: CodeRequest): GeneratedCode
    }

    class FunctionGenerationStrategy {
        +generate(request: CodeRequest): GeneratedCode
    }

    class TestGenerationStrategy {
        +generate(request: CodeRequest): GeneratedCode
    }

    class CodeGenerationAssistant {
        -strategy: CodeGenerationStrategy
        +generate(request: CodeRequest): GeneratedCode
        +generateMultiple(requests: List~CodeRequest~): List~GeneratedCode~
    }

    CodeGenerationStrategy <|.. ClassGenerationStrategy
    CodeGenerationStrategy <|.. FunctionGenerationStrategy
    CodeGenerationStrategy <|.. TestGenerationStrategy
    CodeGenerationAssistant --> CodeGenerationStrategy
    CodeGenerationStrategy --> GeneratedCode
    CodeGenerationStrategy --> CodeRequest
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/code-generation-assistant
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
