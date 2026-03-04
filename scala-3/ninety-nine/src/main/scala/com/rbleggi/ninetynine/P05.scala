package com.rbleggi.ninetynine

object P05 {
  def reverse[A](items: List[A]): List[A] =
    println(s"Recursion step: $items")
    items match
      case Nil => Nil
      case head :: tail => reverse(tail) :+ head
}

@main def mainP05(): Unit =
  println("Reverse a list.")
  val numbers = List(1, 1, 2, 3, 5, 8)
  println(s"The list is: $numbers")
  val reversed = P05.reverse(numbers)
  println(s"The reversed list is: $reversed")
