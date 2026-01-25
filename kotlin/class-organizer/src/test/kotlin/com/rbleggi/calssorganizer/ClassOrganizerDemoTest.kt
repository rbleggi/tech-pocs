package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClassOrganizerDemoTest {
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

    @Test
    fun `schedule implements Subject`() {
        val schedule = Schedule()
        assertTrue(schedule is Subject)
    }

    @Test
    fun `registerObserver adds observer`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        schedule.registerObserver(teacher)
        assertTrue(schedule.classSessions.isEmpty())
    }

    @Test
    fun `creates session with correct properties`() {
        val teacher = Teacher("Alice")
        val start = LocalDateTime.of(2025, 1, 1, 9, 0)
        val end = LocalDateTime.of(2025, 1, 1, 10, 0)
        val session = ClassSession("1", "Math", start, end, teacher)
        assertEquals("1", session.id)
        assertEquals("Math", session.subject)
    }
}
