package com.rbleggi.ticketbooking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.rbleggi.ticketbooking.TicketBookingSystem.*;

class TicketBookingSystemTest {

    @AfterEach
    void cleanUp() {
        PlaceCapacity.clear();
    }

    @Test
    void shouldBuildTicketWithAllFields() {
        var ticket = new TicketBuilder()
            .setShow("Iron Maiden")
            .setZone("1")
            .setSeat(12)
            .setDate("2025-05-01")
            .buildTicket();

        assertEquals("Iron Maiden", ticket.show());
        assertEquals("1", ticket.zone());
        assertEquals(12, ticket.seat());
        assertEquals("2025-05-01", ticket.date());
    }

    @Test
    void shouldBuildTicketWithDefaultValues() {
        var ticket = new TicketBuilder().buildTicket();

        assertEquals("", ticket.show());
        assertEquals("", ticket.zone());
        assertEquals(0, ticket.seat());
        assertEquals("", ticket.date());
    }

    @Test
    void shouldBookAvailableTicket() {
        var bookingService = new TicketBookingService();
        var ticket = new TicketBuilder()
            .setShow("Metallica")
            .setZone("A")
            .setSeat(10)
            .setDate("2025-06-15")
            .buildTicket();

        var bookedTicket = bookingService.bookTicket(ticket);

        assertEquals(ticket, bookedTicket);
    }

    @Test
    void shouldThrowExceptionWhenBookingAlreadyBookedSeat() {
        var bookingService = new TicketBookingService();
        var ticket1 = new TicketBuilder()
            .setShow("Queen")
            .setZone("B")
            .setSeat(20)
            .setDate("2025-07-20")
            .buildTicket();

        bookingService.bookTicket(ticket1);

        var ticket2 = new TicketBuilder()
            .setShow("Queen")
            .setZone("B")
            .setSeat(20)
            .setDate("2025-07-20")
            .buildTicket();

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.bookTicket(ticket2);
        });

        assertTrue(exception.getMessage().contains("already booked"));
    }

    @Test
    void shouldAllowBookingSameSeatForDifferentShows() {
        var bookingService = new TicketBookingService();
        var ticket1 = new TicketBuilder()
            .setShow("Show A")
            .setZone("1")
            .setSeat(5)
            .setDate("2025-08-01")
            .buildTicket();

        var ticket2 = new TicketBuilder()
            .setShow("Show B")
            .setZone("1")
            .setSeat(5)
            .setDate("2025-08-01")
            .buildTicket();

        assertDoesNotThrow(() -> {
            bookingService.bookTicket(ticket1);
            bookingService.bookTicket(ticket2);
        });
    }

    @Test
    void shouldAllowBookingSameSeatForDifferentDates() {
        var bookingService = new TicketBookingService();
        var ticket1 = new TicketBuilder()
            .setShow("The Beatles")
            .setZone("VIP")
            .setSeat(1)
            .setDate("2025-09-01")
            .buildTicket();

        var ticket2 = new TicketBuilder()
            .setShow("The Beatles")
            .setZone("VIP")
            .setSeat(1)
            .setDate("2025-09-02")
            .buildTicket();

        assertDoesNotThrow(() -> {
            bookingService.bookTicket(ticket1);
            bookingService.bookTicket(ticket2);
        });
    }

    @Test
    void shouldAllowBookingSameSeatForDifferentZones() {
        var bookingService = new TicketBookingService();
        var ticket1 = new TicketBuilder()
            .setShow("Pink Floyd")
            .setZone("A")
            .setSeat(15)
            .setDate("2025-10-01")
            .buildTicket();

        var ticket2 = new TicketBuilder()
            .setShow("Pink Floyd")
            .setZone("B")
            .setSeat(15)
            .setDate("2025-10-01")
            .buildTicket();

        assertDoesNotThrow(() -> {
            bookingService.bookTicket(ticket1);
            bookingService.bookTicket(ticket2);
        });
    }

    @Test
    void shouldCheckSeatAvailability() {
        var ticket = new TicketBuilder()
            .setShow("AC/DC")
            .setZone("General")
            .setSeat(100)
            .setDate("2025-11-15")
            .buildTicket();

        assertTrue(PlaceCapacity.isAvailable("AC/DC", "General", 100, "2025-11-15"));

        PlaceCapacity.registerTicket(ticket);

        assertFalse(PlaceCapacity.isAvailable("AC/DC", "General", 100, "2025-11-15"));
    }

    @Test
    void shouldClearAllBookedSeats() {
        var ticket1 = new TicketBuilder()
            .setShow("Show 1")
            .setZone("Z1")
            .setSeat(1)
            .setDate("2025-12-01")
            .buildTicket();

        var ticket2 = new TicketBuilder()
            .setShow("Show 2")
            .setZone("Z2")
            .setSeat(2)
            .setDate("2025-12-02")
            .buildTicket();

        PlaceCapacity.registerTicket(ticket1);
        PlaceCapacity.registerTicket(ticket2);

        assertFalse(PlaceCapacity.isAvailable("Show 1", "Z1", 1, "2025-12-01"));
        assertFalse(PlaceCapacity.isAvailable("Show 2", "Z2", 2, "2025-12-02"));

        PlaceCapacity.clear();

        assertTrue(PlaceCapacity.isAvailable("Show 1", "Z1", 1, "2025-12-01"));
        assertTrue(PlaceCapacity.isAvailable("Show 2", "Z2", 2, "2025-12-02"));
    }

    @Test
    void shouldHandleMultipleBookings() {
        var bookingService = new TicketBookingService();

        for (int i = 1; i <= 10; i++) {
            var ticket = new TicketBuilder()
                .setShow("Concert")
                .setZone("Main")
                .setSeat(i)
                .setDate("2025-12-25")
                .buildTicket();

            assertDoesNotThrow(() -> bookingService.bookTicket(ticket));
        }

        for (int i = 1; i <= 10; i++) {
            final int seat = i;
            assertFalse(PlaceCapacity.isAvailable("Concert", "Main", seat, "2025-12-25"));
        }
    }
}
