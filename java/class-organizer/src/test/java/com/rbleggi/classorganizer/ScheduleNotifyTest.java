package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleNotifyTest {

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
    void testNotifyOnRemove() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter.incrementAndGet());

        var teacher = new Teacher("Bob");
        var session = new ClassSession("1", "Physics", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
        schedule.removeClassSession(session);
        assertEquals(2, counter.get());
    }

    @Test
    void testNoNotifyOnFailedAdd() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter.incrementAndGet());

        var teacher = new Teacher("Charlie");
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 9, 30), LocalDateTime.of(2025, 10, 4, 10, 30), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        assertEquals(1, counter.get());
    }
}
