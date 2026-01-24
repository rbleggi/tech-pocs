package com.rbleggi.calendar;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CalendarDemoTest {
    @Test
    void userHasCorrectId() {
        var user = new User("1", "Alice");
        assertEquals("1", user.id());
    }

    @Test
    void userHasCorrectName() {
        var user = new User("1", "Alice");
        assertEquals("Alice", user.name());
    }

    @Test
    void meetingHasCorrectTitle() {
        var user = new User("1", "Alice");
        var meeting = new Meeting("m1", "Sync", LocalDateTime.now(), LocalDateTime.now().plusHours(1), Set.of(user));
        assertEquals("Sync", meeting.title());
    }

    @Test
    void meetingHasCorrectId() {
        var user = new User("1", "Alice");
        var meeting = new Meeting("m1", "Sync", LocalDateTime.now(), LocalDateTime.now().plusHours(1), Set.of(user));
        assertEquals("m1", meeting.id());
    }
}
