package com.rbleggi.euler

fun mainP14() {
    val cache = mutableMapOf<Long, Int>(1L to 1)

    fun collatzLength(n: Long): Int {
        return cache.getOrPut(n) {
            if (n % 2 == 0L) 1 + collatzLength(n / 2)
            else 1 + collatzLength(3 * n + 1)
        }
    }

    var maxLength = 0
    var result = 0

    for (i in 1 until 1000000) {
        val length = collatzLength(i.toLong())
        if (length > maxLength) {
            maxLength = length
            result = i
        }
    }

    println("The starting number under one million that produces the longest Collatz chain is: $result")
    println("The length of the chain is: $maxLength")
}
