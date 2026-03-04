package com.rbleggi.ninetynine

object P18 {
  def slice[A](i: Int, k: Int, items: List[A]): List[A] =
    items.drop(i).take(k - i)
}

@main def mainP18(): Unit =
  println("Extract a slice from a list.")
  val symbols = List("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")
  println(s"The list is: $symbols")
  val sliced = P18.slice(3, 7, symbols)
  println(s"The slice from 3 to 7 is: $sliced")
