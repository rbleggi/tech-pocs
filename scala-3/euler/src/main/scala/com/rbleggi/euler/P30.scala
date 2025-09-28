package com.rbleggi.euler

// Project Euler Problem 30
// Find the sum of all numbers that can be written as the sum of fifth powers of their digits.
// 1 is not included as it is not a sum.

@main def mainP30(): Unit = {
  val power = 5
  val upper = 6 * math.pow(9, power).toInt

  val result = (2 to upper).filter { n =>
    n == n.toString.map(_.asDigit).map(d => math.pow(d, power).toInt).sum
  }.sum

  println(s"Sum of all numbers that can be written as the sum of fifth powers of their digits: $result")
}
