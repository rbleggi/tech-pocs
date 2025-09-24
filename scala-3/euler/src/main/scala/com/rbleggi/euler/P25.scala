package com.rbleggi.euler

// Project Euler Problem 25

// The Fibonacci sequence is defined by the recurrence relation:
// F_n = F_{n - 1} + F_{n - 2}, where F_1 = 1 and F_2 = 1.
// Hence the first 12 terms will be:
// F_1 = 1
// F_2 = 1
// F_3 = 2
// F_4 = 3
// F_5 = 5
// F_6 = 8
// F_7 = 13
// F_8 = 21
// F_9 = 34
// F_{10} = 55
// F_{11} = 89
// F_{12} = 144
// The 12th term, F_{12}, is the first term to contain three digits.
// What is the index of the first term in the Fibonacci sequence to contain 1000 digits?

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
