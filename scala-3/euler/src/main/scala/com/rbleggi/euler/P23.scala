package com.rbleggi.euler

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
