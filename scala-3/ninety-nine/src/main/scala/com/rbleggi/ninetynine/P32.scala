package com.rbleggi.ninetynine

class P32(val value: Int) {
  def gcd(other: Int): Int = {
    @annotation.tailrec
    def gcdRec(a: Int, b: Int): Int =
      if (b == 0) a else gcdRec(b, a % b)
    gcdRec(value.abs, other.abs)
  }
}

object P32 {
  def gcd(a: Int, b: Int): Int = new P32(a).gcd(b)
}

@main def mainP32(): Unit = {
  println("Determine the greatest common divisor of two positive integer numbers.")
  val a = 36
  val b = 63
  val result = P32.gcd(a, b)
  println(s"gcd($a, $b) = $result")
}

