package com.rbleggi.streams;

import java.util.*;
import java.util.stream.*;

public class StreamsDemo {

    public static void main(String[] args) {
        System.out.println("=== Java Streams Comprehensive Guide ===\n");

        demonstrateStreamCreation();
        demonstrateFilterMapReduce();
        demonstrateFlatMap();
        demonstrateCollectors();
        demonstrateGroupingAndPartitioning();
        demonstrateSortingAndDistinct();
        demonstrateMatchingAndFinding();
        demonstrateNumericStreams();
        demonstrateParallelStreams();
        demonstrateAdvancedOperations();
    }

    private static void demonstrateStreamCreation() {
        System.out.println("1. Stream Creation");

        Stream<String> fromList = List.of("A", "B", "C").stream();
        Stream<Integer> fromArray = Arrays.stream(new Integer[]{1, 2, 3});
        Stream<String> fromValues = Stream.of("X", "Y", "Z");
        Stream<Integer> generated = Stream.generate(() -> 42).limit(3);
        Stream<Integer> iterated = Stream.iterate(0, n -> n + 2).limit(5);

        System.out.println("  From list: " + fromList.toList());
        System.out.println("  From array: " + fromArray.toList());
        System.out.println("  From values: " + fromValues.toList());
        System.out.println("  Generated: " + generated.toList());
        System.out.println("  Iterated: " + iterated.toList());
        System.out.println();
    }

    private static void demonstrateFilterMapReduce() {
        System.out.println("2. Filter, Map, Reduce");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();

        List<Integer> doubled = numbers.stream()
            .map(n -> n * 2)
            .toList();

        int sum = numbers.stream()
            .reduce(0, Integer::sum);

        Optional<Integer> max = numbers.stream()
            .reduce(Integer::max);

        System.out.println("  Even numbers: " + evens);
        System.out.println("  Doubled: " + doubled);
        System.out.println("  Sum: " + sum);
        System.out.println("  Max: " + max.orElse(0));
        System.out.println();
    }

    private static void demonstrateFlatMap() {
        System.out.println("3. FlatMap - Flatten nested structures");

        List<List<Integer>> nestedList = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6, 7, 8, 9)
        );

        List<Integer> flattened = nestedList.stream()
            .flatMap(List::stream)
            .toList();

        List<String> words = List.of("Hello World", "Java Streams");
        List<String> allWords = words.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .toList();

        System.out.println("  Flattened list: " + flattened);
        System.out.println("  All words: " + allWords);
        System.out.println();
    }

    private static void demonstrateCollectors() {
        System.out.println("4. Collectors");

        List<String> words = List.of("apple", "banana", "cherry", "date", "elderberry");

        List<String> toList = words.stream().collect(Collectors.toList());
        Set<String> toSet = words.stream().collect(Collectors.toSet());
        String joined = words.stream().collect(Collectors.joining(", "));
        Map<Integer, String> toMap = words.stream()
            .collect(Collectors.toMap(String::length, s -> s, (s1, s2) -> s1 + "," + s2));

        System.out.println("  To List: " + toList);
        System.out.println("  To Set: " + toSet);
        System.out.println("  Joined: " + joined);
        System.out.println("  To Map: " + toMap);
        System.out.println();
    }

    private static void demonstrateGroupingAndPartitioning() {
        System.out.println("5. Grouping and Partitioning");

        List<String> words = List.of("apple", "banana", "cherry", "date", "elderberry", "fig");

        Map<Integer, List<String>> byLength = words.stream()
            .collect(Collectors.groupingBy(String::length));

        Map<Boolean, List<String>> partitionByLong = words.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 5));

        Map<Integer, Long> countByLength = words.stream()
            .collect(Collectors.groupingBy(String::length, Collectors.counting()));

        System.out.println("  Grouped by length: " + byLength);
        System.out.println("  Partitioned (>5 chars): " + partitionByLong);
        System.out.println("  Count by length: " + countByLength);
        System.out.println();
    }

    private static void demonstrateSortingAndDistinct() {
        System.out.println("6. Sorting and Distinct");

        List<Integer> numbers = List.of(5, 2, 8, 1, 9, 2, 3, 8, 4);

        List<Integer> sorted = numbers.stream()
            .sorted()
            .toList();

        List<Integer> sortedDesc = numbers.stream()
            .sorted(Comparator.reverseOrder())
            .toList();

        List<Integer> distinct = numbers.stream()
            .distinct()
            .toList();

        List<String> words = List.of("zebra", "apple", "banana", "Cherry");
        List<String> sortedCaseInsensitive = words.stream()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .toList();

        System.out.println("  Sorted: " + sorted);
        System.out.println("  Sorted desc: " + sortedDesc);
        System.out.println("  Distinct: " + distinct);
        System.out.println("  Case insensitive: " + sortedCaseInsensitive);
        System.out.println();
    }

    private static void demonstrateMatchingAndFinding() {
        System.out.println("7. Matching and Finding");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);

        Optional<Integer> firstEven = numbers.stream()
            .filter(n -> n % 2 == 0)
            .findFirst();

        Optional<Integer> anyEvenParallel = numbers.stream()
            .parallel()
            .filter(n -> n % 2 == 0)
            .findAny();

        System.out.println("  Any even? " + anyEven);
        System.out.println("  All positive? " + allPositive);
        System.out.println("  None negative? " + noneNegative);
        System.out.println("  First even: " + firstEven.orElse(0));
        System.out.println("  Any even (parallel): " + anyEvenParallel.orElse(0));
        System.out.println();
    }

    private static void demonstrateNumericStreams() {
        System.out.println("8. Numeric Streams (IntStream, LongStream, DoubleStream)");

        IntStream range = IntStream.range(1, 6);
        IntStream rangeClosed = IntStream.rangeClosed(1, 5);

        System.out.println("  Range 1-5: " + range.boxed().toList());
        System.out.println("  RangeClosed 1-5: " + rangeClosed.boxed().toList());

        int sum = IntStream.rangeClosed(1, 100).sum();
        OptionalDouble average = IntStream.rangeClosed(1, 10).average();
        IntSummaryStatistics stats = IntStream.rangeClosed(1, 10).summaryStatistics();

        System.out.println("  Sum 1-100: " + sum);
        System.out.println("  Average 1-10: " + average.orElse(0));
        System.out.println("  Stats 1-10: " + stats);
        System.out.println();
    }

    private static void demonstrateParallelStreams() {
        System.out.println("9. Parallel Streams");

        List<Integer> numbers = IntStream.rangeClosed(1, 1000).boxed().toList();

        long sequentialSum = numbers.stream()
            .mapToLong(Integer::longValue)
            .sum();

        long parallelSum = numbers.parallelStream()
            .mapToLong(Integer::longValue)
            .sum();

        System.out.println("  Sequential sum: " + sequentialSum);
        System.out.println("  Parallel sum: " + parallelSum);
        System.out.println("  Sums equal: " + (sequentialSum == parallelSum));
        System.out.println();
    }

    private static void demonstrateAdvancedOperations() {
        System.out.println("10. Advanced Operations");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        numbers.stream()
            .peek(n -> System.out.print("  Processing: " + n))
            .map(n -> n * 2)
            .peek(n -> System.out.println(" -> " + n))
            .toList();

        List<Integer> limited = Stream.iterate(1, n -> n + 1)
            .limit(5)
            .toList();

        List<Integer> skipped = IntStream.rangeClosed(1, 10)
            .skip(5)
            .boxed()
            .toList();

        List<Integer> takeWhile = IntStream.rangeClosed(1, 10)
            .takeWhile(n -> n < 6)
            .boxed()
            .toList();

        List<Integer> dropWhile = IntStream.rangeClosed(1, 10)
            .dropWhile(n -> n < 6)
            .boxed()
            .toList();

        System.out.println("  Limited: " + limited);
        System.out.println("  Skipped first 5: " + skipped);
        System.out.println("  Take while < 6: " + takeWhile);
        System.out.println("  Drop while < 6: " + dropWhile);

        System.out.println("\n=== Summary ===");
        System.out.println("Key Stream Operations:");
        System.out.println("- Intermediate: filter, map, flatMap, distinct, sorted, limit, skip");
        System.out.println("- Terminal: collect, reduce, forEach, count, anyMatch, findFirst");
        System.out.println("- Collectors: toList, toSet, groupingBy, partitioningBy, joining");
        System.out.println("- Numeric: IntStream, LongStream, DoubleStream with sum, average, stats");
    }
}

