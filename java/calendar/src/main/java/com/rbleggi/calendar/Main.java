package com.rbleggi.calendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class Main {
    public static void main(String[] args) {
        var calendar = new Calendar();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println("===== Calendar System Demo =====");

        var user1 = new User("1", "Alice");
        var user2 = new User("2", "Bob");
        var user3 = new User("3", "Charlie");

        System.out.println("Created users: " + user1.name() + ", " + user2.name() + ", " + user3.name() + "\n");

        var meeting1 = new Meeting("m1", "Sync",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2),
            Set.of(user1, user2));

        var result1 = new BookMeetingCommand(calendar, meeting1).execute();
        System.out.println("Book meeting '" + meeting1.title() + "' (" +
            meeting1.start().format(formatter) + " - " + meeting1.end().format(formatter) + "): " +
            (result1 ? "SUCCESS" : "FAILED"));

        var conflictingMeeting = new Meeting("m2", "Conflict",
            LocalDateTime.now().plusHours(1).plusMinutes(30),
            LocalDateTime.now().plusHours(2).plusMinutes(30),
            Set.of(user1, user3));

        var result2 = new BookMeetingCommand(calendar, conflictingMeeting).execute();
        System.out.println("Book meeting '" + conflictingMeeting.title() + "' (" +
            conflictingMeeting.start().format(formatter) + " - " + conflictingMeeting.end().format(formatter) + "): " +
            (result2 ? "SUCCESS" : "FAILED (Conflict detected)"));

        var meeting3 = new Meeting("m3", "Planning",
            LocalDateTime.now().plusHours(3),
            LocalDateTime.now().plusHours(4),
            Set.of(user1, user2, user3));

        var result3 = new BookMeetingCommand(calendar, meeting3).execute();
        System.out.println("Book meeting '" + meeting3.title() + "' (" +
            meeting3.start().format(formatter) + " - " + meeting3.end().format(formatter) + "): " +
            (result3 ? "SUCCESS" : "FAILED") + "\n");

        var aliceMeetings = new ListMeetingsCommand(calendar, user1).execute();
        System.out.println(user1.name() + "'s meetings (" + aliceMeetings.size() + "):");
        aliceMeetings.forEach(m -> System.out.println("  • " + m.title() + " at " +
            m.start().format(formatter) + " with " + m.attendees().size() + " attendees"));
        System.out.println();

        var removeResult = new RemoveMeetingCommand(calendar, "m1").execute();
        System.out.println("Remove meeting 'Sync': " + (removeResult ? "SUCCESS" : "FAILED") + "\n");

        var aliceMeetingsAfter = new ListMeetingsCommand(calendar, user1).execute();
        System.out.println(user1.name() + "'s meetings after removal (" + aliceMeetingsAfter.size() + "):");
        aliceMeetingsAfter.forEach(m -> System.out.println("  • " + m.title() + " at " +
            m.start().format(formatter) + " with " + m.attendees().size() + " attendees"));
    }
}
