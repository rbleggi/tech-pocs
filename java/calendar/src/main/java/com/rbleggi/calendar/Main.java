package com.rbleggi.calendar;

import java.time.LocalDateTime;
import java.util.*;

record User(String id, String name) {}

record Meeting(String id, String title, LocalDateTime start, LocalDateTime end, Set<User> attendees) {}

interface CalendarCommand<T> {
    T execute();
}

class Calendar {
    private final Map<String, Meeting> meetings = new HashMap<>();

    boolean bookMeeting(Meeting meeting) {
        boolean hasConflict = meetings.values().stream()
            .anyMatch(m -> {
                Set<User> common = new HashSet<>(m.attendees());
                common.retainAll(meeting.attendees());
                return !common.isEmpty() && m.start().isBefore(meeting.end()) && meeting.start().isBefore(m.end());
            });
        if (hasConflict) return false;
        meetings.put(meeting.id(), meeting);
        return true;
    }

    boolean removeMeeting(String meetingId) {
        return meetings.remove(meetingId) != null;
    }

    List<Meeting> listMeetings(User user) {
        return meetings.values().stream().filter(m -> m.attendees().contains(user)).toList();
    }

    List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetings.values());
    }
}

record BookMeetingCommand(Calendar calendar, Meeting meeting) implements CalendarCommand<Boolean> {
    @Override
    public Boolean execute() {
        return calendar.bookMeeting(meeting);
    }
}

record RemoveMeetingCommand(Calendar calendar, String meetingId) implements CalendarCommand<Boolean> {
    @Override
    public Boolean execute() {
        return calendar.removeMeeting(meetingId);
    }
}

record ListMeetingsCommand(Calendar calendar, User user) implements CalendarCommand<List<Meeting>> {
    @Override
    public List<Meeting> execute() {
        return calendar.listMeetings(user);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Calendar");
    }
}
