# TAX System Project

## Overview

This project demonstrates a tax calculation system where different products are subject to different tax rates based on
the state and year.

- **Kotlin** → A concise, modern JVM-based language.

## Features

- **Product-based Taxation**: Different products have distinct tax rates.
- **State-Specific Taxes**: Each state may define its own tax rates.
- **Yearly Tax Changes**: Taxes can change depending on the year.
- **Scalable Architecture**: Designed for easy expansion and modification.

## Class Diagram

Below is a **UML class diagram** representing the core structure of the TAX system:

```mermaid
classDiagram
    class Product {
        <<sealedclass>>
        +String name
        +double price
    }

    class Electronic {
        +String name
        +double price
    }

    class Book {
        +String name
        +double price
    }

    class Food {
        +String name
        +double price
    }

    class State {
        +String name
        +String code
    }

    class TaxRule {
        +State state
        +Product product
        +int year
        +double taxRate
        +calculateTax(): double
    }

    class TaxCalculator {
        +calculateTotalPrice(product: Product, state: State, year: int): double
    }

    Product <|-- Electronic
    Product <|-- Book
    Product <|-- Food
    Product --> TaxRule
    State --> TaxRule
    TaxRule --> TaxCalculator
```

---

## Prerequisites

- **JDK 21** → Ensure Java is installed.
- **Gradle** → Used for dependency management.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.tlcinternal.com/rbleggi/tech-pocs.git
   cd kotlin/tax-system
   ```

2. **Compiling & Running**:
   ```sh
   ./gradlew build run
   ```

3. **Tests**:
   ```sh
   ./gradlew test
   ```