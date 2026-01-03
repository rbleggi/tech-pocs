package com.rbleggi.euler

import kotlin.math.sqrt

fun mainP10() {
    val limit = 2000000

    val isPrime = BooleanArray(limit) { true }
    isPrime[0] = false
    isPrime[1] = false

    for (i in 2..sqrt(limit.toDouble()).toInt()) {
        if (isPrime[i]) {
            for (j in i * 2 until limit step i) {
                isPrime[j] = false
            }
        }
    }

    val sum = isPrime.withIndex().filter { it.value }.sumOf { it.index.toLong() }
    println("The sum of all primes below $limit is: $sum")
}
