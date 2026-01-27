package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    void testFullWorkflow() {
        var teacherA = new Teacher("Alice");
        var teacherB = new Teacher("Bob");
        var schedule = new Schedule();
        var counter = new AtomicInteger(0);

        schedule.registerObserver(teacherA);
        schedule.registerObserver(teacherB);
        schedule.registerObserver(s -> counter.incrementAndGet());

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacherA);
        var session3 = new ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherB);

        assertTrue(schedule.addClassSession(session1));
        assertTrue(schedule.addClassSession(session2));
        assertTrue(schedule.addClassSession(session3));
        assertEquals(3, schedule.getClassSessions().size());

        var overlapSession = new ClassSession("4", "Biology", LocalDateTime.of(2025, 10, 4, 9, 30), LocalDateTime.of(2025, 10, 4, 10, 30), teacherA);
        assertFalse(schedule.addClassSession(overlapSession));
        assertEquals(3, schedule.getClassSessions().size());

        schedule.optimizeSchedule();

        assertTrue(schedule.removeClassSession(session1));
        assertEquals(2, schedule.getClassSessions().size());

        assertEquals(5, counter.get());
    }

    @Test
    void testComplexScenario() {
        var teacherA = new Teacher("Alice");
        var teacherB = new Teacher("Bob");
        var teacherC = new Teacher("Charlie");
        var schedule = new Schedule();

        schedule.registerObserver(teacherA);
        schedule.registerObserver(teacherB);
        schedule.registerObserver(teacherC);

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacherB);
        var session3 = new ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 13, 0), LocalDateTime.of(2025, 10, 4, 14, 0), teacherC);
        var session4 = new ClassSession("4", "Biology", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacherA);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.addClassSession(session3);
        schedule.addClassSession(session4);

        assertEquals(4, schedule.getClassSessions().size());

        schedule.removeObserver(teacherC);
        schedule.optimizeSchedule();

        assertTrue(schedule.getClassSessions().contains(session1));
        assertTrue(schedule.getClassSessions().contains(session2));
        assertTrue(schedule.getClassSessions().contains(session3));
        assertTrue(schedule.getClassSessions().contains(session4));
    }
}
