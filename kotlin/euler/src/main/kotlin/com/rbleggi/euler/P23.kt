package com.rbleggi.euler

import kotlin.math.sqrt

fun mainP23() {
    fun sumProperDivisors(n: Int): Int {
        if (n < 2) return 0
        return (1..sqrt(n.toDouble()).toInt()).flatMap { i ->
            if (n % i == 0) {
                val j = n / i
                when {
                    i == 1 -> listOf(1)
                    i == j -> listOf(i)
                    else -> listOf(i, j)
                }
            } else emptyList()
        }.filter { it < n }.sum()
    }

    val limit = 28123
    val abundant = (1..limit).filter { n -> sumProperDivisors(n) > n }
    val canBeWritten = BooleanArray(limit + 1)
    for (i in abundant) {
        for (j in abundant) {
            if (i + j <= limit) {
                canBeWritten[i + j] = true
            }
        }
    }
    val result = (1..limit).filterNot { canBeWritten[it] }.sum()
    println("Sum of all positive integers which cannot be written as the sum of two abundant numbers: $result")
}
