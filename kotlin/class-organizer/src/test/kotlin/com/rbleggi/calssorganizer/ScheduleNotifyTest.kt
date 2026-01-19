package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ScheduleNotifyTest {
    @Test
    fun `addClassSession notifies observers`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        val schedule = Schedule()
        val teacher = Teacher("Alice")
        schedule.registerObserver(teacher)
        val session = ClassSession("1", "Math", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), teacher)
        schedule.addClassSession(session)
        assertTrue(output.toString().contains("Teacher Alice notified"))
    }
}
