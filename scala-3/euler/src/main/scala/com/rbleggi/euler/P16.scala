package com.rbleggi.euler

@main def runP16(): Unit = {
  val number = BigInt(2).pow(1000)
  val digitSum = number.toString.map(_.asDigit).sum
  println(digitSum)
}
