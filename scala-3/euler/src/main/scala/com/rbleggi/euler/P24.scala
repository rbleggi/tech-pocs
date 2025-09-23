package com.rbleggi.euler

// Project Euler Problem 24

// A permutation is an ordered arrangement of objects. For example, 3124 is one possible permutation of the digits 1, 2, 3 and 4. If all of the permutations are listed numerically or alphabetically, we call it lexicographic order. The lexicographic permutations of 0, 1 and 2 are:
// 012   021   102   120   201   210
// What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?

@main def runP24(): Unit = {
  val digits = (0 to 9).toList
  var k = 1000000 - 1 // zero-based index
  var available = digits
  var result = List.empty[Int]

  for (i <- 9 to 0 by -1) {
    val f = (1 to i).product
    val idx = if (f == 0) 0 else k / f
    result = result :+ available(idx)
    available = available.patch(idx, Nil, 1)
    k = k % f
  }

  println(s"The millionth lexicographic permutation is: ${result.mkString}")
}
