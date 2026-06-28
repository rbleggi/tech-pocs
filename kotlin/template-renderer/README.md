# **Template Renderer (Kotlin)**

## Overview

Template rendering system demonstrating the **Template Method Pattern**. The abstract `TemplateRenderer` defines a fixed rendering skeleton (`render` -> render title, render content, assemble), while each output format (HTML, CSV, PDF) overrides the hook steps. Adding a new format means subclassing and overriding the hooks without touching the skeleton.

---

## Tech Stack

- **Kotlin 2.2.20** -> Modern JVM language with concise syntax and null safety.
- **Gradle** -> Build automation tool with Kotlin DSL support.
- **JDK 25** -> Required to run the application.
- **iText 9** -> PDF generation library.
- **JUnit 5** -> Testing framework.

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/template-renderer
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
