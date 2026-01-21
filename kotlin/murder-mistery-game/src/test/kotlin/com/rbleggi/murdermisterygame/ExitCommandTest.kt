package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertTrue

class ExitCommandTest {
    @Test
    fun `execute finishes game`() {
        val game = Game()
        val result = ExitCommand.execute(game)
        assertTrue(result.finished)
    }
}
