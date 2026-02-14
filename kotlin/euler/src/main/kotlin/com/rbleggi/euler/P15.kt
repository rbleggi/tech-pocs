package com.rbleggi.euler

import java.math.BigInteger

fun mainP15() {
    fun factorial(n: BigInteger): BigInteger =
        if (n == BigInteger.ZERO) BigInteger.ONE else n * factorial(n - BigInteger.ONE)

    val gridSize = BigInteger.valueOf(20)
    val result = factorial(gridSize * BigInteger.TWO) / (factorial(gridSize) * factorial(gridSize))

    println("The number of routes through a 20x20 grid is: $result")
}
