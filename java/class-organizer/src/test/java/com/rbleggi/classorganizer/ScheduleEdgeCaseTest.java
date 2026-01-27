package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleEdgeCaseTest {

    @Test
    void testSameStartAndEndTime() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 9, 0), teacher);

        assertTrue(schedule.addClassSession(session));
        assertEquals(1, schedule.getClassSessions().size());
    }

    @Test
    void testMultipleSessionsSameTeacher() {
        var teacher = new Teacher("Bob");
        var schedule = new Schedule();

        for (int i = 0; i < 5; i++) {
            var start = LocalDateTime.of(2025, 10, 4, 9 + i * 2, 0);
            var end = LocalDateTime.of(2025, 10, 4, 10 + i * 2, 0);
            var session = new ClassSession(String.valueOf(i), "Subject" + i, start, end, teacher);
            schedule.addClassSession(session);
        }

        assertEquals(5, schedule.getClassSessions().size());
    }

    @Test
    void testRemoveAllSessions() {
        var teacher = new Teacher("Charlie");
        var schedule = new Schedule();

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.removeClassSession(session1);
        schedule.removeClassSession(session2);

        assertTrue(schedule.getClassSessions().isEmpty());
    }
}
