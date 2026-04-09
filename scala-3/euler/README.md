# **Project Euler**

## Overview

Solutions to mathematical and computational problems from Project Euler, implemented in Scala 3. Each solution aims for clarity, efficiency, and correctness using functional programming idioms.

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

    class P01 {
        +solve(): Long
    }

    class P02 {
        +solve(): Long
    }

    class P03 {
        +solve(): Long
    }

    class P04 {
        +solve(): Long
    }

    class P05 {
        +solve(): Long
    }

    P01 ..> P02
    P02 ..> P03
    P03 ..> P04
    P04 ..> P05
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/euler
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
