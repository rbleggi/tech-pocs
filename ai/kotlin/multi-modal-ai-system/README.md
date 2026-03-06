# **Multi-Modal AI System**

## Overview

Multi-modal AI system demonstrating the **Strategy Pattern** for processing different input types: text, numeric, and categorical data. No machine learning libraries, just text-based processing strategies.

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

    class Input {
        <<sealed>>
    }

    class TextInput {
        +text: String
    }

    class NumericInput {
        +value: Double
    }

    class CategoricalInput {
        +category: String
    }

    class ProcessingResult {
        +input: Input
        +output: String
        +mode: String
    }

    class ProcessingStrategy {
        <<interface>>
        +process(input: Input): ProcessingResult
    }

    class TextProcessingStrategy {
        +process(input: Input): ProcessingResult
    }

    class NumericProcessingStrategy {
        +process(input: Input): ProcessingResult
    }

    class CategoricalProcessingStrategy {
        -categoryMapping: Map~String, String~
        +process(input: Input): ProcessingResult
    }

    class MultiModalAISystem {
        -strategy: ProcessingStrategy
        +process(input: Input): ProcessingResult
        +processBatch(inputs: List~Input~): List~ProcessingResult~
    }

    Input <|-- TextInput
    Input <|-- NumericInput
    Input <|-- CategoricalInput
    ProcessingStrategy <|.. TextProcessingStrategy
    ProcessingStrategy <|.. NumericProcessingStrategy
    ProcessingStrategy <|.. CategoricalProcessingStrategy
    MultiModalAISystem --> ProcessingStrategy
    ProcessingStrategy --> ProcessingResult
    ProcessingResult --> Input
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/multi-modal-ai-system
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
