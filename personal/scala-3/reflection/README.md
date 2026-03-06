# **Reflection**

## Overview

POC demonstrating runtime reflection in Scala 3 using the Java reflection API. Covers class inspection, field access, method invocation, dynamic instantiation, and generic serialization utilities.

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

    class ReflectionUtils {
        +getFields(obj: Any): List[Field]
        +getFieldValue(obj: Any, name: String): Any
        +invokeMethod(obj: Any, name: String, args: Any*): Any
        +createInstance[T](clazz: Class[T]): T
        +toMap(obj: Any): Map[String, Any]
        +toJson(obj: Any): String
    }

    class ClassInfo {
        +name: String
        +simpleName: String
        +fields: List[String]
        +methods: List[String]
    }

    ReflectionUtils --> ClassInfo
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/reflection
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
