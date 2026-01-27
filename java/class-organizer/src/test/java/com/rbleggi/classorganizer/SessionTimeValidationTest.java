package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionTimeValidationTest {

    @Test
    void testStartBeforeEnd() {
        var teacher = new Teacher("Alice");
        var start = LocalDateTime.of(2025, 10, 4, 9, 0);
        var end = LocalDateTime.of(2025, 10, 4, 10, 0);
        var session = new ClassSession("1", "Math", start, end, teacher);

        assertNotNull(session);
        assertEquals(start, session.start());
        assertEquals(end, session.end());
    }

    @Test
    void testSingleMinuteDuration() {
        var teacher = new Teacher("Bob");
        var start = LocalDateTime.of(2025, 10, 4, 9, 0);
        var end = LocalDateTime.of(2025, 10, 4, 9, 1);
        var session = new ClassSession("2", "Physics", start, end, teacher);

        assertEquals(start, session.start());
        assertEquals(end, session.end());
    }

    @Test
    void testLongDuration() {
        var teacher = new Teacher("Charlie");
        var start = LocalDateTime.of(2025, 10, 4, 9, 0);
        var end = LocalDateTime.of(2025, 10, 4, 18, 0);
        var session = new ClassSession("3", "Chemistry", start, end, teacher);

        assertEquals(start, session.start());
        assertEquals(end, session.end());
    }
}
