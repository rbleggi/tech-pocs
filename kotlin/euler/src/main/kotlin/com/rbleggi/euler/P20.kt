package com.rbleggi.euler

import java.math.BigInteger

fun mainP20() {
    val factorial = (1..100).fold(BigInteger.ONE) { acc, i -> acc * BigInteger.valueOf(i.toLong()) }
    val digitSum = factorial.toString().sumOf { it.digitToInt() }
    println(digitSum)
}
