package com.rbleggi.calssorganizer

import kotlin.test.Test
import kotlin.test.assertTrue

class ScheduleRegisterTest {
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
}
