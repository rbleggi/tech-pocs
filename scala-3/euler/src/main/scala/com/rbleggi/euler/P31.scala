package com.rbleggi.euler

@main def mainP31(): Unit = {
  val coins = List(1, 2, 5, 10, 20, 50, 100, 200)
  val target = 200
  val ways = Array.fill(target + 1)(0)
  ways(0) = 1

  for (coin <- coins) {
    for (amount <- coin to target) {
      ways(amount) += ways(amount - coin)
    }
  }

  println(s"Number of ways to make £2 using any number of coins: ${ways(target)}")
}
