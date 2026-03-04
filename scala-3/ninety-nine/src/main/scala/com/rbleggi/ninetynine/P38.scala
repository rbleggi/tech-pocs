package com.rbleggi.ninetynine


object P38 {
  def totientBrute(m: Int): Int = {
    def gcd(a: Int, b: Int): Int =
      if (b == 0) a else gcd(b, a % b)
    (1 to m).count(r => gcd(r, m) == 1)
  }

  def totientImproved(n: Int): Int = {
    def primeFactorMultiplicity: List[(Int, Int)] = {
      def factors(x: Int, divisor: Int = 2, acc: List[Int] = Nil): List[Int] = {
        if (x < 2) acc.reverse
        else if (x % divisor == 0) factors(x / divisor, divisor, divisor :: acc)
        else factors(x, divisor + 1, acc)
      }
      factors(n).groupBy(identity).map { case (prime, list) => (prime, list.size) }.toList
    }
    primeFactorMultiplicity.foldLeft(1) { case (acc, (p, m)) =>
      acc * (p - 1) * math.pow(p, m - 1).toInt
    }
  }
}

@main def mainP38(): Unit = {
  val n = 10090
  println(s"Comparing Euler’s totient function methods for $n:")
  val brute = P38.totientBrute(n)
  val improved = P38.totientImproved(n)
  println(s"Brute force (P34): ϕ($n) = $brute")
  println(s"Improved (P37): ϕ($n) = $improved")
  println(s"Results are equal: ${brute == improved}")
}

