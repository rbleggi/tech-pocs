package com.rbleggi.ticketbooking

class TicketBookingSpec {
  test("TicketBookingService should book a ticket if available") {
    val service = new TicketBookingService
    val ticket = Ticket("Show1", "A", 1, "2025-10-20")
    val booked = service.bookTicket(ticket)
    assert(booked == ticket)
  }

  test("TicketBookingService should not book a ticket if already booked") {
    val service = new TicketBookingService
    val ticket = Ticket("Show1", "A", 1, "2025-10-20")
    service.bookTicket(ticket)
    intercept[IllegalArgumentException] {
      service.bookTicket(ticket)
    }
  }

  test("TicketBuilder should build a ticket with correct fields") {
    val builder = new TicketBuilder()
      .setShow("Show2")
      .setZone("B")
      .setSeat(2)
      .setDate("2025-10-21")
    val ticket = builder.buildTicket()
    assert(ticket.show == "Show2")
    assert(ticket.zone == "B")
    assert(ticket.seat == 2)
    assert(ticket.date == "2025-10-21")
  }
}
