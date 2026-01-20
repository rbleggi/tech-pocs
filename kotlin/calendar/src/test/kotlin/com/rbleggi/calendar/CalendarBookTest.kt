package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class CalendarBookTest {
    @Test
    fun `bookMeeting returns true for valid meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        assertTrue(calendar.bookMeeting(meeting))
    }

    @Test
    fun `bookMeeting adds to list`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        assertEquals(1, calendar.getAllMeetings().size)
    }
}
