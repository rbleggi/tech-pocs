# Unused Class Detector

## Overview

This project detects unused Java classes in source files. The program reads the file, analyzes class declarations and usages, and prints unused classes to the console using the **Visitor Pattern** for AST traversal.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class ASTNode {
        <<interface>>
    }

    class ClassDecl {
        +name: String
    }

    class ClassUsage {
        +name: String
    }

    class SourceFile {
        +content: String
    }

    class ASTVisitor {
        <<interface>>
        +visit(source: SourceFile): AnalysisResult
    }

    class UnusedClassVisitor {
        -classDeclPattern: Pattern
        -classUsagePattern: Pattern
        +visit(source: SourceFile): AnalysisResult
    }

    class AnalysisResult {
        +unusedClasses: List~String~
    }

    ASTNode <|-- ClassDecl
    ASTNode <|-- ClassUsage
    ASTNode <|-- SourceFile
    ASTVisitor <|.. UnusedClassVisitor
    UnusedClassVisitor --> AnalysisResult
```

---

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/rbleggi/tech-pocs.git
   cd java/unused-class-detector
   ```

2. **Compiling & Running**:
   ```bash
   ./gradlew build run
   ```

3. **Tests**:
   ```bash
   ./gradlew test
   ```
