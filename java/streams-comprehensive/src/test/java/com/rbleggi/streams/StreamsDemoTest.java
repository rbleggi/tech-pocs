package com.rbleggi.streams;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

class StreamsDemoTest {

    @Test
    @DisplayName("Stream from list should create stream")
    void streamCreation_fromList_createsStream() {
        var list = List.of("A", "B", "C");
        var stream = list.stream();
        assertEquals(3, stream.count());
    }

    @Test
    @DisplayName("Stream from array should create stream")
    void streamCreation_fromArray_createsStream() {
        var stream = Arrays.stream(new Integer[]{1, 2, 3});
        assertEquals(3, stream.count());
    }

    @Test
    @DisplayName("Stream filter should filter elements")
    void filter_filtersByPredicate() {
        var numbers = List.of(1, 2, 3, 4, 5, 6);
        var evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        assertEquals(3, evens.size());
        assertTrue(evens.contains(2));
        assertTrue(evens.contains(4));
        assertTrue(evens.contains(6));
    }

    @Test
    @DisplayName("Stream map should transform elements")
    void map_transformsElements() {
        var numbers = List.of(1, 2, 3);
        var doubled = numbers.stream()
            .map(n -> n * 2)
            .toList();
        assertEquals(List.of(2, 4, 6), doubled);
    }

    @Test
    @DisplayName("Stream reduce should aggregate values")
    void reduce_aggregatesValues() {
        var numbers = List.of(1, 2, 3, 4, 5);
        var sum = numbers.stream()
            .reduce(0, Integer::sum);
        assertEquals(15, sum);
    }

    @Test
    @DisplayName("FlatMap should flatten nested structures")
    void flatMap_flattensNestedStructures() {
        var nested = List.of(
            List.of(1, 2),
            List.of(3, 4),
            List.of(5, 6)
        );
        var flattened = nested.stream()
            .flatMap(List::stream)
            .toList();
        assertEquals(List.of(1, 2, 3, 4, 5, 6), flattened);
    }

    @Test
    @DisplayName("Collectors toList should collect to list")
    void collectors_toList_collectsToList() {
        var numbers = List.of(1, 2, 3);
        var result = numbers.stream().collect(Collectors.toList());
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Collectors joining should join strings")
    void collectors_joining_joinsStrings() {
        var words = List.of("Hello", "World");
        var joined = words.stream().collect(Collectors.joining(" "));
        assertEquals("Hello World", joined);
    }

    @Test
    @DisplayName("Collectors groupingBy should group by key")
    void collectors_groupingBy_groupsByKey() {
        var words = List.of("apple", "banana", "cherry", "date");
        var grouped = words.stream()
            .collect(Collectors.groupingBy(String::length));
        assertTrue(grouped.containsKey(4));
        assertTrue(grouped.containsKey(5));
        assertTrue(grouped.containsKey(6));
        assertEquals(1, grouped.get(4).size());
    }

    @Test
    @DisplayName("Collectors partitioningBy should partition by predicate")
    void collectors_partitioningBy_partitionsByPredicate() {
        var numbers = List.of(1, 2, 3, 4, 5, 6);
        var partitioned = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        assertEquals(3, partitioned.get(true).size());
        assertEquals(3, partitioned.get(false).size());
    }

    @Test
    @DisplayName("Stream sorted should sort elements")
    void sorted_sortsElements() {
        var numbers = List.of(5, 2, 8, 1, 9);
        var sorted = numbers.stream()
            .sorted()
            .toList();
        assertEquals(List.of(1, 2, 5, 8, 9), sorted);
    }

    @Test
    @DisplayName("Stream distinct should remove duplicates")
    void distinct_removesDuplicates() {
        var numbers = List.of(1, 2, 2, 3, 3, 3);
        var distinct = numbers.stream()
            .distinct()
            .toList();
        assertEquals(List.of(1, 2, 3), distinct);
    }

    @Test
    @DisplayName("Stream anyMatch should test if any element matches")
    void anyMatch_testsIfAnyMatches() {
        var numbers = List.of(1, 2, 3, 4, 5);
        assertTrue(numbers.stream().anyMatch(n -> n > 3));
        assertFalse(numbers.stream().anyMatch(n -> n > 10));
    }

    @Test
    @DisplayName("Stream allMatch should test if all elements match")
    void allMatch_testsIfAllMatch() {
        var numbers = List.of(2, 4, 6, 8);
        assertTrue(numbers.stream().allMatch(n -> n % 2 == 0));
        assertFalse(numbers.stream().allMatch(n -> n > 5));
    }

    @Test
    @DisplayName("Stream noneMatch should test if no elements match")
    void noneMatch_testsIfNoneMatch() {
        var numbers = List.of(1, 3, 5, 7);
        assertTrue(numbers.stream().noneMatch(n -> n % 2 == 0));
        assertFalse(numbers.stream().noneMatch(n -> n > 5));
    }

    @Test
    @DisplayName("Stream findFirst should find first element")
    void findFirst_findsFirstElement() {
        var numbers = List.of(1, 2, 3, 4, 5);
        var first = numbers.stream()
            .filter(n -> n > 2)
            .findFirst();
        assertTrue(first.isPresent());
        assertEquals(3, first.get());
    }

    @Test
    @DisplayName("IntStream range should generate range")
    void intStream_range_generatesRange() {
        var range = IntStream.range(1, 5).boxed().toList();
        assertEquals(List.of(1, 2, 3, 4), range);
    }

    @Test
    @DisplayName("IntStream rangeClosed should generate closed range")
    void intStream_rangeClosed_generatesClosedRange() {
        var range = IntStream.rangeClosed(1, 5).boxed().toList();
        assertEquals(List.of(1, 2, 3, 4, 5), range);
    }

    @Test
    @DisplayName("IntStream sum should calculate sum")
    void intStream_sum_calculatesSum() {
        var sum = IntStream.rangeClosed(1, 10).sum();
        assertEquals(55, sum);
    }

    @Test
    @DisplayName("IntStream average should calculate average")
    void intStream_average_calculatesAverage() {
        var average = IntStream.rangeClosed(1, 10).average();
        assertTrue(average.isPresent());
        assertEquals(5.5, average.getAsDouble(), 0.01);
    }

    @Test
    @DisplayName("Stream limit should limit elements")
    void limit_limitsElements() {
        var limited = Stream.iterate(1, n -> n + 1)
            .limit(5)
            .toList();
        assertEquals(5, limited.size());
        assertEquals(List.of(1, 2, 3, 4, 5), limited);
    }

    @Test
    @DisplayName("Stream skip should skip elements")
    void skip_skipsElements() {
        var skipped = IntStream.rangeClosed(1, 10)
            .skip(5)
            .boxed()
            .toList();
        assertEquals(5, skipped.size());
        assertEquals(List.of(6, 7, 8, 9, 10), skipped);
    }

    @Test
    @DisplayName("Stream takeWhile should take while predicate is true")
    void takeWhile_takesWhileTrue() {
        var taken = IntStream.rangeClosed(1, 10)
            .takeWhile(n -> n < 6)
            .boxed()
            .toList();
        assertEquals(List.of(1, 2, 3, 4, 5), taken);
    }

    @Test
    @DisplayName("Stream dropWhile should drop while predicate is true")
    void dropWhile_dropsWhileTrue() {
        var dropped = IntStream.rangeClosed(1, 10)
            .dropWhile(n -> n < 6)
            .boxed()
            .toList();
        assertEquals(List.of(6, 7, 8, 9, 10), dropped);
    }
}
