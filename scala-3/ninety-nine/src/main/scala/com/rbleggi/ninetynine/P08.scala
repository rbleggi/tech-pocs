package com.rbleggi.ninetynine

object P08 {
  def compress[A](items: List[A]): List[A] =
    println(s"Recursion step: items = $items")
    items match
      case Nil => Nil
      case head :: tail =>
        tail match
          case next :: rest
            if head == next => compress(head :: rest)
          case _ => head :: compress(tail)
}

@main def mainP08(): Unit =
  println("Eliminate consecutive duplicates of list elements.")
  val symbols = List("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e")
  println(s"The list is: $symbols")
  val compressed = P08.compress(symbols)
  println(s"The compressed list is: $compressed")
