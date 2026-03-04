package com.rbleggi.ninetynine


object P33 {
  def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  def isCoprimeTo(a: Int, b: Int): Boolean =
    gcd(a, b) == 1
}

@main def mainP33(): Unit = {
  println("Determine whether two positive integer numbers are coprime.")
  val a = 35
  val b = 64
  val result = P33.isCoprimeTo(a, b)
  println(s"Are $a and $b coprime? $result")
}
