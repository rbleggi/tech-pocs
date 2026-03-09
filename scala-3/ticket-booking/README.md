# **Ticket Booking**

## Overview

This project implements a flexible and safe ticket booking system using the Builder Pattern. Users can choose zones, dates, seats, and shows, while the system enforces maximum place capacity and prevents overbooking.

---

## Tech Stack

- **Language** -> Scala 3
- **Build Tool** -> sbt
- **Testing** -> ScalaTest 3.2.16
- **JDK** -> 25

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Ticket {
        +show: String
        +zone: String
        +seat: Int
        +date: String
    }

    class TicketBuilder {
        -show: String
        -zone: String
        -seat: Int
        -date: String
        +setShow(show: String): TicketBuilder
        +setZone(zone: String): TicketBuilder
        +setSeat(seat: Int): TicketBuilder
        +setDate(date: String): TicketBuilder
        +buildTicket(): Ticket
    }

    class TicketBookingService {
        +bookTicket(ticket: Ticket): Ticket
    }

    class PlaceCapacity {
        -bookedSeats: Set[(String, String, Int, String)]
        +isAvailable(show: String, zone: String, seat: Int, date: String): Boolean
        +registerTicket(ticket: Ticket): Unit
    }

    TicketBuilder --> Ticket: builds
    TicketBookingService --> Ticket: receives
    TicketBookingService --> PlaceCapacity: validates & registers
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/ticket-booking
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
