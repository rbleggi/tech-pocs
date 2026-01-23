package com.rbleggi.calendar;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class BookMeetingCommandTest {
    @Test
    void executeBooksMeeting() {
        var calendar = new Calendar();
        var user = new User("1", "Alice");
        var meeting = new Meeting("m1", "Sync", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user));
        var command = new BookMeetingCommand(calendar, meeting);
        assertTrue(command.execute());
    }

    @Test
    void executeAddsMeetingToCalendar() {
        var calendar = new Calendar();
        var user = new User("1", "Alice");
        var meeting = new Meeting("m1", "Sync", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user));
        new BookMeetingCommand(calendar, meeting).execute();
        assertEquals(1, calendar.getAllMeetings().size());
    }
}
