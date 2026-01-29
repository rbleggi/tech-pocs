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
}
