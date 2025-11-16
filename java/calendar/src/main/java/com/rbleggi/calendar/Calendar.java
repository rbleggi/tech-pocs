package com.rbleggi.calendar;

import java.util.*;
import java.util.stream.Collectors;

public class Calendar {
    private final Map<String, Meeting> meetings = new HashMap<>();

    public boolean bookMeeting(Meeting meeting) {
        boolean hasConflict = meetings.values().stream()
            .anyMatch(m -> {
                Set<User> commonAttendees = new HashSet<>(m.attendees());
                commonAttendees.retainAll(meeting.attendees());
                return !commonAttendees.isEmpty() &&
                       m.start().isBefore(meeting.end()) &&
                       meeting.start().isBefore(m.end());
            });

        if (hasConflict) {
            return false;
        }

        meetings.put(meeting.id(), meeting);
        return true;
    }

    public boolean removeMeeting(String meetingId) {
        return meetings.remove(meetingId) != null;
    }

    public List<Meeting> listMeetings(User user) {
        return meetings.values().stream()
            .filter(m -> m.attendees().contains(user))
            .collect(Collectors.toList());
    }

    public List<Meeting> getMeetingsForUsers(Set<User> users) {
        return meetings.values().stream()
            .filter(m -> {
                Set<User> commonUsers = new HashSet<>(m.attendees());
                commonUsers.retainAll(users);
                return !commonUsers.isEmpty();
            })
            .collect(Collectors.toList());
    }

    public List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetings.values());
    }
}
