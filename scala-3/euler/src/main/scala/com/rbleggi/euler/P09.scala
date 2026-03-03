package com.rbleggi.euler

@main def mainP09(): Unit = {
  for (a <- 1 to 998) {
    for (b <- a + 1 to 999) {
      val c = 1000 - a - b
      if (a * a + b * b == c * c) {
        println(s"The Pythagorean triplet is: a=$a, b=$b, c=$c")
        println(s"The product abc is: ${a * b * c}")
        return
      }
    }
  }
}
