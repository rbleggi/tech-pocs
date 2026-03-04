package com.rbleggi.ninetynine

object P46 {
  def and(a: Boolean, b: Boolean): Boolean = a && b
  def or(a: Boolean, b: Boolean): Boolean = a || b
  def nand(a: Boolean, b: Boolean): Boolean = !(a && b)
  def nor(a: Boolean, b: Boolean): Boolean = !(a || b)
  def xor(a: Boolean, b: Boolean): Boolean = a != b
  def impl(a: Boolean, b: Boolean): Boolean = !a || b
  def equ(a: Boolean, b: Boolean): Boolean = a == b

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

@main def mainP46(): Unit = {
  println("Truth table for and(a, or(a, b)):")
  P46.table2((a: Boolean, b: Boolean) => P46.and(a, P46.or(a, b)))

  println("\nTruth table for xor(a, b):")
  P46.table2(P46.xor)
}

