package com.rbleggi.ninetynine

import scala.annotation.tailrec

object P01 {
  @tailrec
  def last[A](items: List[A]): A =
    items match
      case Nil => throw new NoSuchElementException("last of empty list")
      case single :: Nil => single
      case head :: tail => last(tail)
}

@main def mainP01(): Unit =
  println("Find the last element of a list.")
  val numbers = List(1, 1, 2, 3, 5, 8)
  println(s"The list is: $numbers")
  val lastElement = P01.last(numbers)
  println(s"The last element is: $lastElement")
