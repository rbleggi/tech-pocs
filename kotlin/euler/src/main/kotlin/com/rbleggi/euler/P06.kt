package com.rbleggi.euler

import kotlin.math.pow

fun mainP06() {
    val sumOfSquares10 = (1..10).sumOf { it * it }
    val squareOfSum10 = (1..10).sum().toDouble().pow(2).toLong()
    val difference10 = squareOfSum10 - sumOfSquares10
    println("Difference for first 10 natural numbers: $difference10")

    val sumOfSquares100 = (1..100).sumOf { it * it }
    val squareOfSum100 = (1..100).sum().toDouble().pow(2).toLong()
    val difference100 = squareOfSum100 - sumOfSquares100
    println("Difference for first 100 natural numbers: $difference100")
}
