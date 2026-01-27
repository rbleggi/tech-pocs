package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleConflictTest {

    @Test
    void testOverlappingSessionsRejected() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 9, 30), LocalDateTime.of(2025, 10, 4, 10, 30), teacher);

        assertTrue(schedule.addClassSession(session1));
        assertFalse(schedule.addClassSession(session2));
    }

    @Test
    void testDifferentTeachersCanOverlap() {
        var teacherA = new Teacher("Alice");
        var teacherB = new Teacher("Bob");
        var schedule = new Schedule();
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 9, 30), LocalDateTime.of(2025, 10, 4, 10, 30), teacherB);

        assertTrue(schedule.addClassSession(session1));
        assertTrue(schedule.addClassSession(session2));
    }

    @Test
    void testAdjacentSessionsAllowed() {
        var teacher = new Teacher("Charlie");
        var schedule = new Schedule();
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacher);

        assertTrue(schedule.addClassSession(session1));
        assertTrue(schedule.addClassSession(session2));
    }
}
