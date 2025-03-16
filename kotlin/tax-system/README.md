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
- **Specification Pattern**: Clearly defines flexible and reusable tax rule criteria (e.g., product, state, and year).

## Class Diagram

Below is a **UML class diagram** representing the core structure of the TAX system:

```mermaid
classDiagram
    class Product {
        <<sealedclass>>
        +name: String
        +price: Double
    }

    class Electronic
    class Book
    class Food

    class State {
        +name: String
        +code: String
    }

    class TaxRule {
        +state: State
        +product: Product
        +year: Int
        +taxRate: Double
        +calculateTax(): Double
    }

    class Specification~T~ {
        <<interface>>
        +matches(candidate: T): Boolean
        +and(other: Specification~T~): Specification~T~
        +or(other: Specification~T~): Specification~T~
    }

    class ProductSpecification
    class StateSpecification
    class YearSpecification

    class TaxCalculator {
        +calculateTotalPrice(product: Product, state: State, year: Int): Double
    }

    Product <|-- Electronic
    Product <|-- Book
    Product <|-- Food

    Product --> TaxRule
    State --> TaxRule
    TaxRule --> TaxCalculator

    Specification~T~ <|.. ProductSpecification
    Specification~T~ <|-- StateSpecification
    Specification~T~ <|-- YearSpecification

    Specification~T~ ..> TaxRule : evaluates
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