package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class OtherCommandsTest {
    @Test
    fun `HelpCommand does not modify game`() {
        val game = Game()
        val result = HelpCommand.execute(game)
        assertFalse(result.finished)
    }

    @Test
    fun `UnknownCommand does not modify game`() {
        val game = Game()
        val result = UnknownCommand.execute(game)
        assertFalse(result.finished)
        assertEquals(game.foundClues, result.foundClues)
    }
}
