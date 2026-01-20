package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class RemoveMeetingCommandTest {
    @Test
    fun `execute removes meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        val command = RemoveMeetingCommand(calendar, meeting.id)
        assertTrue(command.execute())
    }

    @Test
    fun `undo restores removed meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        val command = RemoveMeetingCommand(calendar, meeting.id)
        command.execute()
        command.undo()
        assertEquals(1, calendar.getAllMeetings().size)
    }
}
