package com.rbleggi.ninetynine

object P04 {
  def length[A](items: List[A]): Int =
    println(s"Recursion step: $items")
    items match
      case Nil => 0
      case head :: tail => 1 + length(tail)
}

@main def mainP04(): Unit =
  println("Find the number of elements of a list.")
  val numbers = List(1, 1, 2, 3, 5, 8)
  println(s"The list is: $numbers")
  val count = P04.length(numbers)
  println(s"The number of elements is: $count")
