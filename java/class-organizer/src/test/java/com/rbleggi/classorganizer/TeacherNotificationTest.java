package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TeacherNotificationTest {

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
    void testTeacherNotNotified() {
        var originalOut = System.out;
        var outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            var teacherA = new Teacher("Alice");
            var teacherB = new Teacher("Bob");
            var schedule = new Schedule();
            schedule.registerObserver(teacherA);

            var session = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherB);
            schedule.addClassSession(session);

            var output = outputStream.toString();
            assertTrue(output.contains("Teacher Alice notified"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
