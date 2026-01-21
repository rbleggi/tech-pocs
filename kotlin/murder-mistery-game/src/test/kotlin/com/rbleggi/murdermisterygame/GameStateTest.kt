package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class GameStateTest {
    @Test
    fun `withClue adds clue to found`() {
        val game = Game().withClue("glove")
        assertTrue(game.foundClues.contains("glove"))
    }

    @Test
    fun `withAccused sets accused and finishes`() {
        val game = Game().withAccused("Bob")
        assertEquals("Bob", game.accused)
        assertTrue(game.finished)
    }

    @Test
    fun `finish sets finished true`() {
        val game = Game().finish()
        assertTrue(game.finished)
    }
}
