package com.rbleggi.euler

import java.math.BigInteger

fun mainP25() {
    var a = BigInteger.ONE
    var b = BigInteger.ONE
    var idx = 2
    while (b.toString().length < 1000) {
        val next = a + b
        a = b
        b = next
        idx += 1
    }
    println("The index of the first Fibonacci term with 1000 digits is: $idx")
}
