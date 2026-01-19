package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ScheduleDifferentTeacherTest {
    @Test
    fun `allows overlapping for different teachers`() {
        val schedule = Schedule()
        val teacherA = Teacher("Alice")
        val teacherB = Teacher("Bob")
        val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacherA)
        val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacherB)
        schedule.addClassSession(session1)
        assertTrue(schedule.addClassSession(session2))
    }

    @Test
    fun `both sessions added for different teachers`() {
        val schedule = Schedule()
        val teacherA = Teacher("Alice")
        val teacherB = Teacher("Bob")
        val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacherA)
        val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacherB)
        schedule.addClassSession(session1)
        schedule.addClassSession(session2)
        assertEquals(2, schedule.classSessions.size)
    }
}
