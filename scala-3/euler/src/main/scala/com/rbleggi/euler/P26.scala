package com.rbleggi.euler

// Project Euler Problem 26: Reciprocal cycles
// Find the value of d < 1000 for which 1/d contains the longest recurring cycle in its decimal fraction part.

@main def runP26(): Unit = {
  def recurringCycleLength(denominator: Int): Int = {
    var remainder = 1
    var position = 0
    val seen = scala.collection.mutable.Map[Int, Int]()
    while (remainder != 0 && !seen.contains(remainder)) {
      seen(remainder) = position
      remainder = (remainder * 10) % denominator
      position += 1
    }
    if (remainder == 0) 0
    else position - seen(remainder)
  }

  var maxLength = 0
  var resultD = 0
  for (d <- 2 until 1000) {
    val length = recurringCycleLength(d)
    if (length > maxLength) {
      maxLength = length
      resultD = d
    }
  }
  println(s"The value of d < 1000 with the longest recurring cycle is $resultD (cycle length: $maxLength)")
}

