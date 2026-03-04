package com.rbleggi.ninetynine


object P35 {
  def primeFactors(n: Int): List[Int] = {
    def factors(x: Int, divisor: Int = 2, acc: List[Int] = Nil): List[Int] = {
      if (x < 2) acc.reverse
      else if (x % divisor == 0) factors(x / divisor, divisor, divisor :: acc)
      else factors(x, divisor + 1, acc)
    }
    factors(n)
  }
}

@main def mainP35(): Unit = {
  println("Determine the prime factors of a given positive integer. Example: 315.primeFactors == List(3, 3, 5, 7)")
  val n = 315
  val result = P35.primeFactors(n)
  println(s"Prime factors of $n: $result")
}

