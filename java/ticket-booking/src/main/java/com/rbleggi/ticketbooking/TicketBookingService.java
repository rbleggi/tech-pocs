package com.rbleggi.ticketbooking;

public class TicketBookingService {
    public Ticket bookTicket(Ticket ticket) {
        if (!PlaceCapacity.isAvailable(ticket.show(), ticket.zone(), ticket.seat(), ticket.date())) {
            throw new IllegalArgumentException(
                "Seat " + ticket.seat() + " in zone " + ticket.zone() +
                " for '" + ticket.show() + "' on " + ticket.date() + " is already booked."
            );
        }
        PlaceCapacity.registerTicket(ticket);
        return ticket;
    }
}
