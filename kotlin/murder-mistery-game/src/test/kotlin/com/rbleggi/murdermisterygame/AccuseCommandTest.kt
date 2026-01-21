package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

class AccuseCommandTest {
    @Test
    fun `execute sets accused for valid suspect`() {
        val game = Game()
        val result = AccuseCommand("Bob").execute(game)
        assertEquals("Bob", result.accused)
    }

    @Test
    fun `execute finishes game for valid suspect`() {
        val game = Game()
        val result = AccuseCommand("Alice").execute(game)
        assertTrue(result.finished)
    }

    @Test
    fun `execute ignores invalid suspect`() {
        val game = Game()
        val result = AccuseCommand("Invalid").execute(game)
        assertNull(result.accused)
    }
}
