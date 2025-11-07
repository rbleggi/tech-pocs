package com.rbleggi.ticketbooking

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TicketBookingTest {
    @BeforeEach
    fun setup() {
        PlaceCapacity.clear()
    }

    @Test
    fun testBookTicketSuccess() {
        val service = TicketBookingService()
        val ticket = Ticket("Iron Maiden", "1", 12, "2025-05-01")
        val booked = service.bookTicket(ticket)
        assertEquals(ticket, booked)
    }

    @Test
    fun testBookTicketDuplicate() {
        val service = TicketBookingService()
        val ticket1 = Ticket("Iron Maiden", "1", 12, "2025-05-01")
        service.bookTicket(ticket1)

        val ticket2 = Ticket("Iron Maiden", "1", 12, "2025-05-01")
        assertFailsWith<IllegalArgumentException> {
            service.bookTicket(ticket2)
        }
    }
}
