# **Advanced Monads**

## Overview

This project demonstrates advanced monads in Scala 3 including IO, State, Reader, and Writer monads. These monads are used to manage side effects, stateful computations, environment dependencies, and logging in a purely functional way. The example uses Brazilian banking operations to illustrate practical applications.

---

## Tech Stack

- **Language** -> Scala 3.6.3
- **Build Tool** -> sbt 1.10.11
- **Runtime** -> JDK 25
- **Testing** -> ScalaTest 3.2.16

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class IO~A~ {
        +run: () => A
        +map[B](f: A => B): IO[B]
        +flatMap[B](f: A => IO[B]): IO[B]
    }

    class State~S,A~ {
        +run: S => (A, S)
        +map[B](f: A => B): State[S, B]
        +flatMap[B](f: A => State[S, B]): State[S, B]
    }

    class Reader~R,A~ {
        +run: R => A
        +map[B](f: A => B): Reader[R, B]
        +flatMap[B](f: A => Reader[R, B]): Reader[R, B]
    }

    class Writer~W,A~ {
        +run: (A, W)
        +map[B](f: A => B): Writer[W, B]
        +flatMap[B](f: A => Writer[W, B]): Writer[W, B]
    }

    class ContaBancaria {
        +titular: String
        +saldo: Double
        +cpf: String
    }

    class BancoMonads {
        +depositar(valor: Double): State[ContaBancaria, Double]
        +sacar(valor: Double): State[ContaBancaria, Option[Double]]
        +transferir(origem, destino, valor): IO[String]
    }

    BancoMonads --> State~S,A~
    BancoMonads --> IO~A~
    BancoMonads --> ContaBancaria
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/advanced-monads
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
