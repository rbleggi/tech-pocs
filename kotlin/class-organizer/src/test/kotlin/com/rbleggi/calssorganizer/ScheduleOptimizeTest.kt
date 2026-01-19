package com.rbleggi.calssorganizer

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ScheduleOptimizeTest {
    @Test
    fun `optimizeSchedule prints message`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        val schedule = Schedule()
        schedule.optimizeSchedule()
        assertTrue(output.toString().contains("Optimizing schedule"))
    }
}
