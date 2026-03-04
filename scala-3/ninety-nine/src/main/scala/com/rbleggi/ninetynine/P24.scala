package com.rbleggi.ninetynine

object P24 {
  def lotto(n: Int, m: Int): List[Int] =
    P23.randomSelect(n, (1 to m).toList)
}

@main def mainP24(): Unit = {
  println("Lotto: Draw N different random numbers from the set 1..M.")
  val numbers = P24.lotto(6, 49)
  println(s"Lotto numbers: $numbers")
}
