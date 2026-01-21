package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CluesCommandTest {
    @Test
    fun `execute returns same game`() {
        val game = Game()
        val result = CluesCommand.execute(game)
        assertEquals(game.clues, result.clues)
    }

    @Test
    fun `execute does not modify game`() {
        val game = Game()
        val result = CluesCommand.execute(game)
        assertFalse(result.finished)
    }
}
