package com.rbleggi.euler

import kotlin.math.sqrt

fun mainP07() {
    fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        if (n == 2) return true
        if (n % 2 == 0) return false
        return !(3..sqrt(n.toDouble()).toInt() step 2).any { n % it == 0 }
    }

    var count = 0
    var candidate = 1
    var lastPrime = 2

    while (count < 10001) {
        candidate += 1
        if (isPrime(candidate)) {
            count += 1
            lastPrime = candidate
        }
    }

    println("The 10,001st prime number is: $lastPrime")
}
