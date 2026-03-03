package com.rbleggi.euler

@main def mainP27(): Unit = {
  def isPrime(n: Int): Boolean = {
    if (n < 2) false
    else if (n == 2) true
    else if (n % 2 == 0) false
    else !(3 to math.sqrt(n.abs).toInt by 2).exists(n % _ == 0)
  }

  var maxCount = 0
  var bestA = 0
  var bestB = 0

  for (a <- -999 to 999; b <- -1000 to 1000) {
    var n = 0
    while (isPrime(n * n + a * n + b)) n += 1
    if (n > maxCount) {
      maxCount = n
      bestA = a
      bestB = b
    }
  }

  println(s"Max consecutive primes: $maxCount for a = $bestA, b = $bestB. Product: ${bestA * bestB}")
}
