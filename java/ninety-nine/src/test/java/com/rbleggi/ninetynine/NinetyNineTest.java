package com.rbleggi.ninetynine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class NinetyNineTest {

    @Test
    @DisplayName("P01 last should return last element of list")
    void p01_last_returnsLastElement() {
        var numbers = List.of(1, 1, 2, 3, 5, 8);
        assertEquals(8, P01.last(numbers));
    }

    @Test
    @DisplayName("P01 last should work with single element")
    void p01_last_singleElement_returnsElement() {
        var list = List.of(42);
        assertEquals(42, P01.last(list));
    }

    @Test
    @DisplayName("P01 last should throw on empty list")
    void p01_last_emptyList_throwsException() {
        var emptyList = List.<Integer>of();
        assertThrows(NoSuchElementException.class, () -> P01.last(emptyList));
    }

    @Test
    @DisplayName("P01 last should work with strings")
    void p01_last_strings_returnsLastString() {
        var words = List.of("one", "two", "three");
        assertEquals("three", P01.last(words));
    }

    @Test
    @DisplayName("P02 penultimate should return second to last element")
    void p02_penultimate_returnsPenultimateElement() {
        var numbers = List.of(1, 1, 2, 3, 5, 8);
        assertEquals(5, P02.penultimate(numbers));
    }

    @Test
    @DisplayName("P02 penultimate should work with two elements")
    void p02_penultimate_twoElements_returnsFirst() {
        var list = List.of(10, 20);
        assertEquals(10, P02.penultimate(list));
    }

    @Test
    @DisplayName("P02 penultimate should throw on single element list")
    void p02_penultimate_singleElement_throwsException() {
        var list = List.of(42);
        assertThrows(NoSuchElementException.class, () -> P02.penultimate(list));
    }

    @Test
    @DisplayName("P02 penultimate should throw on empty list")
    void p02_penultimate_emptyList_throwsException() {
        var emptyList = List.<Integer>of();
        assertThrows(NoSuchElementException.class, () -> P02.penultimate(emptyList));
    }

    @Test
    @DisplayName("P02 penultimate should work with strings")
    void p02_penultimate_strings_returnsPenultimateString() {
        var words = List.of("one", "two", "three", "four");
        assertEquals("three", P02.penultimate(words));
    }
}
