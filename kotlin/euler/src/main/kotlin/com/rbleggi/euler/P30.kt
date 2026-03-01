package com.rbleggi.euler

import kotlin.math.pow

fun mainP30() {
    val power = 5
    val upper = (6 * 9.0.pow(power)).toInt()

    val result = (2..upper).filter { n ->
        n == n.toString().sumOf { it.digitToInt().toDouble().pow(power).toInt() }
    }.sum()

    println("Sum of all numbers that can be written as the sum of fifth powers of their digits: $result")
}
