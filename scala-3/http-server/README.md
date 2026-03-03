# **Scala HTTP Server**

## Overview

This project implements a simple and extensible HTTP server in Scala 3 using the Strategy Pattern. It supports custom GET endpoints and demonstrates how to compose route handlers in a clean, extensible way.

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

    class GetRouteHandler {
        <<trait>>
        +handle(path: String): Option[String]
    }

    class HelloHandler {
        +handle(path: String): Option[String]
    }

    class PingHandler {
        +handle(path: String): Option[String]
    }

    class Main {
        +run(): Unit
    }

    GetRouteHandler <|.. HelloHandler
    GetRouteHandler <|.. PingHandler
    Main --> GetRouteHandler
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/http-server
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
