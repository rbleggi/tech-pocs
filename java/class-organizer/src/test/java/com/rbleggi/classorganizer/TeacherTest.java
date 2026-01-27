package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeacherTest {

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
    void testTeacherUpdate() {
        var teacher = new Teacher("Dave");
        var schedule = new Schedule();
        var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacher);
        schedule.addClassSession(session);
        teacher.update(schedule);
        assertTrue(schedule.getClassSessions().contains(session));
    }
}
