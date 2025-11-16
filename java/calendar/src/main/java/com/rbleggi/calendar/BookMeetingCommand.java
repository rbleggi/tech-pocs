package com.rbleggi.calendar;

public class BookMeetingCommand implements CalendarCommand<Boolean> {
    private final Calendar calendar;
    private final Meeting meeting;

    public BookMeetingCommand(Calendar calendar, Meeting meeting) {
        this.calendar = calendar;
        this.meeting = meeting;
    }

    @Override
    public Boolean execute() {
        return calendar.bookMeeting(meeting);
    }
}
