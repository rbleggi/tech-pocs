package com.rbleggi.calendar;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CalendarDifferentUserTest {
    @Test
    void allowsOverlappingForDifferentUsers() {
        var calendar = new Calendar();
        var user1 = new User("1", "Alice");
        var user2 = new User("2", "Bob");
        var m1 = new Meeting("m1", "Sync", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user1));
        var m2 = new Meeting("m2", "Other", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user2));
        calendar.bookMeeting(m1);
        assertTrue(calendar.bookMeeting(m2));
    }

    @Test
    void bothMeetingsAddedForDifferentUsers() {
        var calendar = new Calendar();
        var user1 = new User("1", "Alice");
        var user2 = new User("2", "Bob");
        var m1 = new Meeting("m1", "Sync", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user1));
        var m2 = new Meeting("m2", "Other", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0), Set.of(user2));
        calendar.bookMeeting(m1);
        calendar.bookMeeting(m2);
        assertEquals(2, calendar.getAllMeetings().size());
    }
}
