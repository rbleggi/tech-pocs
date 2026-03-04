package com.rbleggi.ninetynine


class P37(val n: Int) {
  def totientImproved: Int = {
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

@main def mainP37(): Unit = {
  println("Calculate Euler’s totient function ϕ(m) (improved). Example: 10.totientImproved == 4")
  val n = 10
  val t = new P37(n)
  println(s"Totient improved for $n: ${t.totientImproved}")
  val n2 = 315
  val t2 = new P37(n2)
  println(s"Totient improved for $n2: ${t2.totientImproved}")
}

