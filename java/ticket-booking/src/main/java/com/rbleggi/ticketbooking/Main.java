package com.rbleggi.ticketbooking;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public record Ticket(String show, String zone, int seat, String date) {}

    public static class TicketBuilder {
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

    public static class PlaceCapacity {
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

    public static class TicketBookingService {
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

    public static void main(String[] args) {
        System.out.println("Ticket Booking");
    }
}
