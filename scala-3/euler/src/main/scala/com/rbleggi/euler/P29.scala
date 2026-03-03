package com.rbleggi.euler

@main def mainP29(): Unit = {
  val terms = (for {
    a <- 2 to 100
    b <- 2 to 100
  } yield BigInt(a).pow(b)).toSet

  println(s"Number of distinct terms for 2 <= a <= 100 and 2 <= b <= 100: ${terms.size}")
}
