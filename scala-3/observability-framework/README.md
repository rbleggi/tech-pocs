# **Observability Framework**

## Overview

This project implements a simple and extensible framework for latency observability using the Strategy Pattern in Scala. It allows you to define different latency calculation strategies and manage multiple metric trackers flexibly.

---

## Tech Stack

- **Language** -> Scala 3.6.3
- **Build Tool** -> sbt 1.10.11
- **Runtime** -> JDK 25
- **Testing** -> ScalaTest 3.2.16

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class LatencyStrategy {
        <<trait>>
        +calculateLatency(startTime: Long, endTime: Long): Long
    }

    class MillisecondsLatencyStrategy {
        +calculateLatency(startTime: Long, endTime: Long): Long
    }

    class NanosecondsLatencyStrategy {
        +calculateLatency(startTime: Long, endTime: Long): Long
    }

    class LatencyTracker {
        -latencies: ListBuffer[Long]
        +recordLatency(startTime: Long, endTime: Long): Unit
        +getAverageLatency: Double
        +getAllLatencies: List[Long]
    }

    class MetricManager {
        +getTracker(name: String, strategy: LatencyStrategy): LatencyTracker
        +resetTracker(name: String): Unit
        +getAllTrackerNames: List[String]
    }

    LatencyStrategy <|-- MillisecondsLatencyStrategy
    LatencyStrategy <|-- NanosecondsLatencyStrategy
    LatencyTracker o-- LatencyStrategy: uses
    MetricManager o-- LatencyTracker: manages
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/observability-framework
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
