package com.rbleggi.euler

@main def runP20(): Unit = {
  val factorial = (1 to 100).foldLeft(BigInt(1))(_ * _)
  val digitSum = factorial.toString.map(_.asDigit).sum
  println(digitSum)
}
