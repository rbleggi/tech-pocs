package com.rbleggi.dungeongame

import scala.util.CommandLineParser

/**
 * Strategy interface for calculating minimum health points required to complete a dungeon.
 * This trait follows the Strategy design pattern, allowing different algorithms
 * to be implemented for health calculation.
 */
trait HealthCalculationStrategy {
  def calculateMinimumHP(dungeon: Array[Array[Int]]): Int

  def explainSteps(dungeon: Array[Array[Int]]): Unit
}

/**
 * Implementation of the health calculation strategy using dynamic programming
 * to find the optimal path requiring the minimum initial health.
 *
 * The algorithm works backward from the princess's cell to the knight's starting position,
 * calculating the minimum health needed at each cell to survive.
 */
class OptimalPathHealthStrategy extends HealthCalculationStrategy {
  override def calculateMinimumHP(dungeon: Array[Array[Int]]): Int = {
    // Handle edge case of empty dungeon
    if (dungeon.isEmpty || dungeon(0).isEmpty) return 1

    val healthTable = initializeHealthTable(dungeon)
    fillHealthTable(dungeon, healthTable)
    // The answer is in the top-left cell after filling the table
    healthTable(0)(0)
  }

  override def explainSteps(dungeon: Array[Array[Int]]): Unit = {
    // Handle edge case of empty dungeon
    if (dungeon.isEmpty || dungeon(0).isEmpty) {
      println("Dungeon is empty. Minimum health required is 1.")
      return
    }

    // Calculate the minimum health needed and trace the optimal path
    val healthTable = initializeHealthTable(dungeon)
    fillHealthTable(dungeon, healthTable)
    val path = traceOptimalPath(healthTable)

    // Print the results with explanation
    printDungeonInput(dungeon)
    printExplanation(healthTable(0)(0), path)
  }

  /**
   * Initialize the health table with the princess's cell.
   * At the princess's cell, we need at least 1 HP after accounting for the cell's value.
   *
   * @param dungeon The dungeon grid
   * @return An initialized health table with the princess's cell filled
   */
  private def initializeHealthTable(dungeon: Array[Array[Int]]): Array[Array[Int]] = {
    val rows = dungeon.length
    val cols = dungeon(0).length
    val healthTable = Array.ofDim[Int](rows, cols)
    // For the bottom-right cell (princess's location), we need at least 1 HP after accounting for the cell's value
    healthTable(rows - 1)(cols - 1) = Math.max(1, 1 - dungeon(rows - 1)(cols - 1))
    healthTable
  }

  /**
   * Fill the health table using dynamic programming, working backwards from the princess's cell.
   * For each cell, we calculate the minimum health needed to survive from that cell onwards.
   *
   * @param dungeon The dungeon grid with positive (healing) and negative (damage) values
   * @param healthTable The table to fill with minimum health needed at each cell
   */
  private def fillHealthTable(dungeon: Array[Array[Int]], healthTable: Array[Array[Int]]): Unit = {
    val rows = dungeon.length
    val cols = dungeon(0).length

    // Fill the bottom row (can only move right)
    (rows - 2 to 0 by -1).foreach { i =>
      healthTable(i)(cols - 1) = Math.max(1, healthTable(i + 1)(cols - 1) - dungeon(i)(cols - 1))
    }

    // Fill the rightmost column (can only move down)
    (cols - 2 to 0 by -1).foreach { j =>
      healthTable(rows - 1)(j) = Math.max(1, healthTable(rows - 1)(j + 1) - dungeon(rows - 1)(j))
    }

    // Fill the rest of the table, working backwards
    // For each cell, choose the path (right or down) that requires less health
    for {
      i <- (rows - 2 to 0 by -1)
      j <- (cols - 2 to 0 by -1)
    } {
      val minHealthNeeded = Math.min(healthTable(i + 1)(j), healthTable(i)(j + 1))
      healthTable(i)(j) = Math.max(1, minHealthNeeded - dungeon(i)(j))
    }
  }

  /**
   * Trace the optimal path from the starting position to the princess.
   * At each step, choose the direction (down or right) that requires less initial health.
   *
   * @param healthTable The filled health table
   * @return A list of directions (DOWN or RIGHT) forming the optimal path
   */
  private def traceOptimalPath(healthTable: Array[Array[Int]]): List[String] = {
    val rows = healthTable.length
    val cols = healthTable(0).length
    var r = 0
    var c = 0
    val path = scala.collection.mutable.ListBuffer[String]()

    // Continue until we reach the princess's cell (bottom-right)
    while (r < rows - 1 || c < cols - 1) {
      if (c == cols - 1 || (r < rows - 1 && healthTable(r + 1)(c) <= healthTable(r)(c + 1))) {
        // If we're at the right edge or going down requires less health, go down
        path.append("DOWN")
        r += 1
      } else {
        // Otherwise, go right
        path.append("RIGHT")
        c += 1
      }
    }

    path.toList
  }

  /**
   * Print the dungeon layout for visualization
   *
   * @param dungeon The dungeon grid
   */
  private def printDungeonInput(dungeon: Array[Array[Int]]): Unit = {
    println("Dungeon:")
    dungeon.foreach(row => println(row.mkString("[", ", ", "]")))
  }

  /**
   * Print the explanation of the result including the minimum health required
   * and the optimal path to follow
   *
   * @param minHealth Minimum initial health required
   * @param path List of directions forming the optimal path
   */
  private def printExplanation(minHealth: Int, path: List[String]): Unit = {
    println(s"Explanation: The initial health of the knight must be at least $minHealth " +
      s"if he follows the optimal path: ${path.mkString(" -> ")}.")
  }
}

/**
 * Main game class that uses the Strategy pattern to calculate minimum health.
 * This class delegates the calculation to the provided strategy implementation.
 *
 * @param strategy The health calculation strategy to use
 */
class DungeonGame(strategy: HealthCalculationStrategy) {
  def calculateMinimumHP(dungeon: Array[Array[Int]]): Int = strategy.calculateMinimumHP(dungeon)

  def explainSteps(dungeon: Array[Array[Int]]): Unit = strategy.explainSteps(dungeon)
}

// Command-line parser for handling array inputs
given CommandLineParser.FromString[Array[String]] with
  def fromString(s: String): Array[String] = s.split(",")

/**
 * Main entry point of the application.
 * Demonstrates the dungeon game with two example dungeons:
 * 1. A 3x3 dungeon with negative and positive values
 * 2. A simple 1x1 dungeon with a zero value
 */
@main def main(): Unit = {
  // Example 1: A 3x3 dungeon with obstacles and bonuses
  val dungeon1 = Array(
    Array(-2, -3, 3),
    Array(-5, -10, 1),
    Array(10, 30, -5)
  )

  // Example 2: A trivial 1x1 dungeon
  val dungeon2 = Array(Array(0))

  // Create a game instance with the optimal path strategy
  val game = new DungeonGame(new OptimalPathHealthStrategy)

  // Calculate and explain the minimum health required for each dungeon
  println(s"\nMinimum initial health needed: ${game.calculateMinimumHP(dungeon1)}")
  game.explainSteps(dungeon1)

  println(s"\nMinimum initial health needed: ${game.calculateMinimumHP(dungeon2)}")
  game.explainSteps(dungeon2)
}
