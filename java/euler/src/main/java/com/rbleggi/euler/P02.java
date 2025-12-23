package com.rbleggi.euler;

import java.util.stream.Stream;

public class P02 {
    static class FibGenerator {
        private long a = 1;
        private long b = 2;
        private boolean first = true;
        private boolean second = true;

        public long next() {
            if (first) {
                first = false;
                return a;
            }
            if (second) {
                second = false;
                return b;
            }
            long next = a + b;
            a = b;
            b = next;
            return next;
        }
    }

    public static void main(String[] args) {
        var fibGen = new FibGenerator();
        long evenSum = Stream.generate(fibGen::next)
            .takeWhile(fib -> fib <= 4_000_000)
            .filter(fib -> fib % 2 == 0)
            .mapToLong(Long::longValue)
            .sum();
        System.out.println("Sum of even Fibonacci numbers not exceeding 4 million: " + evenSum);
    }
}
