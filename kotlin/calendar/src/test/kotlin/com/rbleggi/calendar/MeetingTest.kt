package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MeetingTest {
    @Test
    fun `meeting has correct title`() {
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.now(), end = LocalDateTime.now().plusHours(1), attendees = setOf(user))
        assertEquals("Test", meeting.title)
    }

    @Test
    fun `meeting generates id`() {
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.now(), end = LocalDateTime.now().plusHours(1), attendees = setOf(user))
        assertNotNull(meeting.id)
    }
}
