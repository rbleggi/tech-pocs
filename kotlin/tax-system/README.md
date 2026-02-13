# **TAX System (Kotlin)**

## Overview

This project demonstrates a flexible tax calculation system that calculates product prices based on tax rules defined by year, state, and product.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build tool for Kotlin projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram

   class YearSpecification {
      <<interface>>
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class 2023YearSpecification {
      -stateSpecifications: List~StateSpecification~
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class 2024YearSpecification {
      -stateSpecifications: List~StateSpecification~
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class CASpecification2023 {
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class TXSpecification2023 {
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class CASpecification2024 {
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class TXSpecification2024 {
      +isSatisfiedBy(candidate: TaxRule): Boolean
   }

   class TaxRule {
      +state: State
      +product: String
      +year: Int
      +taxRate: Double
      +calculateTax(price: Double): Double
   }

   class TaxCalculator {
      +calculateTotalPrice(product: String, price: Double, state: State, year: Int): Double
   }

   YearSpecification <|-- 2023YearSpecification
   YearSpecification <|-- 2024YearSpecification

   2023YearSpecification --> CASpecification2023 : uses
   2023YearSpecification --> TXSpecification2023 : uses
   2024YearSpecification --> CASpecification2024 : uses
   2024YearSpecification --> TXSpecification2024 : uses

   TaxCalculator --> YearSpecification : uses
   YearSpecification ..> TaxRule : evaluates
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/tax-system
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```