package com.rbleggi.ninetynine

object P10 {
  def encode[A](items: List[A]): List[(Int, A)] =
    P09.pack(items).map(sublist => (sublist.length, sublist.head))
}

@main def mainP10(): Unit =
  println("Run-length encoding of a list.")
  val symbols = List("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e")
  println(s"The list is: $symbols")
  val encoded = P10.encode(symbols)
  println(s"The encoded list is: $encoded")
