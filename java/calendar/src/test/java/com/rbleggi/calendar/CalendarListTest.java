package com.rbleggi.calendar;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CalendarListTest {
    @Test
    void listMeetingsReturnsUserMeetings() {
        var calendar = new Calendar();
        var user = new User("1", "Alice");
        var meeting = new Meeting("m1", "Sync", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user));
        calendar.bookMeeting(meeting);
        assertEquals(1, calendar.listMeetings(user).size());
    }

    @Test
    void listMeetingsReturnsEmptyForUserWithoutMeetings() {
        var calendar = new Calendar();
        var user = new User("1", "Alice");
        assertTrue(calendar.listMeetings(user).isEmpty());
    }
}
