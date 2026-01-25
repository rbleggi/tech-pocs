package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

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

    @Test
    fun `addClassSession returns true for valid session`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        assertTrue(schedule.addClassSession(session))
    }

    @Test
    fun `addClassSession adds to list`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        schedule.addClassSession(session)
        assertEquals(1, schedule.classSessions.size)
    }

    @Test
    fun `addClassSession rejects overlapping session`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 1, 1, 9, 30), LocalDateTime.of(2025, 1, 1, 10, 30), teacher)
        schedule.addClassSession(session1)
        assertFalse(schedule.addClassSession(session2))
    }

    @Test
    fun `conflict does not add to list`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 1, 1, 9, 30), LocalDateTime.of(2025, 1, 1, 10, 30), teacher)
        schedule.addClassSession(session1)
        schedule.addClassSession(session2)
        assertEquals(1, schedule.classSessions.size)
    }
}
