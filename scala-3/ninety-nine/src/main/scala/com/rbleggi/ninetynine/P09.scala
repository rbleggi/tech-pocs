package com.rbleggi.ninetynine

object P09 {
  def pack[A](items: List[A]): List[List[A]] =
    println(s"Recursion step: items = $items")
    items match
      case Nil => Nil
      case head :: tail =>
        val (packed, rest) = tail.span(_ == head)
        (head :: packed) :: pack(rest)
}

@main def mainP09(): Unit =
  println("Pack consecutive duplicates of list elements into sublists.")
  val symbols = List("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e")
  println(s"The list is: $symbols")
  val packed = P09.pack(symbols)
  println(s"The packed list is: $packed")
