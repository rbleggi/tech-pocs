package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class CalendarInvokerTest {
    @Test
    fun `execute runs command`() {
        val calendar = Calendar()
        val invoker = CalendarInvoker()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        assertTrue(invoker.execute(BookMeetingCommand(calendar, meeting)) as Boolean)
    }

    @Test
    fun `undo reverses command`() {
        val calendar = Calendar()
        val invoker = CalendarInvoker()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        invoker.execute(BookMeetingCommand(calendar, meeting))
        invoker.undo()
        assertEquals(0, calendar.getAllMeetings().size)
    }
}
