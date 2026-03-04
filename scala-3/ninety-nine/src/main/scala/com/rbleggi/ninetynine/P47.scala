package com.rbleggi.ninetynine

object P47 {
  def not(a: Boolean): Boolean = !a

  class BoolOps(val a: Boolean) extends AnyVal {
    def and(b: Boolean): Boolean = a && b
    def or(b: Boolean): Boolean = a || b
    def nand(b: Boolean): Boolean = !(a && b)
    def nor(b: Boolean): Boolean = !(a || b)
    def xor(b: Boolean): Boolean = a != b
    def impl(b: Boolean): Boolean = !a || b
    def equ(b: Boolean): Boolean = a == b
  }

  implicit def toBoolOps(a: Boolean): BoolOps = new BoolOps(a)

  def table2(f: (Boolean, Boolean) => Boolean): Unit = {
    println("A     B     result")
    for {
      a <- List(true, false)
      b <- List(true, false)
    } {
      val res = f(a, b)
      println(f"$a%-5s $b%-5s $res%-5s")
    }
  }
}

@main def mainP47(): Unit = {
  import P47._
  println("Truth table for a and (a or not(b)):")
  table2((a: Boolean, b: Boolean) => a and (a or not(b)))
}

