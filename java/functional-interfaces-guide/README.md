# Functional Interfaces Guide

POC demonstrating Java functional interfaces, lambdas, and method references.

## What are Functional Interfaces?

Functional interfaces are interfaces with exactly one abstract method. They can be implemented using lambda expressions or method references, enabling functional programming in Java.

## Examples Included

### 1. Predicate<T>
- Tests conditions, returns boolean
- `test()`, `and()`, `or()`, `negate()`
- Use case: Filtering collections

### 2. Function<T, R>
- Transforms input to output
- `apply()`, `andThen()`, `compose()`
- Use case: Data transformation

### 3. Consumer<T>
- Accepts input, returns void
- `accept()`, `andThen()`
- Use case: Side effects, printing

### 4. Supplier<T>
- Provides output, no input
- `get()`
- Use case: Lazy evaluation, factories

### 5. UnaryOperator<T>
- Function where input & output same type
- Extends Function<T, T>
- Use case: Transformation pipelines

### 6. BinaryOperator<T>
- Combines two same-type inputs
- Extends BiFunction<T, T, T>
- Use case: Reduction operations

### 7. BiFunction<T, U, R>
- Two inputs, one output
- `apply()`, `andThen()`
- Use case: Complex transformations

### 8. BiPredicate<T, U>
- Two inputs, boolean output
- `test()`, `and()`, `or()`, `negate()`
- Use case: Relationship testing

### 9. BiConsumer<T, U>
- Two inputs, void output
- `accept()`, `andThen()`
- Use case: Map operations

### 10. Custom Functional Interface
- Create your own @FunctionalInterface
- Default methods allowed
- Single abstract method rule

### 11. Method References
- `Class::staticMethod`
- `instance::instanceMethod`
- `Class::instanceMethod`
- `Class::new`

### 12. Function Composition
- Chain functions with `andThen()`
- Reverse with `compose()`
- Combine predicates with `and()`, `or()`

## Build and Run

```bash
./gradlew run
```

Or:

```bash
gradle run
```

## Key Concepts

### Lambda Syntax
```java
(parameters) -> expression
(parameters) -> { statements; }
```

### Method Reference Types
```java
String::length              // instance method
Math::random               // static method
String::toUpperCase        // instance method of arbitrary object
ArrayList::new             // constructor
```

### Primitive Specializations
- `IntPredicate`, `IntFunction`, `IntConsumer`, `IntSupplier`
- `LongPredicate`, `DoubleFunction`, etc.
- Avoid boxing/unboxing overhead

## Common Patterns

### Filtering
```java
list.stream()
    .filter(x -> x > 0)
    .collect(Collectors.toList());
```

### Mapping
```java
list.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Reduction
```java
list.stream()
    .reduce(0, (a, b) -> a + b);
```

## Benefits

1. **Concise Code** - Less boilerplate
2. **Readability** - Clear intent
3. **Functional Style** - Immutability, composition
4. **Stream API** - Works seamlessly with streams
5. **Type Safety** - Compile-time checking

## Use Cases

- Stream operations
- Event handlers
- Strategy pattern
- Template method pattern
- Callbacks
- Async operations

