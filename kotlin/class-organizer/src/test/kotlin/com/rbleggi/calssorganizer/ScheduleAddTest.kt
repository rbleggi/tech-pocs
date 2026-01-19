package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ScheduleAddTest {
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
}
