package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleAddTest {

    @Test
    void testAddSingleSession() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        assertTrue(schedule.addClassSession(session));
        assertEquals(1, schedule.getClassSessions().size());
    }

    @Test
    void testAddMultipleSessions() {
        var teacher = new Teacher("Bob");
        var schedule = new Schedule();
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        assertEquals(2, schedule.getClassSessions().size());
    }

    @Test
    void testGetClassSessionsReturnsImmutable() {
        var teacher = new Teacher("Charlie");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        schedule.addClassSession(session);
        var sessions = schedule.getClassSessions();
        assertEquals(1, sessions.size());
    }
}
