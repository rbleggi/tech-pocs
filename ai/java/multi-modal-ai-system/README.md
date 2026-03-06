# **Multi-Modal AI System**

## Overview

Multi-modal input processing system demonstrating the **Strategy Pattern** with different processing modes for text, numeric, and categorical data using rule-based transformations without ML libraries.

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

    class InputData {
        <<sealed interface>>
    }

    class TextInput {
        +text: String
    }

    class NumericInput {
        +value: double
    }

    class CategoricalInput {
        +category: String
        +validCategories: List~String~
    }

    class ProcessingResult {
        +inputType: String
        +processedValue: Object
        +metadata: Map~String, Object~
    }

    class ProcessingStrategy {
        <<sealed interface>>
        +process(input: InputData): ProcessingResult
    }

    class TextProcessingStrategy {
        +process(input: InputData): ProcessingResult
    }

    class NumericProcessingStrategy {
        +process(input: InputData): ProcessingResult
    }

    class CategoricalProcessingStrategy {
        +process(input: InputData): ProcessingResult
        -createOneHotEncoding(category: String, validCategories: List~String~): List~Integer~
    }

    class MultiModalAISystem {
        -strategies: Map~Class, ProcessingStrategy~
        +process(input: InputData): ProcessingResult
        +processBatch(inputs: List~InputData~): List~ProcessingResult~
        +getInputTypeDistribution(inputs: List~InputData~): Map~String, Long~
    }

    InputData <|.. TextInput
    InputData <|.. NumericInput
    InputData <|.. CategoricalInput
    ProcessingStrategy <|.. TextProcessingStrategy
    ProcessingStrategy <|.. NumericProcessingStrategy
    ProcessingStrategy <|.. CategoricalProcessingStrategy
    MultiModalAISystem --> ProcessingStrategy
    ProcessingStrategy --> ProcessingResult
    ProcessingStrategy --> InputData
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/multi-modal-ai-system
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
