# **Core Bank Ledger**

## Overview

Banking ledger demonstrating the **Command Pattern** for executing deposit, withdrawal, and transfer operations with transaction recording and extensible command-based design.

---

## Tech Stack

- **Kotlin 2.2.20** → Modern JVM language with concise syntax and null safety.
- **Gradle** → Build automation tool with Kotlin DSL support.
- **JDK 25** → Required to run the application.
- **kotlin.test** → Testing framework.

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

    LedgerCommand <|.. Deposit
    LedgerCommand <|.. Withdraw
    LedgerCommand <|.. Transfer
    Deposit --> Ledger: records
    Withdraw --> Ledger: records
    Transfer --> Ledger: records
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
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
