package com.rbleggi.euler

@main def runP24(): Unit = {
  val digits = (0 to 9).toList
  var k = 1000000 - 1
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
