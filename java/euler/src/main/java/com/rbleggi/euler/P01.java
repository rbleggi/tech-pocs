package com.rbleggi.euler;

import java.util.stream.IntStream;

public class P01 {
    public static void main(String[] args) {
        int sumBelow10 = IntStream.range(1, 10)
            .filter(n -> n % 3 == 0 || n % 5 == 0)
            .sum();
        System.out.println("Sum of multiples of 3 or 5 below 10: " + sumBelow10 + " (expected: 23)");

        int sumBelow1000 = IntStream.range(1, 1000)
            .filter(n -> n % 3 == 0 || n % 5 == 0)
            .sum();
        System.out.println("Sum of multiples of 3 or 5 below 1000: " + sumBelow1000);
    }
}
