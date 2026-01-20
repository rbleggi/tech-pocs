package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalendarListTest {
    @Test
    fun `listMeetings returns meetings for user`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        assertEquals(1, calendar.listMeetings(user).size)
    }

    @Test
    fun `listMeetings returns empty for user without meetings`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        assertTrue(calendar.listMeetings(user).isEmpty())
    }
}
