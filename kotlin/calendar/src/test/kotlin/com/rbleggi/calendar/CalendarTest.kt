package com.rbleggi.calendar

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CalendarTest {

    @Test
    fun `user has correct id and name`() {
        val user = User("1", "Alice")
        assertEquals("1", user.id)
        assertEquals("Alice", user.name)
    }

    @Test
    fun `meeting has correct title and generates id`() {
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.now(), end = LocalDateTime.now().plusHours(1), attendees = setOf(user))
        assertEquals("Test", meeting.title)
        assertNotNull(meeting.id)
    }

    @Test
    fun `bookMeeting returns true for valid meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        assertTrue(calendar.bookMeeting(meeting))
        assertEquals(1, calendar.getAllMeetings().size)
    }

    @Test
    fun `bookMeeting rejects overlapping meeting`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting1 = Meeting(title = "Test1", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        val meeting2 = Meeting(title = "Test2", start = LocalDateTime.of(2025, 1, 1, 9, 30), end = LocalDateTime.of(2025, 1, 1, 10, 30), attendees = setOf(user))
        calendar.bookMeeting(meeting1)
        assertFalse(calendar.bookMeeting(meeting2))
        assertEquals(1, calendar.getAllMeetings().size)
    }

    @Test
    fun `removeMeeting returns true for existing and false for missing`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        assertTrue(calendar.removeMeeting(meeting.id))
        assertFalse(calendar.removeMeeting("nonexistent"))
    }

    @Test
    fun `listMeetings returns meetings for user`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        assertTrue(calendar.listMeetings(user).isEmpty())
        calendar.bookMeeting(meeting)
        assertEquals(1, calendar.listMeetings(user).size)
    }

    @Test
    fun `book meeting command executes and undoes`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        val command = BookMeetingCommand(calendar, meeting)
        assertTrue(command.execute())
        command.undo()
        assertEquals(0, calendar.getAllMeetings().size)
    }

    @Test
    fun `remove meeting command executes and undoes`() {
        val calendar = Calendar()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        calendar.bookMeeting(meeting)
        val command = RemoveMeetingCommand(calendar, meeting.id)
        assertTrue(command.execute())
        command.undo()
        assertEquals(1, calendar.getAllMeetings().size)
    }

    @Test
    fun `invoker executes and undoes command`() {
        val calendar = Calendar()
        val invoker = CalendarInvoker()
        val user = User("1", "Alice")
        val meeting = Meeting(title = "Test", start = LocalDateTime.of(2025, 1, 1, 9, 0), end = LocalDateTime.of(2025, 1, 1, 10, 0), attendees = setOf(user))
        assertTrue(invoker.execute(BookMeetingCommand(calendar, meeting)) as Boolean)
        invoker.undo()
        assertEquals(0, calendar.getAllMeetings().size)
    }

    @Test
    fun `invoker redo re-executes undone command`() {
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
    fun `invoker redo returns null when stack empty`() {
        val invoker = CalendarInvoker()
        assertNull(invoker.redo())
    }
}
