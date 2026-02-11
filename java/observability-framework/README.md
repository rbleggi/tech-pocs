# **Observability Framework**

## Overview

This project implements a simple and extensible framework for latency observability using the Strategy Pattern in Java. It allows you to define different latency calculation strategies and manage multiple metric trackers flexibly.

---

## Tech Stack

- **Java 25** → Modern Java with enhanced features.
- **Gradle** → Build tool.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class LatencyStrategy {
        <<interface>>
        +calculateLatency(startTime: long, endTime: long): long
    }

    class MillisecondsLatencyStrategy {
        +calculateLatency(startTime: long, endTime: long): long
    }

    class NanosecondsLatencyStrategy {
        +calculateLatency(startTime: long, endTime: long): long
    }

    class LatencyTracker {
        -latencies: List~Long~
        +recordLatency(startTime: long, endTime: long): void
        +getAverageLatency(): double
        +getAllLatencies(): List~Long~
    }

    class MetricManager {
        +getTracker(name: String, strategy: LatencyStrategy): LatencyTracker
        +resetTracker(name: String): void
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
cd java/observability-framework
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```
