package com.rbleggi.calendar;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
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
}
