package com.rbleggi.ninetynine

import scala.annotation.tailrec

object P03 {
  @tailrec
  def nth[A](k: Int, items: List[A]): A =
    (k, items) match
      case (k, Nil) => throw new NoSuchElementException("nth of empty list")
      case (k, head :: tail) if k < 0 => throw new NoSuchElementException("negative index")
      case (0, head :: tail) => head
      case (k, head :: tail) => nth(k - 1, tail)
}

@main def mainP03(): Unit =
  println("Find the Kth element of a list")
  val numbers = List(1, 1, 2, 3, 5, 8)
  println(s"The list is: $numbers")
  val k = 4
  println(s"The index to find is: $k")
  val kthElement = P03.nth(k, numbers)
  println(s"The element at index $k is: $kthElement")
