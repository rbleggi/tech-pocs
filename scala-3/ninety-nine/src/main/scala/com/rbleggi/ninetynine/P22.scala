package com.rbleggi.ninetynine

object P22 {
  def range(start: Int, end: Int): List[Int] =
    (start to end).toList
}

@main def mainP22(): Unit =
  println("Create a list containing all integers within a given range.")
  val r = P22.range(4, 9)
  println(s"The range from 4 to 9 is: $r")

