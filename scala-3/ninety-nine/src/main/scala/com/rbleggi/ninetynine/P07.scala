package com.rbleggi.ninetynine

object P07 {
  def flatten(items: List[Any]): List[Any] =
    items match
      case Nil => Nil
      case (head: List[_]) :: tail =>
        flatten(head) ++ flatten(tail)
      case head :: tail =>
        head :: flatten(tail)
}

@main def mainP07(): Unit =
  println("Flatten a nested list structure.")
  val nestedList = List(List(1, 1), 2, List(3, List(5, 8)))
  println(s"The list is: $nestedList")
  val flatList = P07.flatten(nestedList)
  println(s"The flattened list is: $flatList")
