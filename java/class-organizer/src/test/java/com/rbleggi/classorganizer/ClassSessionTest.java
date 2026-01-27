package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClassSessionTest {

    @Test
    void testClassSessionCreation() {
        var teacher = new Teacher("Alice");
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        assertNotNull(session);
        assertEquals("1", session.id());
        assertEquals("Math", session.subject());
        assertEquals(teacher, session.teacher());
    }

    @Test
    void testClassSessionTimes() {
        var teacher = new Teacher("Bob");
        var start = LocalDateTime.of(2025, 10, 4, 9, 0);
        var end = LocalDateTime.of(2025, 10, 4, 10, 0);
        var session = new ClassSession("2", "Physics", start, end, teacher);
        assertEquals(start, session.start());
        assertEquals(end, session.end());
    }
}
