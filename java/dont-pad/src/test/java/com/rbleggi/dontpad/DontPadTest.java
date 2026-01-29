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
}
