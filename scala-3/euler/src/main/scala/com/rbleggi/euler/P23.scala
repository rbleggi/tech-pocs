package com.rbleggi.euler

// Project Euler Problem 23
//
// A perfect number is a number for which the sum of its proper divisors is exactly equal to the number. 
// For example, the sum of the proper divisors of 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
// A number n is called deficient if the sum of its proper divisors is less than n and it is called abundant if this sum exceeds n.
// As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the smallest number that can be written as the sum of two abundant numbers is 24. 
// By mathematical analysis, it can be shown that all integers greater than 28123 can be written as the sum of two abundant numbers. 
// However, this upper limit cannot be reduced any further by analysis even though it is known that the greatest number that cannot be expressed as the sum of two abundant numbers is less than this limit.
// Find the sum of all the positive integers which cannot be written as the sum of two abundant numbers.

@main def runP23(): Unit = {
  def sumProperDivisors(n: Int): Int = {
    if (n < 2) 0
    else (1 to math.sqrt(n).toInt).flatMap { i =>
      if (n % i == 0) {
        val j = n / i
        if (i == 1) Seq(1)
        else if (i == j) Seq(i)
        else Seq(i, j)
      } else Seq()
    }.filter(_ < n).sum
  }

  val limit = 28123
  val abundant = (1 to limit).filter(n => sumProperDivisors(n) > n)
  val canBeWritten = Array.fill(limit + 1)(false)
  for {
    i <- abundant
    j <- abundant
    if i + j <= limit
  } canBeWritten(i + j) = true
  val result = (1 to limit).filterNot(canBeWritten).sum
  println(s"Sum of all positive integers which cannot be written as the sum of two abundant numbers: $result")
}
