# **Ticket Booking (Kotlin)**

## Overview

This project implements a flexible and safe ticket booking system in Kotlin. Users can choose zones, dates, seats, and shows, while the system enforces maximum place capacity and prevents overbooking.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build automation tool for Kotlin projects.
- **JDK 25** → Required to run the application.

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
        -bookedSeats: Set~Tuple4~
        +isAvailable(show: String, zone: String, seat: Int, date: String): Boolean
        +registerTicket(ticket: Ticket): Unit
        +clear(): Unit
    }

    TicketBuilder --> Ticket: builds
    TicketBookingService --> Ticket: receives
    TicketBookingService --> PlaceCapacity: validates & registers

```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/ticket-booking
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```