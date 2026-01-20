package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class BookMeetingCommandTest {
    @Test
    fun `execute books meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        val command = BookMeetingCommand(calendar, meeting)
        assertTrue(command.execute())
    }

    @Test
    fun `undo removes booked meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        val command = BookMeetingCommand(calendar, meeting)
        command.execute()
        command.undo()
        assertEquals(0, calendar.getAllMeetings().size)
    }
}
