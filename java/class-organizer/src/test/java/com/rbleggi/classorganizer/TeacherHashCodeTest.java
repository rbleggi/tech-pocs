package com.rbleggi.classorganizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TeacherHashCodeTest {

    @Test
    void testSameNameSameHashCode() {
        var teacher1 = new Teacher("Alice");
        var teacher2 = new Teacher("Alice");
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void testDifferentNameDifferentHashCode() {
        var teacher1 = new Teacher("Alice");
        var teacher2 = new Teacher("Bob");
        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void testConsistentHashCode() {
        var teacher = new Teacher("Charlie");
        var hash1 = teacher.hashCode();
        var hash2 = teacher.hashCode();
        assertEquals(hash1, hash2);
    }
}
