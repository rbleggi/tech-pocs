# **json4s Deep Dive**

## Overview

A practical deep-dive into json4s for Scala 3 developers, covering custom serializers for ADTs and enums, advanced JValue transformations, Option/Either/sealed trait handling, error handling, and testing JSON with ScalaTest.

---

## Tech Stack

- **Language** -> Scala 3
- **Build Tool** -> sbt
- **Testing** -> ScalaTest 3.2.16
- **JDK** -> 25
- **json4s** -> JSON serialization library for Scala

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Formats {
        <<trait>>
        +serializers: List[Serializer[_]]
    }

    class CustomSerializer~T~ {
        +deserialize: PartialFunction[JValue, T]
        +serialize: PartialFunction[Any, JValue]
    }

    class JValue {
        <<sealed trait>>
    }

    class JObject {
        +fields: List[JField]
    }

    class JArray {
        +values: List[JValue]
    }

    class JString {
        +value: String
    }

    Formats --> CustomSerializer~T~
    JValue <|-- JObject
    JValue <|-- JArray
    JValue <|-- JString
    CustomSerializer~T~ --> JValue
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/json4s
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
