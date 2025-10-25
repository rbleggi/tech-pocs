package com.rbleggi.calendar

import java.time.LocalDateTime

class CalendarSpec {
  val user1 = User("1", "Alice")
  val user2 = User("2", "Bob")
  val user3 = User("3", "Charlie")

  test("bookMeeting should add a meeting if no conflict") {
    val calendar = new Calendar
    val meeting = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    assert(calendar.bookMeeting(meeting))
    assert(calendar.listMeetings(user1).contains(meeting))
  }

  test("bookMeeting should not add a meeting if there is a conflict") {
    val calendar = new Calendar
    val m1 = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    val m2 = Meeting("m2", "Conflict", LocalDateTime.now().plusHours(1).plusMinutes(30), LocalDateTime.now().plusHours(2).plusMinutes(30), Set(user1, user3))
    assert(calendar.bookMeeting(m1))
    assert(!calendar.bookMeeting(m2))
  }

  test("removeMeeting should remove a meeting by id") {
    val calendar = new Calendar
    val meeting = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    calendar.bookMeeting(meeting)
    assert(calendar.removeMeeting("m1"))
    assert(!calendar.listMeetings(user1).contains(meeting))
  }

  test("listMeetings should return all meetings for a user") {
    val calendar = new Calendar
    val m1 = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    val m2 = Meeting("m2", "Planning", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), Set(user1, user3))
    calendar.bookMeeting(m1)
    calendar.bookMeeting(m2)
    val meetings = calendar.listMeetings(user1)
    assert(meetings.contains(m1) && meetings.contains(m2))
  }

  test("getMeetingsForUsers should return meetings for multiple users") {
    val calendar = new Calendar
    val m1 = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    val m2 = Meeting("m2", "Planning", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), Set(user2, user3))
    val m3 = Meeting("m3", "Solo", LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6), Set(user1))
    calendar.bookMeeting(m1)
    calendar.bookMeeting(m2)
    calendar.bookMeeting(m3)

    val meetings = calendar.getMeetingsForUsers(Set(user2))
    assert(meetings.contains(m1))
    assert(meetings.contains(m2))
    assert(!meetings.contains(m3))
  }

  test("getAllMeetings should return all meetings in calendar") {
    val calendar = new Calendar
    val m1 = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    val m2 = Meeting("m2", "Planning", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), Set(user2, user3))
    calendar.bookMeeting(m1)
    calendar.bookMeeting(m2)

    val allMeetings = calendar.getAllMeetings
    assert(allMeetings.size == 2)
    assert(allMeetings.contains(m1))
    assert(allMeetings.contains(m2))
  }

  test("BookMeetingCommand should book meeting when executed") {
    val calendar = new Calendar
    val meeting = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    val cmd = new BookMeetingCommand(calendar, meeting)
    val invoker = new CalendarInvoker

    val result = invoker.execute(cmd).asInstanceOf[Boolean]
    assert(result)
    assert(calendar.listMeetings(user1).contains(meeting))
  }

  test("RemoveMeetingCommand should remove meeting when executed") {
    val calendar = new Calendar
    val meeting = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    calendar.bookMeeting(meeting)

    val cmd = new RemoveMeetingCommand(calendar, "m1")
    val invoker = new CalendarInvoker

    val result = invoker.execute(cmd).asInstanceOf[Boolean]
    assert(result)
    assert(!calendar.listMeetings(user1).contains(meeting))
  }

  test("ListMeetingsCommand should return meetings for user") {
    val calendar = new Calendar
    val meeting = Meeting("m1", "Sync", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1, user2))
    calendar.bookMeeting(meeting)

    val cmd = new ListMeetingsCommand(calendar, user1)
    val invoker = new CalendarInvoker

    val meetings = invoker.execute(cmd).asInstanceOf[List[Meeting]]
    assert(meetings.contains(meeting))
  }

  test("SuggestBestTimeCommand should suggest available time slot") {
    val calendar = new Calendar
    val cmd = new SuggestBestTimeCommand(calendar, user1, user2, 30)
    val invoker = new CalendarInvoker

    val suggestion = invoker.execute(cmd).asInstanceOf[Option[(LocalDateTime, LocalDateTime)]]
    assert(suggestion.isDefined)
  }

  test("SuggestionEngine should find time slot when users are free") {
    val calendar = new Calendar
    val suggestion = SuggestionEngine.suggestBestTime(calendar, user1, user2, 30)
    assert(suggestion.isDefined)
  }

  test("SuggestionEngine should avoid busy slots") {
    val calendar = new Calendar
    val now = LocalDateTime.now()
    val m1 = Meeting("m1", "Busy", now.plusMinutes(30), now.plusMinutes(90), Set(user1, user2))
    calendar.bookMeeting(m1)

    val suggestion = SuggestionEngine.suggestBestTime(calendar, user1, user2, 30)
    suggestion match {
      case Some((start, end)) =>
        assert(start.isAfter(m1.end) || end.isBefore(m1.start))
      case None =>
    }
  }

  test("Calendar should not book meeting with exact time overlap") {
    val calendar = new Calendar
    val start = LocalDateTime.now().plusHours(1)
    val end = LocalDateTime.now().plusHours(2)
    val m1 = Meeting("m1", "First", start, end, Set(user1))
    val m2 = Meeting("m2", "Second", start, end, Set(user1))

    assert(calendar.bookMeeting(m1))
    assert(!calendar.bookMeeting(m2))
  }

  test("Calendar should allow back-to-back meetings") {
    val calendar = new Calendar
    val m1 = Meeting("m1", "First", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Set(user1))
    val m2 = Meeting("m2", "Second", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), Set(user1))

    assert(calendar.bookMeeting(m1))
    assert(calendar.bookMeeting(m2))
  }
}
