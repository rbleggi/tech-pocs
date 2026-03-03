package com.rbleggi.euler

@main def mainP05(): Unit = {
  def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)

  def lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

  val result = (1L to 20L).reduce(lcm)

  println(s"The smallest positive number evenly divisible by all numbers from 1 to 20 is: $result")
}
