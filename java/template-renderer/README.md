# Template Renderer Project

## Overview

This project follows an **Object-Oriented Design (OOP)** approach to generate templates in **HTML, CSV, and PDF** formats. It uses:

- **Java 21** → Modern Java with enhanced language features.
- **iText** → A library for PDF generation.

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

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/rbleggi/tech-pocs.git
   cd java/template-renderer
   ```

2. **Compiling & Running**:
   ```bash
   ./gradlew build run
   ```

3. **Tests**:
   ```sh
   ./gradlew test
   ```