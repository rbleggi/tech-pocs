package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleOptimizeTest {

    @Test
    void testOptimizeNotifiesObservers() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter.incrementAndGet());

        schedule.optimizeSchedule();
        assertEquals(1, counter.get());
    }

    @Test
    void testOptimizeWithMultipleObservers() {
        var counter1 = new AtomicInteger(0);
        var counter2 = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter1.incrementAndGet());
        schedule.registerObserver(s -> counter2.incrementAndGet());

        schedule.optimizeSchedule();
        assertEquals(1, counter1.get());
        assertEquals(1, counter2.get());
    }

    @Test
    void testOptimizeMultipleTimes() {
        var counter = new AtomicInteger(0);
        var schedule = new Schedule();
        schedule.registerObserver(s -> counter.incrementAndGet());

        schedule.optimizeSchedule();
        schedule.optimizeSchedule();
        schedule.optimizeSchedule();
        assertEquals(3, counter.get());
    }
}
