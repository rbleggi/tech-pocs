package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ScheduleAdjacentTest {
    @Test
    fun `allows adjacent sessions for same teacher`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0), teacher)
        schedule.addClassSession(session1)
        assertTrue(schedule.addClassSession(session2))
    }

    @Test
    fun `both adjacent sessions added`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0), teacher)
        schedule.addClassSession(session1)
        schedule.addClassSession(session2)
        assertEquals(2, schedule.classSessions.size)
    }
}
