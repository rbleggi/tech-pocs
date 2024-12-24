package com.rbleggi.euler;

public class P03 {
    static long largestPrimeFactor(long n) {
        long num = n;
        long factor = 2;
        long lastFactor = 1;
        while (factor * factor <= num) {
            if (num % factor == 0) {
                lastFactor = factor;
                num /= factor;
                while (num % factor == 0) {
                    num /= factor;
                }
            }
            factor += 1;
        }
        return num > 1 ? num : lastFactor;
    }

    public static void main(String[] args) {
        long testNumber = 13195L;
        long testResult = largestPrimeFactor(testNumber);
        System.out.println("Largest prime factor of " + testNumber + ": " + testResult);

        long targetNumber = 600851475143L;
        long result = largestPrimeFactor(targetNumber);
        System.out.println("Largest prime factor of " + targetNumber + ": " + result);
    }
}
