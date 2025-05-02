package com.rbleggi.dungeongame

import scala.util.CommandLineParser

@main def main(): Unit = {
  val dungeons = Seq(
    Array(
      Array(-2, -3, 3),
      Array(-5, -10, 1),
      Array(10, 30, -5)
    ), Array(
      Array(-2, -3, 3, 5),
      Array(-5, -10, 1, 9),
      Array(10, 30, -5, 7)
    ),
    Array(Array(0))
  )

  def calculateMinimumHP(dungeon: Array[Array[Int]]): Int = {
    val rows = dungeon.length
    val cols = dungeon.headOption.map(_.length).getOrElse(0)
    if (rows == 0 || cols == 0) return 1

    val healthTable = Array.fill(rows, cols)(0)
    healthTable(rows - 1)(cols - 1) = Math.max(1, 1 - dungeon(rows - 1)(cols - 1))

    for (i <- (rows - 2 to 0 by -1))
      healthTable(i)(cols - 1) = Math.max(1, healthTable(i + 1)(cols - 1) - dungeon(i)(cols - 1))

    for (j <- (cols - 2 to 0 by -1))
      healthTable(rows - 1)(j) = Math.max(1, healthTable(rows - 1)(j + 1) - dungeon(rows - 1)(j))

    for {
      i <- (rows - 2 to 0 by -1)
      j <- (cols - 2 to 0 by -1)
    } healthTable(i)(j) = Math.max(1, Math.min(healthTable(i + 1)(j), healthTable(i)(j + 1)) - dungeon(i)(j))

    healthTable(0)(0)
  }

  dungeons.foreach { dungeon =>
    println(s"Minimum initial health needed: ${calculateMinimumHP(dungeon)}")
  }
}
