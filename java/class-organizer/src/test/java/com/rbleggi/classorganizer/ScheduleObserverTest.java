package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleObserverTest {

    @Test
    void testObserverRegistration() {
        var teacher = new Teacher("Alice");
        var schedule = new Schedule();
        schedule.registerObserver(teacher);

        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
    }

    @Test
    void testObserverRemoval() {
        var teacher = new Teacher("Bob");
        var schedule = new Schedule();
        schedule.registerObserver(teacher);
        schedule.removeObserver(teacher);

        var session = new ClassSession("1", "Physics", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
    }

    @Test
    void testMultipleObservers() {
        var teacherA = new Teacher("Alice");
        var teacherB = new Teacher("Bob");
        var schedule = new Schedule();
        schedule.registerObserver(teacherA);
        schedule.registerObserver(teacherB);

        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA);
        schedule.addClassSession(session);
    }

    @Test
    void testCustomObserver() {
        var schedule = new Schedule();
        var counter = new AtomicInteger(0);
        Observer customObserver = s -> counter.incrementAndGet();
        schedule.registerObserver(customObserver);

        var teacher = new Teacher("Charlie");
        var session = new ClassSession("1", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
        assertEquals(1, counter.get());
    }
}
