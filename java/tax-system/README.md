# **TAX System**

## Overview

This project demonstrates a dynamic tax calculation system where different products are subject to varying tax rates based on state and year. The system follows the Specification Pattern, ensuring that tax rules are modular, extensible, and easily adaptable to changes.

---

## Tech Stack

- **Java 25** → Modern Java with records and pattern matching.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
   direction TB

   class Product {
      - String name
      - String category
   }

   class TaxConfiguration {
      - String state
      - int year
      - Map<String, Double> rates
   }

   class TaxSpecification {
      <<interface>>
      +isSatisfiedBy(state: String, year: int): boolean
      +calculateTax(product: Product, price: double): double
   }

   class DefaultTaxSpecification {
      - TaxConfiguration config
      +isSatisfiedBy(state: String, year: int): boolean
      +calculateTax(product: Product, price: double): double
   }

   class TaxCalculator {
      +calculateTax(state: String, year: int, product: Product, price: double): double
   }

   TaxSpecification <|-- DefaultTaxSpecification
   TaxCalculator --> TaxSpecification : uses
   TaxCalculator --> Product : uses
   DefaultTaxSpecification --> TaxConfiguration : contains
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/tax-system
```

### 2 - Compile & Run the Application
```bash
./gradlew build run
```

### 3 - Run Tests
```bash
./gradlew test
```