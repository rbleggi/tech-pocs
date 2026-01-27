package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClassSessionEqualityTest {

    @Test
    void testSameSessionEquals() {
        var teacher = new Teacher("Alice");
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        assertEquals(session1, session2);
    }

    @Test
    void testDifferentIdNotEquals() {
        var teacher = new Teacher("Bob");
        var session1 = new ClassSession("1", "Physics", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        assertNotEquals(session1, session2);
    }

    @Test
    void testDifferentSubjectNotEquals() {
        var teacher = new Teacher("Charlie");
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("1", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        assertNotEquals(session1, session2);
    }

    @Test
    void testDifferentTimeNotEquals() {
        var teacher = new Teacher("Dave");
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacher);
        assertNotEquals(session1, session2);
    }
}
