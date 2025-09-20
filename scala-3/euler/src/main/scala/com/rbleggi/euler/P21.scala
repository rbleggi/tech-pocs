package com.rbleggi.euler

// Project Euler Problem 21
// Let d(n) be defined as the sum of proper divisors of n (numbers less than n which divide evenly into n).
// If d(a) = b and d(b) = a, where a ≠ b, then a and b are an amicable pair and each of a and b are called amicable numbers.
// For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284.
// The proper divisors of 284 are 1, 2, 4, 71 and 142; so d(284) = 220.
// Evaluate the sum of all the amicable numbers under 10000.

@main def runP21(): Unit = {
  def sumProperDivisors(n: Int): Int = {
    (1 to n / 2).filter(n % _ == 0).sum
  }

  val amicableNumbers = (2 until 10000).filter { a =>
    val b = sumProperDivisors(a)
    b != a && b < 10000 && sumProperDivisors(b) == a
  }

  println(amicableNumbers.sum)
}
