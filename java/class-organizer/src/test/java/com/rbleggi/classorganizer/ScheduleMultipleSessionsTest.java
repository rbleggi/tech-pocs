package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleMultipleSessionsTest {

    @Test
    void testAddThreeSessions() {
        var teacherA = new Teacher("Alice");
        var teacherB = new Teacher("Bob");
        var schedule = new Schedule();

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacherA);
        var session3 = new ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherB);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.addClassSession(session3);

        assertEquals(3, schedule.getClassSessions().size());
    }

    @Test
    void testRemoveFromMultiple() {
        var teacher = new Teacher("Charlie");
        var schedule = new Schedule();

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);
        var session3 = new ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 13, 0), LocalDateTime.of(2025, 10, 4, 14, 0), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.addClassSession(session3);
        schedule.removeClassSession(session2);

        assertEquals(2, schedule.getClassSessions().size());
    }
}
