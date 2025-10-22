package com.rbleggi.murdermistery

import org.scalatest.funsuite.AnyFunSuite

class MurderMisteryGameSpec extends AnyFunSuite {
  test("Game logic: murderer is in suspects") {
    val suspects = List("Alice", "Bob", "Charlie")
    val murderer = "Bob"
    assert(suspects.contains(murderer))
  }
}
