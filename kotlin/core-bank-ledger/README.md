# Core Bank Ledger PoC

## Overview

This project implements a simple Core Bank Ledger using the Command Pattern in Kotlin. It demonstrates basic banking operations (deposit, withdraw, transfer) with an extensible and testable design, all in a single file for proof-of-concept purposes.

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with concise syntax and null safety.
- **Gradle** → Build automation tool.
- **JDK 25** → Required to run the application.

---

## Features
- **In-memory account and transaction ledger**
- **Command Pattern**: Encapsulates each operation as a command for extensibility
- **Simple CLI PoC**: Run and see account balances and transaction logs
- **Extensible**: Add new operations by implementing the `LedgerCommand` interface

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

## Implementation Details

- The `LedgerCommand` interface defines the interface for all ledger operations.
- Concrete commands (`Deposit`, `Withdraw`, `Transfer`) implement the operation logic.
- The `Ledger` class manages accounts and transaction history in memory.
- The `main` function creates accounts, executes a sequence of commands, and prints results.
- To add new operations, implement the `LedgerCommand` interface and use it in the main runner.

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/core-bank-ledger
```

### **2️ - Compile & Run the Application**

```bash
./gradlew run
```

### **3️ - Run Tests**

```bash
./gradlew test
```
