# **Core Bank Ledger (Kotlin)**

## Overview

This project implements a simple Core Bank Ledger in Kotlin. It demonstrates basic banking operations (deposit, withdraw, transfer) with an extensible and testable design, all in a single file for proof-of-concept purposes.

---

## Tech Stack

- **Kotlin 2.2.20** → Modern JVM-based language with concise syntax and null safety.
- **Gradle** → Build automation tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class LedgerCommand {
        <<interface>>
        +execute(): Unit
    }

    class Deposit {
        +execute(): Unit
    }
    class Withdraw {
        +execute(): Unit
    }
    class Transfer {
        +execute(): Unit
    }

    class Ledger {
        +createAccount(id, initial)
        +getAccount(id)
        +record(tx)
        +printAccounts()
        +printTransactions()
    }

    class Main {
        +main(): Unit
    }

    LedgerCommand <|.. Deposit
    LedgerCommand <|.. Withdraw
    LedgerCommand <|.. Transfer
    Main --> Ledger
    Main --> LedgerCommand
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/core-bank-ledger
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
