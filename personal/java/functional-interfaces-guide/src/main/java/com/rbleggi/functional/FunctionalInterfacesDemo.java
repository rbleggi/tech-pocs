package com.rbleggi.functional;

import java.util.function.*;
import java.util.List;
import java.util.ArrayList;

public class FunctionalInterfacesDemo {

    public static void main(String[] args) {
        System.out.println("=== Functional Interfaces Guide ===\n");

        demonstratePredicate();
        demonstrateFunction();
        demonstrateConsumer();
        demonstrateSupplier();
        demonstrateUnaryOperator();
        demonstrateBinaryOperator();
        demonstrateBiFunction();
        demonstrateBiPredicate();
        demonstrateBiConsumer();
        demonstrateCustomFunctionalInterface();
        demonstrateMethodReferences();
        demonstrateFunctionComposition();
    }

    private static void demonstratePredicate() {
        System.out.println("1. Predicate<T> - Tests a condition, returns boolean");

        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<String> isLongString = s -> s.length() > 5;

        System.out.println("  Is 4 even? " + isEven.test(4));
        System.out.println("  Is 5 even? " + isEven.test(5));
        System.out.println("  Is 10 positive and even? " + isPositive.and(isEven).test(10));
        System.out.println("  Is -2 positive or even? " + isPositive.or(isEven).test(-2));
        System.out.println("  Is 'Hello' long? " + isLongString.test("Hello"));
        System.out.println("  Is 'Hello World' long? " + isLongString.test("Hello World"));
        System.out.println();
    }

    private static void demonstrateFunction() {
        System.out.println("2. Function<T, R> - Transforms input to output");

        Function<String, Integer> stringLength = String::length;
        Function<Integer, String> intToString = Object::toString;
        Function<String, String> toUpperCase = String::toUpperCase;

        System.out.println("  Length of 'Hello': " + stringLength.apply("Hello"));
        System.out.println("  42 to string: " + intToString.apply(42));
        System.out.println("  'hello' to upper: " + toUpperCase.apply("hello"));

        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> addTen = x -> x + 10;
        Function<Integer, Integer> squareThenAdd = square.andThen(addTen);

        System.out.println("  Square(5) then add 10: " + squareThenAdd.apply(5));
        System.out.println();
    }

    private static void demonstrateConsumer() {
        System.out.println("3. Consumer<T> - Accepts input, returns void");

        Consumer<String> printer = s -> System.out.println("  Printing: " + s);
        Consumer<Integer> squarePrinter = n -> System.out.println("  Square: " + (n * n));

        printer.accept("Hello World");
        squarePrinter.accept(5);

        Consumer<List<String>> listPrinter = list -> {
            System.out.print("  List: [");
            list.forEach(s -> System.out.print(s + " "));
            System.out.println("]");
        };

        listPrinter.accept(List.of("A", "B", "C"));

        Consumer<String> upperCase = s -> System.out.println("  Upper: " + s.toUpperCase());
        Consumer<String> combined = printer.andThen(upperCase);
        combined.accept("test");
        System.out.println();
    }

    private static void demonstrateSupplier() {
        System.out.println("4. Supplier<T> - Provides output, no input");

        Supplier<Double> randomSupplier = Math::random;
        Supplier<String> messageSupplier = () -> "Hello from Supplier";
        Supplier<List<Integer>> listSupplier = () -> List.of(1, 2, 3, 4, 5);

        System.out.println("  Random number: " + randomSupplier.get());
        System.out.println("  Message: " + messageSupplier.get());
        System.out.println("  List: " + listSupplier.get());

        Supplier<Long> timestampSupplier = System::currentTimeMillis;
        System.out.println("  Timestamp: " + timestampSupplier.get());
        System.out.println();
    }

    private static void demonstrateUnaryOperator() {
        System.out.println("5. UnaryOperator<T> - Function where input & output are same type");

        UnaryOperator<Integer> square = x -> x * x;
        UnaryOperator<String> toUpper = String::toUpperCase;
        UnaryOperator<Double> negate = x -> -x;

        System.out.println("  Square of 5: " + square.apply(5));
        System.out.println("  Upper case 'hello': " + toUpper.apply("hello"));
        System.out.println("  Negate 3.14: " + negate.apply(3.14));
        System.out.println();
    }

    private static void demonstrateBinaryOperator() {
        System.out.println("6. BinaryOperator<T> - Combines two same-type inputs");

        BinaryOperator<Integer> add = (a, b) -> a + b;
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        BinaryOperator<String> concat = (a, b) -> a + " " + b;
        BinaryOperator<Integer> max = Integer::max;

        System.out.println("  5 + 3 = " + add.apply(5, 3));
        System.out.println("  5 * 3 = " + multiply.apply(5, 3));
        System.out.println("  Concat: " + concat.apply("Hello", "World"));
        System.out.println("  Max(10, 20): " + max.apply(10, 20));
        System.out.println();
    }

    private static void demonstrateBiFunction() {
        System.out.println("7. BiFunction<T, U, R> - Two inputs, one output");

        BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
        BiFunction<Integer, Integer, Double> divide = (a, b) -> (double) a / b;
        BiFunction<String, String, Integer> indexOf = String::indexOf;

        System.out.println("  Repeat 'Hi' 3 times: " + repeat.apply("Hi", 3));
        System.out.println("  10 / 3 = " + divide.apply(10, 3));
        System.out.println("  Index of 'World' in 'Hello World': " + indexOf.apply("Hello World", "World"));
        System.out.println();
    }

    private static void demonstrateBiPredicate() {
        System.out.println("8. BiPredicate<T, U> - Two inputs, boolean output");

        BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
        BiPredicate<String, String> startsWith = String::startsWith;
        BiPredicate<Integer, Integer> isSumEven = (a, b) -> (a + b) % 2 == 0;

        System.out.println("  Is 5 > 3? " + isGreater.test(5, 3));
        System.out.println("  Does 'Hello' start with 'He'? " + startsWith.test("Hello", "He"));
        System.out.println("  Is sum of 4 and 5 even? " + isSumEven.test(4, 5));
        System.out.println();
    }

    private static void demonstrateBiConsumer() {
        System.out.println("9. BiConsumer<T, U> - Two inputs, void output");

        BiConsumer<String, Integer> printNTimes = (s, n) -> {
            for (int i = 0; i < n; i++) {
                System.out.print("  " + s);
            }
            System.out.println();
        };

        BiConsumer<String, String> printPair = (a, b) ->
            System.out.println("  Pair: (" + a + ", " + b + ")");

        printNTimes.accept("*", 5);
        printPair.accept("Key", "Value");
        System.out.println();
    }

    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);

        default int multiplyBy10(int value) {
            return value * 10;
        }
    }

    private static void demonstrateCustomFunctionalInterface() {
        System.out.println("10. Custom Functional Interface");

        Calculator add = (a, b) -> a + b;
        Calculator subtract = (a, b) -> a - b;
        Calculator multiply = (a, b) -> a * b;

        System.out.println("  Custom add(5, 3): " + add.calculate(5, 3));
        System.out.println("  Custom subtract(5, 3): " + subtract.calculate(5, 3));
        System.out.println("  Custom multiply(5, 3): " + multiply.calculate(5, 3));
        System.out.println("  Default method multiply by 10: " + add.multiplyBy10(5));
        System.out.println();
    }

    private static void demonstrateMethodReferences() {
        System.out.println("11. Method References");

        Function<String, Integer> lengthRef = String::length;
        Consumer<String> printRef = System.out::println;
        Supplier<Double> randomRef = Math::random;
        UnaryOperator<String> upperRef = String::toUpperCase;

        System.out.println("  Length using method ref: " + lengthRef.apply("Hello"));
        System.out.print("  Print using method ref: ");
        printRef.accept("Hello World");
        System.out.println("  Random using method ref: " + randomRef.get());
        System.out.println("  Upper using method ref: " + upperRef.apply("test"));
        System.out.println();
    }

    private static void demonstrateFunctionComposition() {
        System.out.println("12. Function Composition");

        Function<Integer, Integer> multiplyBy2 = x -> x * 2;
        Function<Integer, Integer> add3 = x -> x + 3;

        Function<Integer, Integer> multiplyThenAdd = multiplyBy2.andThen(add3);
        Function<Integer, Integer> addThenMultiply = multiplyBy2.compose(add3);

        System.out.println("  (5 * 2) + 3 = " + multiplyThenAdd.apply(5));
        System.out.println("  (5 + 3) * 2 = " + addThenMultiply.apply(5));

        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isOdd = isEven.negate();

        System.out.println("  Is 4 odd? " + isOdd.test(4));
        System.out.println("  Is 5 odd? " + isOdd.test(5));

        System.out.println("\n=== Summary ===");
        System.out.println("Core Functional Interfaces:");
        System.out.println("1. Predicate<T> - T -> boolean");
        System.out.println("2. Function<T,R> - T -> R");
        System.out.println("3. Consumer<T> - T -> void");
        System.out.println("4. Supplier<T> - () -> T");
        System.out.println("5. UnaryOperator<T> - T -> T");
        System.out.println("6. BinaryOperator<T> - (T,T) -> T");
        System.out.println("7. BiFunction<T,U,R> - (T,U) -> R");
        System.out.println("8. BiPredicate<T,U> - (T,U) -> boolean");
        System.out.println("9. BiConsumer<T,U> - (T,U) -> void");
    }
}

