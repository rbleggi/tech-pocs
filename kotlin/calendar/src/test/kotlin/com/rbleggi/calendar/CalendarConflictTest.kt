package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class CalendarConflictTest {
    @Test
    fun `bookMeeting rejects overlapping meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting1 = Meeting(title = "Test1", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        val meeting2 = Meeting(title = "Test2", start = LocalDateTime.of(2025, 1, 1, 9, 30), end = LocalDateTime.of(2025, 1, 1, 10, 30), attendees = setOf(user))
        calendar.bookMeeting(meeting1)
        assertFalse(calendar.bookMeeting(meeting2))
    }

    @Test
    fun `conflict does not add to list`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting1 = Meeting(title = "Test1", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        val meeting2 = Meeting(title = "Test2", start = LocalDateTime.of(2025, 1, 1, 9, 30), end = LocalDateTime.of(2025, 1, 1, 10, 30), attendees = setOf(user))
        calendar.bookMeeting(meeting1)
        calendar.bookMeeting(meeting2)
        assertEquals(1, calendar.getAllMeetings().size)
    }
}
