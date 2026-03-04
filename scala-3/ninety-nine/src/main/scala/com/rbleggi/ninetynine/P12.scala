package com.rbleggi.ninetynine

object P12 {
  def decode[A](items: List[(Int, A)]): List[A] =
    items.flatMap { case (count, elem) => List.fill(count)(elem) }
}

@main def mainP12(): Unit =
  println("Decode a run-length encoded list.")
  val encoded = List((4, "a"), (1, "b"), (2, "c"), (2, "a"), (1, "d"), (4, "e"))
  println(s"The encoded list is: $encoded")
  val decoded = P12.decode(encoded)
  println(s"The decoded list is: $decoded")
