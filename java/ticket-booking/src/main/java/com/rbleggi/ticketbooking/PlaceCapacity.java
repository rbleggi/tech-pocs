package com.rbleggi.ticketbooking;

import java.util.HashSet;
import java.util.Set;

public class PlaceCapacity {
    private static final Set<BookedSeat> bookedSeats = new HashSet<>();

    public static boolean isAvailable(String show, String zone, int seat, String date) {
        return !bookedSeats.contains(new BookedSeat(show, zone, seat, date));
    }

    public static void registerTicket(Ticket ticket) {
        bookedSeats.add(new BookedSeat(ticket.show(), ticket.zone(), ticket.seat(), ticket.date()));
    }

    public static void clear() {
        bookedSeats.clear();
    }

    private record BookedSeat(String show, String zone, int seat, String date) {}
}
