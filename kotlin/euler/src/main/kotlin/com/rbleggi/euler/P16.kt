package com.rbleggi.euler

import java.math.BigInteger

fun mainP16() {
    val number = BigInteger.TWO.pow(1000)
    val digitSum = number.toString().sumOf { it.digitToInt() }
    println(digitSum)
}
