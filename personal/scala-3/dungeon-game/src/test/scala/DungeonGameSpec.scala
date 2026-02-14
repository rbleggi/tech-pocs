package com.rbleggi.dungeongame

import org.scalatest.funsuite.AnyFunSuite

class DungeonGameSpec extends AnyFunSuite {
  test("calculateMinimumHP should work for 3x3 dungeon") {
    val dungeon = Array(
      Array(-2, -3, 3),
      Array(-5, -10, 1),
      Array(10, 30, -5)
    )
    val result = calculateMinimumHP(dungeon)
    assert(result == 7)
  }

  test("calculateMinimumHP should work for 3x4 dungeon") {
    val dungeon = Array(
      Array(-2, -3, 3, 5),
      Array(-5, -10, 1, 9),
      Array(10, 30, -5, 7)
    )
    val result = calculateMinimumHP(dungeon)
    assert(result > 0)
  }

  test("calculateMinimumHP should work for single cell dungeon") {
    val dungeon = Array(Array(0))
    val result = calculateMinimumHP(dungeon)
    assert(result == 1)
  }

  test("calculateMinimumHP should work for single negative cell") {
    val dungeon = Array(Array(-5))
    val result = calculateMinimumHP(dungeon)
    assert(result == 6)
  }

  test("calculateMinimumHP should work for single positive cell") {
    val dungeon = Array(Array(5))
    val result = calculateMinimumHP(dungeon)
    assert(result == 1)
  }

  test("calculateMinimumHP should return 1 for empty dungeon") {
    val dungeon = Array[Array[Int]]()
    val result = calculateMinimumHP(dungeon)
    assert(result == 1)
  }
}

