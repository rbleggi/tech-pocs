package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObserverPatternTest {

    @Test
    void testObserverPattern() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        Observer observer = s -> counter.incrementAndGet();

        schedule.registerObserver(observer);

        var teacher = new Teacher("Alice");
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);

        assertEquals(1, counter.get());
    }

    @Test
    void testMultipleNotifications() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        Observer observer = s -> counter.incrementAndGet();

        schedule.registerObserver(observer);

        var teacher = new Teacher("Bob");
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);

        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.optimizeSchedule();

        assertEquals(3, counter.get());
    }

    @Test
    void testObserverRemoval() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        Observer observer = s -> counter.incrementAndGet();

        schedule.registerObserver(observer);

        var teacher = new Teacher("Charlie");
        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session1);

        schedule.removeObserver(observer);

        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 11, 0), LocalDateTime.of(2025, 10, 4, 12, 0), teacher);
        schedule.addClassSession(session2);

        assertEquals(1, counter.get());
    }
}
