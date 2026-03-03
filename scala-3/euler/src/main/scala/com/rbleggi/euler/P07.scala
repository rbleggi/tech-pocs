package com.rbleggi.euler

@main def mainP07(): Unit = {
  def isPrime(n: Int): Boolean = {
    if (n < 2) false
    else if (n == 2) true
    else if (n % 2 == 0) false
    else !(3 to math.sqrt(n).toInt by 2).exists(n % _ == 0)
  }

  var count = 0
  var candidate = 1
  var lastPrime = 2

  while (count < 10001) {
    candidate += 1
    if (isPrime(candidate)) {
      count += 1
      lastPrime = candidate
    }
  }

  println(s"The 10,001st prime number is: $lastPrime")
}
