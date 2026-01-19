package com.rbleggi.calssorganizer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TeacherTest {
    @Test
    fun `teacher has correct name`() {
        val teacher = Teacher("Alice")
        assertEquals("Alice", teacher.name)
    }

    @Test
    fun `teacher implements Observer`() {
        val teacher = Teacher("Bob")
        assertTrue(teacher is Observer)
    }
}
