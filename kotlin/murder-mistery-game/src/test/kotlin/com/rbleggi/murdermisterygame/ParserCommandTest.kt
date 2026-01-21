package com.rbleggi.murdermisterygame

import kotlin.test.Test
import kotlin.test.assertTrue

class ParserCommandTest {
    @Test
    fun `parse find returns FindClueCommand`() {
        assertTrue(Parser.parse("find glove") is FindClueCommand)
    }

    @Test
    fun `parse accuse returns AccuseCommand`() {
        assertTrue(Parser.parse("accuse Bob") is AccuseCommand)
    }

    @Test
    fun `parse unknown returns UnknownCommand`() {
        assertTrue(Parser.parse("invalid") is UnknownCommand)
    }
}
