package com.rbleggi.euler

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
