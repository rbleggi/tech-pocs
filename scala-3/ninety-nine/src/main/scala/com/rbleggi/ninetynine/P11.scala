package com.rbleggi.ninetynine

object P11 {
  def encodeModified[A](items: List[A]): List[Any] =
    P09.pack(items).map {
      case single :: Nil => single
      case group => (group.length, group.head)
    }
}

@main def mainP11(): Unit =
  println("Modified run-length encoding of a list.")
  val symbols = List("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e")
  println(s"The list is: $symbols")
  val encoded = P11.encodeModified(symbols)
  println(s"The modified encoded list is: $encoded")
