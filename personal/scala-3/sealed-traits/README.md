# **Sealed Traits Pattern**

## Overview

This project demonstrates Scala 3's sealed traits with exhaustive pattern matching using a Brazilian payment system. Sealed traits ensure type safety by restricting inheritance to the same file and enabling compile-time completeness checking in pattern matching.

---

## Tech Stack

- **Language** -> Scala 3
- **Build Tool** -> sbt
- **Testing** -> ScalaTest 3.2.16
- **JDK** -> 25

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class PaymentMethod {
        <<sealed trait>>
        +process(amount: Double): String
    }

    class Pix {
        -key: String
        +process(amount: Double): String
    }

    class BankSlip {
        -barcode: String
        +process(amount: Double): String
    }

    class CreditCard {
        -number: String
        -installments: Int
        +process(amount: Double): String
    }

    class DebitCard {
        -number: String
        +process(amount: Double): String
    }

    class PaymentProcessor {
        +process(payment: PaymentMethod, amount: Double): String
        +calculateFee(payment: PaymentMethod, amount: Double): Double
    }

    PaymentMethod <|-- Pix
    PaymentMethod <|-- BankSlip
    PaymentMethod <|-- CreditCard
    PaymentMethod <|-- DebitCard
    PaymentProcessor --> PaymentMethod
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/sealed-traits
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
