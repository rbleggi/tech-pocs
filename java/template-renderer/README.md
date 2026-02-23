# **Template Renderer**

## Overview

This project follows an Object-Oriented Design approach to generate templates in HTML, CSV, and PDF formats.

---

## Tech Stack

- **Java 25** → Modern Java with enhanced language features.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
   class TemplateRenderer {
      <<abstract>>
      +render(data: Map<String, String>): byte[]
   }

   class HTMLRenderer {
      +render(data: Map<String, String>): byte[]
   }

   class CSVRenderer {
      +render(data: Map<String, String>): byte[]
   }

   class PDFRenderer {
      +render(data: Map<String, String>): byte[]
   }

   class RendererFactory {
      +getRenderer(type: String): TemplateRenderer
   }

   class FileUtil {
      +saveToFile(filename: String, content: byte[])
   }

   class Main {
      +main(args: String[])
   }

   TemplateRenderer <|-- HTMLRenderer
   TemplateRenderer <|-- CSVRenderer
   TemplateRenderer <|-- PDFRenderer
   RendererFactory --> TemplateRenderer
   Main --> RendererFactory
   Main --> FileUtil
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/template-renderer
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```