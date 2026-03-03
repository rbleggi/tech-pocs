package com.rbleggi.euler

@main def runP25(): Unit = {
  import scala.math.BigInt
  var a = BigInt(1)
  var b = BigInt(1)
  var idx = 2
  while (b.toString.length < 1000) {
    val next = a + b
    a = b
    b = next
    idx += 1
  }
  println(s"The index of the first Fibonacci term with 1000 digits is: $idx")
}
