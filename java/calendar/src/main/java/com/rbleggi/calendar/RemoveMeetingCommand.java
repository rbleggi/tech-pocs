package com.rbleggi.calendar;

public class RemoveMeetingCommand implements CalendarCommand<Boolean> {
    private final Calendar calendar;
    private final String meetingId;

    public RemoveMeetingCommand(Calendar calendar, String meetingId) {
        this.calendar = calendar;
        this.meetingId = meetingId;
    }

    @Override
    public Boolean execute() {
        return calendar.removeMeeting(meetingId);
    }
}
