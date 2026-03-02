package com.rbleggi.calendar

import java.time.{LocalDateTime, LocalTime, format}
import java.time.format.DateTimeFormatter

case class User(id: String, name: String)

case class Meeting(id: String, title: String, start: LocalDateTime, end: LocalDateTime, attendees: Set[User])

class Calendar:
  private var meetings: Map[String, Meeting] = Map.empty

  def bookMeeting(meeting: Meeting): Boolean =
    if meetings.values.exists(m =>
      m.attendees.intersect(meeting.attendees).nonEmpty && m.start.isBefore(meeting.end) && meeting.start.isBefore(m.end)
    ) then false
    else
      meetings += (meeting.id -> meeting)
      true

  def removeMeeting(meetingId: String): Boolean =
    if meetings.contains(meetingId) then
      meetings -= meetingId
      true
    else false

  def listMeetings(user: User): List[Meeting] =
    meetings.values.filter(_.attendees.contains(user)).toList

  def getMeetingsForUsers(users: Set[User]): List[Meeting] =
    meetings.values.filter(m => m.attendees.intersect(users).nonEmpty).toList

  def getAllMeetings: List[Meeting] = meetings.values.toList

trait CalendarCommand:
  def execute(): Any

class BookMeetingCommand(calendar: Calendar, meeting: Meeting) extends CalendarCommand:
  def execute(): Boolean = calendar.bookMeeting(meeting)

class RemoveMeetingCommand(calendar: Calendar, meetingId: String) extends CalendarCommand:
  def execute(): Boolean = calendar.removeMeeting(meetingId)

class ListMeetingsCommand(calendar: Calendar, user: User) extends CalendarCommand:
  def execute(): List[Meeting] = calendar.listMeetings(user)

class SuggestBestTimeCommand(calendar: Calendar, user1: User, user2: User, durationMinutes: Long) extends CalendarCommand:
  def execute(): Option[(LocalDateTime, LocalDateTime)] =
    SuggestionEngine.suggestBestTime(calendar, user1, user2, durationMinutes)

class CalendarInvoker:
  def execute(command: CalendarCommand): Any = command.execute()

object SuggestionEngine:
  def suggestBestTime(calendar: Calendar, user1: User, user2: User, durationMinutes: Long): Option[(LocalDateTime, LocalDateTime)] =
    val now = LocalDateTime.now()
    val endOfDay = now.`with`(LocalTime.MAX)
    val meetings = calendar.getMeetingsForUsers(Set(user1, user2))
    val busySlots = meetings.map(m => (m.start, m.end)).sortBy(_._1)
    var slotStart = now
    while slotStart.plusMinutes(durationMinutes).isBefore(endOfDay) do
      val slotEnd = slotStart.plusMinutes(durationMinutes)
      val overlaps = busySlots.exists { case (start, end) =>
        slotStart.isBefore(end) && slotEnd.isAfter(start)
      }
      if !overlaps then return Some((slotStart, slotEnd))
      slotStart = slotStart.plusMinutes(15)
    None

@main def runCalendarDemo(): Unit =
  println("Calendar")
