package com.rbleggi.dontpad

import kotlin.test.*

class NotePadTest {
    @Test
    fun `getAllText returns empty string initially`() {
        val notePad = NotePad("/test")
        assertEquals("", notePad.getAllText())
    }

    @Test
    fun `setAllText stores single line text`() {
        val notePad = NotePad("/test")
        notePad.setAllText("Hello world")
        assertEquals("Hello world", notePad.getAllText())
    }

    @Test
    fun `setAllText stores multi-line text`() {
        val notePad = NotePad("/test")
        notePad.setAllText("Line 1\nLine 2\nLine 3")
        assertEquals("Line 1\nLine 2\nLine 3", notePad.getAllText())
    }

    @Test
    fun `setAllText replaces existing text`() {
        val notePad = NotePad("/test")
        notePad.setAllText("First")
        notePad.setAllText("Second")
        assertEquals("Second", notePad.getAllText())
    }

    @Test
    fun `appendText adds new line to empty notepad`() {
        val notePad = NotePad("/test")
        notePad.appendText("First line")
        assertEquals("First line", notePad.getAllText())
    }

    @Test
    fun `appendText adds multiple lines`() {
        val notePad = NotePad("/test")
        notePad.appendText("Line 1")
        notePad.appendText("Line 2")
        notePad.appendText("Line 3")
        assertEquals("Line 1\nLine 2\nLine 3", notePad.getAllText())
    }

    @Test
    fun `appendText after setAllText appends correctly`() {
        val notePad = NotePad("/test")
        notePad.setAllText("Initial\nText")
        notePad.appendText("Appended")
        assertEquals("Initial\nText\nAppended", notePad.getAllText())
    }

    @Test
    fun `key is stored correctly`() {
        val notePad = NotePad("/mypage")
        assertEquals("/mypage", notePad.key)
    }
}

class LoadNoteCommandTest {
    @Test
    fun `execute loads and displays notepad content`() {
        val notePad = NotePad("/test")
        notePad.setAllText("Test content")
        val command = LoadNoteCommand(notePad)
        command.execute()
    }

    @Test
    fun `execute works with empty notepad`() {
        val notePad = NotePad("/test")
        val command = LoadNoteCommand(notePad)
        command.execute()
    }
}

class AppendNoteCommandTest {
    @Test
    fun `execute appends text to notepad`() {
        val notePad = NotePad("/test")
        val command = AppendNoteCommand(notePad, "New text")
        command.execute()
        assertEquals("New text", notePad.getAllText())
    }

    @Test
    fun `execute appends multiple times`() {
        val notePad = NotePad("/test")
        val command1 = AppendNoteCommand(notePad, "First")
        val command2 = AppendNoteCommand(notePad, "Second")
        command1.execute()
        command2.execute()
        assertEquals("First\nSecond", notePad.getAllText())
    }
}

class NoOpCommandTest {
    @Test
    fun `execute performs no operation`() {
        val command = NoOpCommand()
        command.execute()
    }
}

class CommandPatternIntegrationTest {
    @Test
    fun `commands work together in sequence`() {
        val notePad = NotePad("/integration")

        val append1 = AppendNoteCommand(notePad, "Hello")
        append1.execute()

        val append2 = AppendNoteCommand(notePad, "World")
        append2.execute()

        assertEquals("Hello\nWorld", notePad.getAllText())

        val load = LoadNoteCommand(notePad)
        load.execute()

        val noop = NoOpCommand()
        noop.execute()

        assertEquals("Hello\nWorld", notePad.getAllText())
    }

    @Test
    fun `multiple notepads are independent`() {
        val notePad1 = NotePad("/page1")
        val notePad2 = NotePad("/page2")

        AppendNoteCommand(notePad1, "Content 1").execute()
        AppendNoteCommand(notePad2, "Content 2").execute()

        assertEquals("Content 1", notePad1.getAllText())
        assertEquals("Content 2", notePad2.getAllText())
    }
}
