package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CalendarInvokerRedoTest {
    @Test
    fun `redo re-executes undone command`() {
        val calendar = Calendar()
        val invoker = CalendarInvoker()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        invoker.execute(BookMeetingCommand(calendar, meeting))
        invoker.undo()
        invoker.redo()
        assertEquals(1, calendar.getAllMeetings().size)
    }

    @Test
    fun `redo returns null when stack empty`() {
        val invoker = CalendarInvoker()
        assertNull(invoker.redo())
    }
}
