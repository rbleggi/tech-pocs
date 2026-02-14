package com.rbleggi.dungeongame

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class DungeonGameTest {
    @Test
    fun `calculateMinimumHP should work for 3x3 dungeon`() {
        val dungeon = arrayOf(
            intArrayOf(-2, -3, 3),
            intArrayOf(-5, -10, 1),
            intArrayOf(10, 30, -5)
        )
        val result = calculateMinimumHP(dungeon)
        assertEquals(7, result)
    }

    @Test
    fun `calculateMinimumHP should work for 3x4 dungeon`() {
        val dungeon = arrayOf(
            intArrayOf(-2, -3, 3, 5),
            intArrayOf(-5, -10, 1, 9),
            intArrayOf(10, 30, -5, 7)
        )
        val result = calculateMinimumHP(dungeon)
        assertTrue(result > 0)
    }

    @Test
    fun `calculateMinimumHP should work for single cell dungeon`() {
        val dungeon = arrayOf(intArrayOf(0))
        val result = calculateMinimumHP(dungeon)
        assertEquals(1, result)
    }

    @Test
    fun `calculateMinimumHP should work for single negative cell`() {
        val dungeon = arrayOf(intArrayOf(-5))
        val result = calculateMinimumHP(dungeon)
        assertEquals(6, result)
    }

    @Test
    fun `calculateMinimumHP should work for single positive cell`() {
        val dungeon = arrayOf(intArrayOf(5))
        val result = calculateMinimumHP(dungeon)
        assertEquals(1, result)
    }

    @Test
    fun `calculateMinimumHP should return 1 for empty dungeon`() {
        val dungeon = emptyArray<IntArray>()
        val result = calculateMinimumHP(dungeon)
        assertEquals(1, result)
    }
}
