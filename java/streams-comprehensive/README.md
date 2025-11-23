# Streams Comprehensive Guide

POC demonstrating Java Stream API with comprehensive examples.

## What are Streams?

Streams provide a functional approach to processing collections. They support operations like filter, map, reduce in a declarative way.

## Examples Included

### 1. Stream Creation
- From collections (List, Set)
- From arrays
- From values (Stream.of)
- Generated streams
- Iterate streams

### 2. Filter, Map, Reduce
- `filter()` - Select elements
- `map()` - Transform elements
- `reduce()` - Combine elements
- Terminal operations

### 3. FlatMap
- Flatten nested structures
- Stream of streams to single stream
- Split strings into words

### 4. Collectors
- `toList()`, `toSet()`
- `joining()` - String concatenation
- `toMap()` - Collection to map
- Custom collectors

### 5. Grouping and Partitioning
- `groupingBy()` - Group by key
- `partitioningBy()` - Split by condition
- Downstream collectors
- Counting within groups

### 6. Sorting and Distinct
- `sorted()` - Natural order
- `sorted(Comparator)` - Custom order
- `distinct()` - Remove duplicates
- Case-insensitive sorting

### 7. Matching and Finding
- `anyMatch()` - At least one
- `allMatch()` - All elements
- `noneMatch()` - No elements
- `findFirst()`, `findAny()`

### 8. Numeric Streams
- `IntStream`, `LongStream`, `DoubleStream`
- `range()`, `rangeClosed()`
- `sum()`, `average()`, `max()`, `min()`
- `summaryStatistics()`

### 9. Parallel Streams
- `parallelStream()` - Concurrent processing
- Performance considerations
- Thread safety
- When to use parallel

### 10. Advanced Operations
- `peek()` - Debugging
- `limit()`, `skip()` - Pagination
- `takeWhile()`, `dropWhile()` - Conditional limits
- Stream pipeline optimization

## Build and Run

```bash
./gradlew run
```

## Key Concepts

### Stream Operations

**Intermediate** (return Stream):
- `filter()`, `map()`, `flatMap()`
- `distinct()`, `sorted()`, `limit()`, `skip()`
- `peek()`, `takeWhile()`, `dropWhile()`

**Terminal** (produce result):
- `collect()`, `reduce()`, `forEach()`
- `count()`, `min()`, `max()`
- `anyMatch()`, `allMatch()`, `noneMatch()`
- `findFirst()`, `findAny()`

### Lazy Evaluation

Streams are lazy - intermediate operations are not executed until a terminal operation is called.

### Short-Circuiting

Operations like `anyMatch()`, `findFirst()` can terminate early.

## Common Patterns

### Transformation Pipeline
```java
list.stream()
    .filter(x -> x > 0)
    .map(x -> x * 2)
    .sorted()
    .collect(Collectors.toList());
```

### Grouping
```java
map = list.stream()
    .collect(Collectors.groupingBy(
        Item::getCategory,
        Collectors.counting()
    ));
```

### Reduction
```java
sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);
```

## Benefits

1. **Declarative** - What, not how
2. **Composable** - Chain operations
3. **Parallel** - Easy parallelization
4. **Lazy** - Efficient evaluation
5. **Functional** - Immutable data

## Performance Tips

- Avoid boxing/unboxing (use IntStream)
- Parallel for large datasets
- Short-circuit when possible
- Reusable streams are not allowed
- Consider collectors overhead

## Use Cases

- Data transformation
- Filtering collections
- Aggregation
- Grouping/partitioning
- Bulk operations
- Report generation

