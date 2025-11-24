package com.rbleggi.ticketbooking;

public class Main {
    public static void main(String[] args) {
        var bookingService = new TicketBookingService();

        try {
            var ticket1 = new TicketBuilder()
                .setShow("Iron Maiden")
                .setZone("1")
                .setSeat(12)
                .setDate("2025-05-01")
                .buildTicket();

            var bookedTicket1 = bookingService.bookTicket(ticket1);
            System.out.println("Ticket booked: " + bookedTicket1);

            var ticket2 = new TicketBuilder()
                .setShow("Iron Maiden")
                .setZone("1")
                .setSeat(12)
                .setDate("2025-05-01")
                .buildTicket();

            var bookedTicket2 = bookingService.bookTicket(ticket2);
            System.out.println("Ticket booked: " + bookedTicket2);
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}
