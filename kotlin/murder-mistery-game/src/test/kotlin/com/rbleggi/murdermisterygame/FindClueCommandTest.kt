package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class FindClueCommandTest {
    @Test
    fun `execute adds valid clue`() {
        val game = Game()
        val result = FindClueCommand("glove").execute(game)
        assertTrue(result.foundClues.contains("glove"))
    }

    @Test
    fun `execute ignores duplicate clue`() {
        val game = Game().withClue("glove")
        val result = FindClueCommand("glove").execute(game)
        assertEquals(1, result.foundClues.count { it == "glove" })
    }

    @Test
    fun `execute ignores invalid clue`() {
        val game = Game()
        val result = FindClueCommand("invalid").execute(game)
        assertTrue(result.foundClues.isEmpty())
    }
}
