package com.rbleggi.ninetynine

object P15 {
  def duplicateN[A](n: Int, items: List[A]): List[A] =
    items.flatMap(e => List.fill(n)(e))
}

@main def mainP15(): Unit =
  println("Duplicate the elements of a list a given number of times.")
  val symbols = List("a", "b", "c", "c", "d")
  println(s"The list is: $symbols")
  val duplicated = P15.duplicateN(3, symbols)
  println(s"The duplicated list is: $duplicated")
