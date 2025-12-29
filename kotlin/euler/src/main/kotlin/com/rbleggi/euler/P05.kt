package com.rbleggi.euler

fun mainP05() {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    val result = (1L..20L).reduce { acc, i -> lcm(acc, i) }

    println("The smallest positive number evenly divisible by all numbers from 1 to 20 is: $result")
}
