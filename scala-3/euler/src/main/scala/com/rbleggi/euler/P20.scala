package com.rbleggi.euler

// Project Euler Problem 20
// n! means n × (n − 1) × ... × 3 × 2 × 1.
// For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800,
// and the sum of the digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.
// Find the sum of the digits in the number 100!.

@main def runP20(): Unit = {
  //foldLeft to acumulate
  val factorial = (1 to 100).foldLeft(BigInt(1))(_ * _)
  val digitSum = factorial.toString.map(_.asDigit).sum
  println(digitSum)
}
