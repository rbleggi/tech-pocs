package com.rbleggi.euler

import java.math.BigInteger

fun mainP29() {
    val terms = (2..100).flatMap { a ->
        (2..100).map { b -> BigInteger.valueOf(a.toLong()).pow(b) }
    }.toSet()

    println("Number of distinct terms for 2 <= a <= 100 and 2 <= b <= 100: ${terms.size}")
}
