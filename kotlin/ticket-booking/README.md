# **Ticket Booking**

## Overview

Ticket booking system demonstrating the **Builder Pattern** for constructing tickets with configurable zones, seats, dates, and shows, while enforcing maximum place capacity and preventing overbooking.

---

## Tech Stack

- **Kotlin 2.2.20** → Modern JVM language with concise syntax and null safety.
- **Gradle** → Build automation tool with Kotlin DSL support.
- **JDK 25** → Required to run the application.
- **kotlin.test** → Testing framework.

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
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
