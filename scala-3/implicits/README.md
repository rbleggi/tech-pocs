# Implicits Example (Scala 3 Given/Using)

POC demonstrating Scala 3 given/using syntax (replacement for implicits).

## What are Givens/Implicits?

In Scala 3, the old implicit keyword is replaced with `given` (for definitions) and `using` (for parameters). They enable:
- Automatic parameter passing
- Extension methods
- Type classes
- Dependency injection

## Examples Included

### 1. Context Parameters (Given/Using)
- Sort function with implicit Ordering
- Automatic instance resolution
- Works with Int, String, Double

### 2. Implicit Config Pattern
- Default configuration via given
- Override with local given
- Dependency injection pattern

### 3. Type Conversions
- Converter type class
- String ↔ Int, String ↔ Boolean
- Composable conversions

### 4. Extension Methods - String
- toSnakeCase / toCamelCase
- isNumeric check
- repeat method

### 5. Extension Methods - Int
- times method for loops
- Duration helpers (seconds, minutes)
- Custom DSL creation

### 6. Extension Methods - List
- secondOption accessor
- splitAt with predicate
- Enhance existing types

### 7. Printable Type Class
- Custom printing behavior
- Composable instances
- Alternative to toString

### 8. Execution Context
- Named contexts
- Runtime configuration
- Async execution pattern

## Build and Run

```bash
sbt run
```

## Scala 2 vs Scala 3

### Scala 2
```scala
implicit val config: Config = ...
def foo(implicit config: Config) = ...
implicit class RichString(s: String) {
  def toSnakeCase = ...
}
```

### Scala 3
```scala
given config: Config = ...
def foo(using config: Config) = ...
extension (s: String)
  def toSnakeCase = ...
```

## Key Benefits

1. **Cleaner Syntax** - more explicit intent
2. **Type Safety** - compiler verification
3. **No Boilerplate** - automatic parameter passing
4. **Extension Methods** - enhance any type
5. **Dependency Injection** - without frameworks

