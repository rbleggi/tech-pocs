package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleGettersTest {

    @Test
    void testGetClassSessions() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        schedule.addClassSession(session);
        var sessions = schedule.getClassSessions();

        assertNotNull(sessions);
        assertEquals(1, sessions.size());
        assertTrue(sessions.contains(session));
    }

    @Test
    void testGetClassSessionsEmpty() {
        var schedule = new Schedule();
        var sessions = schedule.getClassSessions();

        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testGetClassSessionsOrder() {
        var teacher = new Teacher("Bob");
        var schedule = new Schedule();

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);
        var session3 = new ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 13, 0), LocalDateTime.of(2025, 10, 4, 14, 0), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.addClassSession(session3);

        var sessions = schedule.getClassSessions();
        assertEquals(3, sessions.size());
    }
}
