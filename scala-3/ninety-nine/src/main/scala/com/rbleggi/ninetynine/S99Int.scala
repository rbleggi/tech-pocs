package com.rbleggi.ninetynine

class S99Int(val value: Int) {
  def isPrime: Boolean =
    if (value <= 1) false
    else if (value == 2) true
    else !(2 to math.sqrt(value).toInt).exists(value % _ == 0)
}

object S99Int {
  import scala.language.implicitConversions
  implicit def intToS99Int(n: Int): S99Int = new S99Int(n)
}
