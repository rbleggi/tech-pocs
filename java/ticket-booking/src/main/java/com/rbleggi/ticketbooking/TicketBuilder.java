package com.rbleggi.ticketbooking;

public class TicketBuilder {
    private String show = "";
    private String zone = "";
    private int seat = 0;
    private String date = "";

    public TicketBuilder setShow(String show) {
        this.show = show;
        return this;
    }

    public TicketBuilder setZone(String zone) {
        this.zone = zone;
        return this;
    }

    public TicketBuilder setSeat(int seat) {
        this.seat = seat;
        return this;
    }

    public TicketBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    public Ticket buildTicket() {
        return new Ticket(show, zone, seat, date);
    }
}
