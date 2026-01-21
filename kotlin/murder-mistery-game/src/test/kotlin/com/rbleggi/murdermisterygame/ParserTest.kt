package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun `parse suspects returns SuspectsCommand`() {
        assertTrue(Parser.parse("suspects") is SuspectsCommand)
    }

    @Test
    fun `parse clues returns CluesCommand`() {
        assertTrue(Parser.parse("clues") is CluesCommand)
    }

    @Test
    fun `parse help returns HelpCommand`() {
        assertTrue(Parser.parse("help") is HelpCommand)
    }

    @Test
    fun `parse exit returns ExitCommand`() {
        assertTrue(Parser.parse("exit") is ExitCommand)
    }
}
