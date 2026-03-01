package com.rbleggi.euler

fun mainP21() {
    fun sumProperDivisors(n: Int): Int {
        return (1..n / 2).filter { n % it == 0 }.sum()
    }

    val amicableNumbers = (2 until 10000).filter { a ->
        val b = sumProperDivisors(a)
        b != a && b < 10000 && sumProperDivisors(b) == a
    }

    println(amicableNumbers.sum())
}
