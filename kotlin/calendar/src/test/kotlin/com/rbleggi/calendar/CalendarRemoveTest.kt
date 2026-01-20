package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CalendarRemoveTest {
    @Test
    fun `removeMeeting returns true for existing`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        assertTrue(calendar.removeMeeting(meeting.id))
    }

    @Test
    fun `removeMeeting returns false for missing`() {
        val calendar = Calendar()
        assertFalse(calendar.removeMeeting("nonexistent"))
    }
}
