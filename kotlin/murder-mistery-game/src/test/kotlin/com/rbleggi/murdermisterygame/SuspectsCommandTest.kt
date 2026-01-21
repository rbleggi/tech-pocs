package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class SuspectsCommandTest {
    @Test
    fun `execute returns same game`() {
        val game = Game()
        val result = SuspectsCommand.execute(game)
        assertEquals(game.suspects, result.suspects)
    }

    @Test
    fun `execute does not modify game`() {
        val game = Game()
        val result = SuspectsCommand.execute(game)
        assertFalse(result.finished)
    }
}
