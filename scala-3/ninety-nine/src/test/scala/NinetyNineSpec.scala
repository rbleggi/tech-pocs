package com.rbleggi.ninetynine

import org.scalatest.funsuite.AnyFunSuite

class NinetyNineSpec extends AnyFunSuite {
  test("P01.last returns the last element of a non-empty list") {
    assert(P01.last(List(1, 2, 3, 4)) == 4)
    assert(P01.last(List("a", "b", "c")) == "c")
  }

  test("P01.last throws NoSuchElementException for empty list") {
    assertThrows[NoSuchElementException] {
      P01.last(Nil)
    }
  }
}
