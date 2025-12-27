package com.rbleggi.euler

fun mainP03() {
    fun largestPrimeFactor(n: Long): Long {
        var num = n
        var factor = 2L
        var lastFactor = 1L
        while (factor * factor <= num) {
            if (num % factor == 0L) {
                lastFactor = factor
                num /= factor
                while (num % factor == 0L) {
                    num /= factor
                }
            }
            factor += 1
        }
        return if (num > 1) num else lastFactor
    }

    val testNumber = 13195L
    val testResult = largestPrimeFactor(testNumber)
    println("Largest prime factor of $testNumber: $testResult")

    val targetNumber = 600851475143L
    val result = largestPrimeFactor(targetNumber)
    println("Largest prime factor of $targetNumber: $result")
}
