package com.rbleggi.euler

fun mainP28() {
    val size = 1001
    var sum = 1

    for (n in 3..size step 2) {
        val square = n * n
        val step = n - 1
        sum += square + (square - step) + (square - 2 * step) + (square - 3 * step)
    }

    println("Sum of diagonals in a $size x $size spiral: $sum")
}
