package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class ScheduleConflictTest {
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
