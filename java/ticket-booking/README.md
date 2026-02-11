# **Ticket Booking**

## Overview

This project implements a flexible and safe ticket booking system using the Builder Pattern. Users can choose zones, dates, seats, and shows, while the system enforces maximum place capacity and prevents overbooking. The entire implementation is contained in a single file for simplicity.

---

## Tech Stack

- **Java 25** → Modern Java with records and enhanced features.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class TicketBookingSystem {
        +main(args: String[]): void
    }

    class Ticket {
        +show: String
        +zone: String
        +seat: int
        +date: String
    }

    class TicketBuilder {
        -show: String
        -zone: String
        -seat: int
        -date: String
        +setShow(show: String): TicketBuilder
        +setZone(zone: String): TicketBuilder
        +setSeat(seat: int): TicketBuilder
        +setDate(date: String): TicketBuilder
        +buildTicket(): Ticket
    }

    class TicketBookingService {
        +bookTicket(ticket: Ticket): Ticket
    }

    class PlaceCapacity {
        -bookedSeats: Set<BookedSeat>
        +isAvailable(show: String, zone: String, seat: int, date: String): boolean
        +registerTicket(ticket: Ticket): void
        +clear(): void
    }

    TicketBookingSystem --> TicketBuilder
    TicketBookingSystem --> TicketBookingService
    TicketBookingSystem --> Ticket
    TicketBuilder --> Ticket: builds
    TicketBookingService --> Ticket: receives
    TicketBookingService --> PlaceCapacity: validates & registers
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/ticket-booking
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
