package com.rbleggi.ninetynine


object P34 {
  def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  def totient(m: Int): Int =
    (1 to m).count(r => gcd(r, m) == 1)
}

@main def mainP34(): Unit = {
  println("Calculate Euler’s totient function ϕ(m). Example: 10.totient == 4")
  val m = 10
  val result = P34.totient(m)
  println(s"ϕ($m) = $result")
}

