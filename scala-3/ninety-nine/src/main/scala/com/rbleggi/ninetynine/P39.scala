package com.rbleggi.ninetynine

object P39 {
  def isPrime(n: Int): Boolean =
    if (n < 2) false
    else !(2 to math.sqrt(n).toInt).exists(n % _ == 0)

  def listPrimesinRange(r: Range): List[Int] =
    r.filter(isPrime).toList
}

@main def mainP39(): Unit = {
  println("List of primes in range 7 to 31:")
  val primes = P39.listPrimesinRange(7 to 31)
  println(primes)
}

