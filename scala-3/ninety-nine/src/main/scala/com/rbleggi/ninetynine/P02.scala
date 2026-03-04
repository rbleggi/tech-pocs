package com.rbleggi.ninetynine

import scala.annotation.tailrec

object P02 {
  @tailrec
  def penultimate[A](items: List[A]): A =
    items match
      case Nil | _ :: Nil => throw new NoSuchElementException("penultimate of list with less than two elements")
      case penultimateElem :: lastElem :: Nil => penultimateElem
      case head :: tail => penultimate(tail)
}

@main def mainP02(): Unit =
  println("Find the last but one element of a list.")
  val numbers = List(1, 1, 2, 3, 5, 8)
  println(s"The list is: $numbers")
  val penultimateElement = P02.penultimate(numbers)
  println(s"The penultimate element is: $penultimateElement")
