package com.rbleggi.ninetynine

object P17 {
  def split[A](n: Int, items: List[A]): (List[A], List[A]) =
    items.splitAt(n)
}

@main def mainP17(): Unit =
  println("Split a list into two parts.")
  val symbols = List("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")
  println(s"The list is: $symbols")
  val (first, second) = P17.split(3, symbols)
  println(s"First part: $first")
  println(s"Second part: $second")
