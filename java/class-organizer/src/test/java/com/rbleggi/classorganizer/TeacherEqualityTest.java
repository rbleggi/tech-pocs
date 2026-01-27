package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherEqualityTest {

    @Test
    void testSameNameEquals() {
        var teacher1 = new Teacher("Alice");
        var teacher2 = new Teacher("Alice");
        assertEquals(teacher1, teacher2);
    }

    @Test
    void testDifferentNameNotEquals() {
        var teacher1 = new Teacher("Alice");
        var teacher2 = new Teacher("Bob");
        assertNotEquals(teacher1, teacher2);
    }

    @Test
    void testSameInstanceEquals() {
        var teacher = new Teacher("Charlie");
        assertEquals(teacher, teacher);
    }

    @Test
    void testNullNotEquals() {
        var teacher = new Teacher("Dave");
        assertNotEquals(teacher, null);
    }

    @Test
    void testDifferentClassNotEquals() {
        var teacher = new Teacher("Eve");
        assertNotEquals(teacher, "Eve");
    }
}
