package com.rbleggi.euler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class EulerTest {

    @Test
    @DisplayName("P01 should calculate sum of multiples of 3 or 5 below 10")
    void p01_sumBelow10_returns23() {
        int sum = IntStream.range(1, 10)
            .filter(n -> n % 3 == 0 || n % 5 == 0)
            .sum();
        assertEquals(23, sum);
    }

    @Test
    @DisplayName("P01 should calculate sum of multiples of 3 or 5 below 1000")
    void p01_sumBelow1000_returnsCorrectAnswer() {
        int sum = IntStream.range(1, 1000)
            .filter(n -> n % 3 == 0 || n % 5 == 0)
            .sum();
        assertEquals(233168, sum);
    }

    @Test
    @DisplayName("P02 FibGenerator should generate Fibonacci sequence")
    void p02_fibGenerator_generatesFibonacci() {
        var gen = new P02.FibGenerator();
        assertEquals(1, gen.next());
        assertEquals(2, gen.next());
        assertEquals(3, gen.next());
        assertEquals(5, gen.next());
        assertEquals(8, gen.next());
    }

    @Test
    @DisplayName("P02 should calculate sum of even Fibonacci numbers below 4 million")
    void p02_sumEvenFibonacci_returnsCorrectAnswer() {
        var fibGen = new P02.FibGenerator();
        long evenSum = java.util.stream.Stream.generate(fibGen::next)
            .takeWhile(fib -> fib <= 4_000_000)
            .filter(fib -> fib % 2 == 0)
            .mapToLong(Long::longValue)
            .sum();
        assertEquals(4613732, evenSum);
    }

    @Test
    @DisplayName("P03 should find largest prime factor of 13195")
    void p03_largestPrimeFactor_of13195_returns29() {
        long result = P03.largestPrimeFactor(13195);
        assertEquals(29, result);
    }

    @Test
    @DisplayName("P03 should find largest prime factor of 600851475143")
    void p03_largestPrimeFactor_ofTarget_returnsCorrectAnswer() {
        long result = P03.largestPrimeFactor(600851475143L);
        assertEquals(6857, result);
    }

    @Test
    @DisplayName("P03 should handle prime numbers")
    void p03_largestPrimeFactor_ofPrime_returnsSelf() {
        long result = P03.largestPrimeFactor(17);
        assertEquals(17, result);
    }

    @Test
    @DisplayName("P03 should handle small numbers")
    void p03_largestPrimeFactor_ofSmallNumber_returnsCorrectFactor() {
        long result = P03.largestPrimeFactor(12);
        assertEquals(3, result);
    }
}
