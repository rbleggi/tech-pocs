package com.rbleggi.calendar;

import java.util.List;

public class ListMeetingsCommand implements CalendarCommand<List<Meeting>> {
    private final Calendar calendar;
    private final User user;

    public ListMeetingsCommand(Calendar calendar, User user) {
        this.calendar = calendar;
        this.user = user;
    }

    @Override
    public List<Meeting> execute() {
        return calendar.listMeetings(user);
    }
}
