package com.rbleggi.calendar;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ListMeetingsCommandTest {
    @Test
    void executeReturnsUserMeetings() {
        var calendar = new Calendar();
        var user = new User("1", "Alice");
        var meeting = new Meeting("m1", "Sync", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user));
        calendar.bookMeeting(meeting);
        var command = new ListMeetingsCommand(calendar, user);
        assertEquals(1, command.execute().size());
    }

    @Test
    void executeReturnsEmptyForNoMeetings() {
        var calendar = new Calendar();
        var user = new User("1", "Alice");
        var command = new ListMeetingsCommand(calendar, user);
        assertTrue(command.execute().isEmpty());
    }
}
