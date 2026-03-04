package com.rbleggi.ninetynine

object P49 {
  def gray(n: Int): List[String] = {
    if (n <= 0) Nil
    else if (n == 1) List("0", "1")
    else {
      val prev = gray(n - 1)
      val with0 = prev.map("0" + _)
      val with1 = prev.reverse.map("1" + _)
      with0 ++ with1
    }
  }
}

@main def mainP49(): Unit = {
  println("Gray code for n = 3:")
  println(P49.gray(3))
  println("Gray code for n = 4:")
  println(P49.gray(4))
}

