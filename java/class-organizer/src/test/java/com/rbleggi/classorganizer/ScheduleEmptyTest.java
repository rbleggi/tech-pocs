package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleEmptyTest {

    @Test
    void testEmptySchedule() {
        var schedule = new Schedule();
        assertTrue(schedule.getClassSessions().isEmpty());
    }

    @Test
    void testEmptyScheduleSize() {
        var schedule = new Schedule();
        assertEquals(0, schedule.getClassSessions().size());
    }

    @Test
    void testEmptyScheduleNotNull() {
        var schedule = new Schedule();
        assertNotNull(schedule.getClassSessions());
    }
}
