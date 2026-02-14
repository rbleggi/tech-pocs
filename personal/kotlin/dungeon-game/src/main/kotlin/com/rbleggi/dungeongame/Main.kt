package com.rbleggi.dungeongame

fun calculateMinimumHP(dungeon: Array<IntArray>): Int {
    val rows = dungeon.size
    val cols = dungeon.firstOrNull()?.size ?: 0
    if (rows == 0 || cols == 0) return 1

    val healthTable = Array(rows) { IntArray(cols) }
    healthTable[rows - 1][cols - 1] = maxOf(1, 1 - dungeon[rows - 1][cols - 1])

    for (i in rows - 2 downTo 0)
        healthTable[i][cols - 1] = maxOf(1, healthTable[i + 1][cols - 1] - dungeon[i][cols - 1])

    for (j in cols - 2 downTo 0)
        healthTable[rows - 1][j] = maxOf(1, healthTable[rows - 1][j + 1] - dungeon[rows - 1][j])

    for (i in rows - 2 downTo 0) {
        for (j in cols - 2 downTo 0) {
            healthTable[i][j] = maxOf(1, minOf(healthTable[i + 1][j], healthTable[i][j + 1]) - dungeon[i][j])
        }
    }

    return healthTable[0][0]
}

fun main() {
    val dungeons = listOf(
        arrayOf(
            intArrayOf(-2, -3, 3),
            intArrayOf(-5, -10, 1),
            intArrayOf(10, 30, -5)
        ),
        arrayOf(
            intArrayOf(-2, -3, 3, 5),
            intArrayOf(-5, -10, 1, 9),
            intArrayOf(10, 30, -5, 7)
        ),
        arrayOf(intArrayOf(0))
    )

    dungeons.forEach { dungeon ->
        println("Minimum initial health needed: ${calculateMinimumHP(dungeon)}")
    }
}
