package com.rbleggi.ninetynine

object P13 {
  def encodeDirect[A](items: List[A]): List[(Int, A)] =
    items match {
      case Nil => Nil
      case head :: tail =>
        val (same, rest) = tail.span(_ == head)
        ((same.length + 1), head) :: encodeDirect(rest)
    }
}

@main def mainP13(): Unit =
  println("Direct run-length encoding of a list.")
  val symbols = List("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e")
  println(s"The list is: $symbols")
  val encoded = P13.encodeDirect(symbols)
  println(s"The directly encoded list is: $encoded")
