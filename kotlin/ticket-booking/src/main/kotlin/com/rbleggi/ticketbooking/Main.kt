package com.rbleggi.ticketbooking

data class Ticket(val show: String, val zone: String, val seat: Int, val date: String)

object PlaceCapacity {
    private var bookedSeats: Set<Tuple4<String, String, Int, String>> = emptySet()

    fun isAvailable(show: String, zone: String, seat: Int, date: String): Boolean =
        !bookedSeats.contains(Tuple4(show, zone, seat, date))

    fun registerTicket(ticket: Ticket) {
        bookedSeats = bookedSeats + Tuple4(ticket.show, ticket.zone, ticket.seat, ticket.date)
    }

    fun clear() {
        bookedSeats = emptySet()
    }
}

data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

class TicketBookingService {
    fun bookTicket(ticket: Ticket): Ticket {
        if (!PlaceCapacity.isAvailable(ticket.show, ticket.zone, ticket.seat, ticket.date)) {
            throw IllegalArgumentException(
                "Seat ${ticket.seat} in zone ${ticket.zone} for '${ticket.show}' on ${ticket.date} is already booked."
            )
        }
        PlaceCapacity.registerTicket(ticket)
        return ticket
    }
}

class TicketBuilder {
    private var show: String = ""
    private var zone: String = ""
    private var seat: Int = 0
    private var date: String = ""

    fun setShow(show: String): TicketBuilder {
        this.show = show
        return this
    }

    fun setZone(zone: String): TicketBuilder {
        this.zone = zone
        return this
    }

    fun setSeat(seat: Int): TicketBuilder {
        this.seat = seat
        return this
    }

    fun setDate(date: String): TicketBuilder {
        this.date = date
        return this
    }

    fun buildTicket(): Ticket = Ticket(show, zone, seat, date)
}

fun main() {
    val bookingService = TicketBookingService()

    try {
        val ticket1 = TicketBuilder()
            .setShow("Iron Maiden")
            .setZone("1")
            .setSeat(12)
            .setDate("2025-05-01")
            .buildTicket()

        val bookedTicket1 = bookingService.bookTicket(ticket1)
        println("Ticket booked: $bookedTicket1")

        val ticket2 = TicketBuilder()
            .setShow("Iron Maiden")
            .setZone("1")
            .setSeat(12)
            .setDate("2025-05-01")
            .buildTicket()

        val bookedTicket2 = bookingService.bookTicket(ticket2)
        println("Ticket booked: $bookedTicket2")
    } catch (e: Exception) {
        println("Booking failed: ${e.message}")
    }
}
