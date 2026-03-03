package com.rbleggi.euler

@main def runP15(): Unit = {
  def factorial(n: BigInt): BigInt = if (n == 0) 1 else n * factorial(n - 1)

  val gridSize = 20
  val result = factorial(2 * gridSize) / (factorial(gridSize) * factorial(gridSize))

  println(s"The number of routes through a 20x20 grid is: $result")
}
