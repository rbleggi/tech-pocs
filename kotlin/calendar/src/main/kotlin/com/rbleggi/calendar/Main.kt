package com.rbleggi.calendar

import java.time.LocalDateTime
import java.util.UUID

data class User(val id: String, val name: String)

data class Meeting(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val attendees: Set<User>
)

class Calendar {
    private val meetings = mutableMapOf<String, Meeting>()

    fun bookMeeting(meeting: Meeting): Boolean {
        val conflicts = meetings.values.any { m ->
            m.attendees.intersect(meeting.attendees).isNotEmpty()
                    && m.start < meeting.end
                    && meeting.start < m.end
        }
        if (conflicts) return false
        meetings[meeting.id] = meeting
        return true
    }

    fun removeMeeting(meetingId: String): Boolean {
        return meetings.remove(meetingId) != null
    }

    fun listMeetings(user: User): List<Meeting> {
        return meetings.values.filter { it.attendees.contains(user) }
    }

    fun getMeetingsForUsers(users: Set<User>): List<Meeting> {
        return meetings.values.filter { it.attendees.intersect(users).isNotEmpty() }
    }

    fun getAllMeetings(): List<Meeting> = meetings.values.toList()
}

interface Command {
    fun execute(): Any?
    fun undo(): Any
}

class BookMeetingCommand(private val calendar: Calendar, private val meeting: Meeting) : Command {
    private var booked = false
    override fun execute(): Boolean {
        booked = calendar.bookMeeting(meeting)
        return booked
    }

    override fun undo(): Boolean {
        if (booked) {
            return calendar.removeMeeting(meeting.id)
        }
        return false
    }
}

class RemoveMeetingCommand(private val calendar: Calendar, private val meetingId: String) : Command {
    private var removedMeeting: Meeting? = null
    override fun execute(): Boolean {
        removedMeeting = calendar.getAllMeetings().find { it.id == meetingId }
        return calendar.removeMeeting(meetingId)
    }

    override fun undo(): Boolean {
        removedMeeting?.let { return calendar.bookMeeting(it) }
        return false
    }
}

class ListMeetingsCommand(private val calendar: Calendar, private val user: User) : Command {
    override fun execute(): List<Meeting> = calendar.listMeetings(user)
    override fun undo(): Any = Unit
}

class SuggestBestTimeCommand(
    private val calendar: Calendar,
    private val user1: User,
    private val user2: User,
    private val durationMinutes: Long
) : Command {
    override fun execute(): Any? {
        val now = LocalDateTime.now()
        val endSearch = now.plusDays(7)
        val meetings = calendar.getMeetingsForUsers(setOf(user1, user2))
        var slotStart = now
        while (slotStart.plusMinutes(durationMinutes) <= endSearch) {
            val slotEnd = slotStart.plusMinutes(durationMinutes)
            val conflict = meetings.any { m ->
                m.start < slotEnd && slotStart < m.end
            }
            if (!conflict) return slotStart to slotEnd
            slotStart = slotStart.plusMinutes(30)
        }
        return null
    }

    override fun undo(): Any = Unit
}

class CalendarInvoker {
    private val undoStack = ArrayDeque<Command>()
    private val redoStack = ArrayDeque<Command>()

    fun execute(command: Command): Any? {
        val result = command.execute()
        if (command !is ListMeetingsCommand && command !is SuggestBestTimeCommand) {
            undoStack.addLast(command)
            redoStack.clear()
        }
        return result
    }

    fun undo(): Any? {
        val command = undoStack.removeLastOrNull() ?: return null
        val result = command.undo()
        redoStack.addLast(command)
        return result
    }

    fun redo(): Any? {
        val command = redoStack.removeLastOrNull() ?: return null
        val result = command.execute()
        undoStack.addLast(command)
        return result
    }
}

fun main() {
    println("Calendar")
}
