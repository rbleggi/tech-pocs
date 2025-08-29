package com.rbleggi.euler

@main def mainP02(): Unit = {
  // Generate Fibonacci sequence up to 4 million
  def fibs: LazyList[Int] = 1 #:: 2 #:: fibs.zip(fibs.tail).map { case (a, b) => a + b }
  val fibsUpTo4M = fibs.takeWhile(_ <= 4000000)
  val evenSum = fibsUpTo4M.filter(_ % 2 == 0).sum
  println(s"Sum of even Fibonacci numbers not exceeding 4 million: $evenSum")
}

