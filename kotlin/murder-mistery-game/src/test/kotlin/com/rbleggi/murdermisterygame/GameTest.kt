package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameTest {
    @Test
    fun `game has default suspects`() {
        val game = Game()
        assertEquals(3, game.suspects.size)
        assertTrue(game.suspects.contains("Bob"))
    }

    @Test
    fun `game has default clues`() {
        val game = Game()
        assertEquals(3, game.clues.size)
    }
}
