package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleRemoveTest {

    @Test
    void testRemoveExistingSession() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        schedule.addClassSession(session);
        assertTrue(schedule.removeClassSession(session));
        assertEquals(0, schedule.getClassSessions().size());
    }

    @Test
    void testRemoveNonExistingSession() {
        var teacher = new Teacher("Bob");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Physics", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        assertFalse(schedule.removeClassSession(session));
    }

    @Test
    void testRemoveOneOfMultiple() {
        var teacher = new Teacher("Charlie");
        var schedule = new Schedule();
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Chemistry", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.removeClassSession(session1);
        assertEquals(1, schedule.getClassSessions().size());
        assertTrue(schedule.getClassSessions().contains(session2));
    }
}
