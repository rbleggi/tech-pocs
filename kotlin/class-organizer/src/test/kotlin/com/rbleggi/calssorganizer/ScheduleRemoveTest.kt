package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class ScheduleRemoveTest {
    @Test
    fun `removeClassSession returns true for existing`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        schedule.addClassSession(session)
        assertTrue(schedule.removeClassSession(session))
    }

    @Test
    fun `removeClassSession returns false for missing`() {
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        val session = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        assertFalse(schedule.removeClassSession(session))
    }
}
