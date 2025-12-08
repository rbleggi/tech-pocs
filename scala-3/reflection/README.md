# Reflection Example

POC demonstrating runtime reflection in Scala 3.

## What is Reflection?

Reflection allows examining and modifying program structure at runtime:
- Inspect types, fields, methods
- Invoke methods dynamically
- Create instances dynamically
- Access private members

## Examples Included

### 1. Class Information
- Get class name (full and simple)
- Runtime type inspection

### 2. Fields Inspection
- List all fields in a class
- Access field values via reflection

### 3. Field Values Access
- Read private fields
- Dynamic field access by name

### 4. Method Invocation
- Call methods by name
- Pass arguments dynamically

### 5. Instance Creation
- Create objects without direct constructor call
- Dynamic instantiation via reflection

### 6. Type Inspection with ClassTag
- Examine type information
- Check interfaces, primitives
- List methods

### 7. Serialization
- Convert objects to Map
- Generic serialization utility

### 8. Pretty Printing
- Format objects as strings
- Automatic field enumeration

### 9. JSON Serialization
- Convert any object to JSON
- Recursive field processing

### 10. Type Comparison
- Check type relationships
- isAssignableFrom checks

## Build and Run

```bash
sbt run
```

## Use Cases

1. **Serialization/Deserialization** - JSON, XML, binary
2. **Dependency Injection** - Spring, Guice
3. **ORM Frameworks** - Hibernate, Slick
4. **Testing Frameworks** - ScalaTest, JUnit
5. **Generic Utilities** - Copy, compare, validate

## Cautions

- **Performance** - runtime overhead vs compile-time
- **Type Safety** - lose compile-time checks
- **Complexity** - harder to debug
- **Alternatives** - prefer macros or type classes when possible

## Scala 3 Reflection

Scala 3 uses Java reflection API (scala.reflect is deprecated). For compile-time metaprogramming, use macros instead.

