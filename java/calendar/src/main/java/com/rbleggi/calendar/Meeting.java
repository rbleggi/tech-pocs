package com.rbleggi.calendar;

import java.time.LocalDateTime;
import java.util.Set;

public record Meeting(String id, String title, LocalDateTime start, LocalDateTime end, Set<User> attendees) {}
