package com.rbleggi.euler

import kotlin.math.abs
import kotlin.math.sqrt

fun mainP27() {
    fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        if (n == 2) return true
        if (n % 2 == 0) return false
        return !(3..sqrt(abs(n).toDouble()).toInt() step 2).any { n % it == 0 }
    }

    var maxCount = 0
    var bestA = 0
    var bestB = 0

    for (a in -999..999) {
        for (b in -1000..1000) {
            var n = 0
            while (isPrime(n * n + a * n + b)) n += 1
            if (n > maxCount) {
                maxCount = n
                bestA = a
                bestB = b
            }
        }
    }

    println("Max consecutive primes: $maxCount for a = $bestA, b = $bestB. Product: ${bestA * bestB}")
}
