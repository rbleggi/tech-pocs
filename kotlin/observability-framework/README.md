# **Observability Framework (Kotlin)**

## Overview

This project implements a simple and extensible framework for latency observability in Kotlin. It allows you to define different latency calculation strategies and manage multiple metric trackers flexibly.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with advanced type safety and functional programming features.
- **Gradle** → Kotlin's official build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class LatencyStrategy {
        <<interface>>
        +calculateLatency(startTime: Long, endTime: Long): Long
    }

    class MillisecondsLatencyStrategy {
        +calculateLatency(startTime: Long, endTime: Long): Long
    }

    class NanosecondsLatencyStrategy {
        +calculateLatency(startTime: Long, endTime: Long): Long
    }

    class LatencyTracker {
        -latencies: MutableList~Long~
        +recordLatency(startTime: Long, endTime: Long): Unit
        +getAverageLatency(): Double
        +getAllLatencies(): List~Long~
    }

    class MetricManager {
        +getTracker(name: String, strategy: LatencyStrategy): LatencyTracker
        +resetTracker(name: String): Unit
        +getAllTrackerNames(): List~String~
    }

    LatencyStrategy <|-- MillisecondsLatencyStrategy
    LatencyStrategy <|-- NanosecondsLatencyStrategy
    LatencyTracker o-- LatencyStrategy: uses
    MetricManager o-- LatencyTracker: manages
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/observability-framework
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
