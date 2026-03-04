package com.rbleggi.ninetynine

object P14 {
  def duplicate[A](items: List[A]): List[A] =
    items.flatMap(e => List(e, e))
}

@main def mainP14(): Unit =
  println("Duplicate the elements of a list.")
  val symbols = List("a", "b", "c", "c", "d")
  println(s"The list is: $symbols")
  val duplicated = P14.duplicate(symbols)
  println(s"The duplicated list is: $duplicated")
