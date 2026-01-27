package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleImmutabilityTest {

    @Test
    void testGetClassSessionsReturnsCopy() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        schedule.addClassSession(session);
        var sessions1 = schedule.getClassSessions();
        var sessions2 = schedule.getClassSessions();

        assertEquals(sessions1, sessions2);
        assertEquals(1, sessions1.size());
    }

    @Test
    void testSessionListImmutable() {
        var teacher = new Teacher("Bob");
        var schedule = new Schedule();
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);

        schedule.addClassSession(session1);
        var sessions = schedule.getClassSessions();

        schedule.addClassSession(session2);

        assertEquals(1, sessions.size());
        assertEquals(2, schedule.getClassSessions().size());
    }
}
