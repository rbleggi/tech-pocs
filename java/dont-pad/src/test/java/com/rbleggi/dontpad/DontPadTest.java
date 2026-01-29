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
}
