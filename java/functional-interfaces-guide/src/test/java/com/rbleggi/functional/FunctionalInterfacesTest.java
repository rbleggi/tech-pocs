package com.rbleggi.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalInterfacesTest {

    @Test
    @DisplayName("Predicate should test condition")
    void predicate_testCondition_returnsBoolean() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        assertTrue(isEven.test(4));
        assertFalse(isEven.test(5));
    }

    @Test
    @DisplayName("Predicate and should combine predicates")
    void predicate_and_combinesPredicates() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> combined = isEven.and(isPositive);
        assertTrue(combined.test(4));
        assertFalse(combined.test(-2));
    }

    @Test
    @DisplayName("Predicate or should combine predicates")
    void predicate_or_combinesPredicates() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> combined = isEven.or(isPositive);
        assertTrue(combined.test(3));
        assertTrue(combined.test(2));
    }

    @Test
    @DisplayName("Function should transform input")
    void function_apply_transformsInput() {
        Function<String, Integer> length = String::length;
        assertEquals(5, length.apply("Hello"));
    }

    @Test
    @DisplayName("Function andThen should chain functions")
    void function_andThen_chainsTransformations() {
        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> addTen = x -> x + 10;
        Function<Integer, Integer> combined = square.andThen(addTen);
        assertEquals(35, combined.apply(5));
    }

    @Test
    @DisplayName("Function compose should chain functions in reverse")
    void function_compose_chainsInReverse() {
        Function<Integer, Integer> multiplyBy2 = x -> x * 2;
        Function<Integer, Integer> add3 = x -> x + 3;
        Function<Integer, Integer> combined = multiplyBy2.compose(add3);
        assertEquals(16, combined.apply(5));
    }

    @Test
    @DisplayName("Consumer should accept input")
    void consumer_accept_acceptsInput() {
        var result = new StringBuilder();
        Consumer<String> append = result::append;
        append.accept("Hello");
        assertEquals("Hello", result.toString());
    }

    @Test
    @DisplayName("Consumer andThen should chain consumers")
    void consumer_andThen_chainsConsumers() {
        var result = new StringBuilder();
        Consumer<String> append1 = s -> result.append(s);
        Consumer<String> append2 = s -> result.append("!");
        Consumer<String> combined = append1.andThen(append2);
        combined.accept("Hello");
        assertEquals("Hello!", result.toString());
    }

    @Test
    @DisplayName("Supplier should provide output")
    void supplier_get_providesOutput() {
        Supplier<String> supplier = () -> "Hello";
        assertEquals("Hello", supplier.get());
    }

    @Test
    @DisplayName("UnaryOperator should transform same type")
    void unaryOperator_apply_transformsSameType() {
        UnaryOperator<Integer> square = x -> x * x;
        assertEquals(25, square.apply(5));
    }

    @Test
    @DisplayName("BinaryOperator should combine two inputs")
    void binaryOperator_apply_combinesTwoInputs() {
        BinaryOperator<Integer> add = (a, b) -> a + b;
        assertEquals(8, add.apply(5, 3));
    }

    @Test
    @DisplayName("BiFunction should transform two inputs to output")
    void biFunction_apply_transformsTwoInputs() {
        BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
        assertEquals("HiHiHi", repeat.apply("Hi", 3));
    }

    @Test
    @DisplayName("BiPredicate should test two inputs")
    void biPredicate_test_testsTwoInputs() {
        BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
        assertTrue(isGreater.test(5, 3));
        assertFalse(isGreater.test(2, 4));
    }

    @Test
    @DisplayName("BiConsumer should accept two inputs")
    void biConsumer_accept_acceptsTwoInputs() {
        var result = new StringBuilder();
        BiConsumer<String, String> append = (a, b) -> result.append(a).append(b);
        append.accept("Hello", "World");
        assertEquals("HelloWorld", result.toString());
    }

    @Test
    @DisplayName("Custom functional interface should work with lambda")
    void customFunctionalInterface_lambda_works() {
        FunctionalInterfacesDemo.Calculator add = (a, b) -> a + b;
        assertEquals(8, add.calculate(5, 3));
    }

    @Test
    @DisplayName("Custom functional interface should have default method")
    void customFunctionalInterface_defaultMethod_works() {
        FunctionalInterfacesDemo.Calculator calc = (a, b) -> a + b;
        assertEquals(50, calc.multiplyBy10(5));
    }

    @Test
    @DisplayName("Method reference should work with Predicate")
    void methodReference_predicate_works() {
        Predicate<String> isEmpty = String::isEmpty;
        assertTrue(isEmpty.test(""));
        assertFalse(isEmpty.test("Hello"));
    }

    @Test
    @DisplayName("Predicate negate should reverse predicate")
    void predicate_negate_reversesResult() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isOdd = isEven.negate();
        assertTrue(isOdd.test(3));
        assertFalse(isOdd.test(4));
    }
}
