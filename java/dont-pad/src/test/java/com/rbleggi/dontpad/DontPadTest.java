package com.rbleggi.dontpad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DontPadTest {

    @Test
    void testNotePadCreation() {
        var notePad = new NotePad("/mypage");
        assertEquals("/mypage", notePad.getKey());
        assertEquals("", notePad.getAllText());
    }

    @Test
    void testAppendText() {
        var notePad = new NotePad("/test");
        notePad.appendText("Hello");
        assertEquals("Hello", notePad.getAllText());
    }

    @Test
    void testAppendMultipleTexts() {
        var notePad = new NotePad("/test");
        notePad.appendText("Hello");
        notePad.appendText("World");
        assertEquals("Hello\nWorld", notePad.getAllText());
    }

    @Test
    void testSetAllText() {
        var notePad = new NotePad("/test");
        notePad.setAllText("Line1\nLine2\nLine3");
        assertEquals("Line1\nLine2\nLine3", notePad.getAllText());
    }

    @Test
    void testSetAllTextOverwrites() {
        var notePad = new NotePad("/test");
        notePad.appendText("Old text");
        notePad.setAllText("New text");
        assertEquals("New text", notePad.getAllText());
    }

    @Test
    void testAppendNoteCommand() {
        var notePad = new NotePad("/test");
        var command = new AppendNoteCommand(notePad, "Hello");
        command.execute();
        assertEquals("Hello", notePad.getAllText());
    }

    @Test
    void testLoadNoteCommand() {
        var notePad = new NotePad("/test");
        notePad.appendText("Some content");
        var command = new LoadNoteCommand(notePad);
        command.execute();
    }

    @Test
    void testNoOpCommand() {
        var command = new NoOpCommand();
        command.execute();
    }

    @Test
    void testCommandInterface() {
        var notePad = new NotePad("/test");
        Command append = new AppendNoteCommand(notePad, "text");
        Command load = new LoadNoteCommand(notePad);
        Command noop = new NoOpCommand();
        append.execute();
        load.execute();
        noop.execute();
        assertEquals("text", notePad.getAllText());
    }

    @Test
    void testMultipleAppendCommands() {
        var notePad = new NotePad("/test");
        new AppendNoteCommand(notePad, "First").execute();
        new AppendNoteCommand(notePad, "Second").execute();
        new AppendNoteCommand(notePad, "Third").execute();
        assertEquals("First\nSecond\nThird", notePad.getAllText());
    }

    @Test
    void testEmptyNotePad() {
        var notePad = new NotePad("/empty");
        assertEquals("", notePad.getAllText());
    }

    @Test
    void testNotePadKey() {
        var notePad = new NotePad("/my/custom/path");
        assertEquals("/my/custom/path", notePad.getKey());
    }

    @Test
    void testSetAllTextWithMultipleLines() {
        var notePad = new NotePad("/test");
        notePad.setAllText("A\nB\nC\nD");
        assertEquals("A\nB\nC\nD", notePad.getAllText());
    }

    @Test
    void testAppendAfterSetAll() {
        var notePad = new NotePad("/test");
        notePad.setAllText("First");
        notePad.appendText("Second");
        assertEquals("First\nSecond", notePad.getAllText());
    }

    @Test
    void testSetAllTextCommand() {
        var notePad = new NotePad("/test");
        notePad.appendText("Old");
        new SetAllTextCommand(notePad, "New content").execute();
        assertEquals("New content", notePad.getAllText());
    }

    @Test
    void testClearNoteCommand() {
        var notePad = new NotePad("/test");
        notePad.appendText("Some text");
        new ClearNoteCommand(notePad).execute();
        assertEquals("", notePad.getAllText());
    }

    @Test
    void testFullWorkflow() {
        var notePad = new NotePad("/mypage");

        new AppendNoteCommand(notePad, "Hello, world!").execute();
        assertEquals("Hello, world!", notePad.getAllText());

        new AppendNoteCommand(notePad, "Second line").execute();
        assertEquals("Hello, world!\nSecond line", notePad.getAllText());

        new SetAllTextCommand(notePad, "Replaced content").execute();
        assertEquals("Replaced content", notePad.getAllText());

        new LoadNoteCommand(notePad).execute();
        new NoOpCommand().execute();
        assertEquals("Replaced content", notePad.getAllText());

        new ClearNoteCommand(notePad).execute();
        assertEquals("", notePad.getAllText());
    }
}
