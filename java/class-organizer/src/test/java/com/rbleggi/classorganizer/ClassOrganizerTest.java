package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ClassOrganizerTest {

    @Test
    void testTeacherName() {
        var teacher = new Teacher("Alice");
        assertEquals("Alice", teacher.getName());
    }

    @Test
    void testTeacherEquality() {
        var teacher1 = new Teacher("Bob");
        var teacher2 = new Teacher("Bob");
        assertEquals(teacher1, teacher2);
    }

    @Test
    void testTeacherHashCode() {
        var teacher1 = new Teacher("Charlie");
        var teacher2 = new Teacher("Charlie");
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void testClassSessionCreation() {
        var teacher = new Teacher("Alice");
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        assertNotNull(session);
        assertEquals("1", session.id());
        assertEquals("Math", session.subject());
        assertEquals(teacher, session.teacher());
    }

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
    void testRemoveExistingSession() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);

        schedule.addClassSession(session);
        assertTrue(schedule.removeClassSession(session));
        assertEquals(0, schedule.getClassSessions().size());
    }

    @Test
    void testObserverRegistration() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        schedule.registerObserver(teacher);

        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
    }

    @Test
    void testNotifyOnAdd() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter.incrementAndGet());

        var teacher = new Teacher("Alice");
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
        assertEquals(1, counter.get());
    }

    @Test
    void testOptimizeNotifiesObservers() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter.incrementAndGet());

        schedule.optimizeSchedule();
        assertEquals(1, counter.get());
    }

    @Test
    void testEmptySchedule() {
        var schedule = new Schedule();
        assertTrue(schedule.getClassSessions().isEmpty());
    }

    @Test
    void testSameNameEquals() {
        var teacher1 = new Teacher("Alice");
        var teacher2 = new Teacher("Alice");
        assertEquals(teacher1, teacher2);
    }

    @Test
    void testGetClassSessionsReturnsImmutable() {
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

    @Test
    void testTeacherNotified() {
        var originalOut = System.out;
        var outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            var teacher = new Teacher("Alice");
            var schedule = new Schedule();
            schedule.registerObserver(teacher);

            var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
            schedule.addClassSession(session);

            var output = outputStream.toString();
            assertTrue(output.contains("Teacher Alice notified"));
        } finally {
            System.setOut(originalOut);
        }
    }

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
}
