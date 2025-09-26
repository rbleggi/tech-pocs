package com.rbleggi.euler

// Project Euler Problem 28
// Find the sum of the numbers on the diagonals in a 1001 by 1001 spiral
// The spiral starts with 1 in the center and moves to the right in a clockwise direction

@main def mainP28(): Unit = {
  val size = 1001
  var sum = 1

  for (n <- 3 to size by 2) {
    val square = n * n
    val step = n - 1
    sum += square + (square - step) + (square - 2 * step) + (square - 3 * step)
  }

  println(s"Sum of diagonals in a $size x $size spiral: $sum")
}

