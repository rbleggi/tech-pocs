# **Apache Pekko Deep Dive**

## Overview

A deep-dive into Apache Pekko's typed actor model in Scala 3. Demonstrates actor creation, message passing, supervision, and lifecycle management using the Actor Pattern as a foundation for building concurrent and distributed systems.

---

## Tech Stack

- **Language** -> Scala 3
- **Build Tool** -> sbt
- **Testing** -> ScalaTest 3.2.16
- **JDK** -> 25
- **Apache Pekko** -> Typed actor framework (pekko-actor-typed 1.0.2)

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class ActorSystem~T~ {
        +name: String
        +ref: ActorRef[T]
        +terminate(): Unit
    }

    class ActorRef~T~ {
        +tell(msg: T): Unit
        +ask[Res](msg: ActorRef[Res] => T): Future[Res]
    }

    class Behavior~T~ {
        <<sealed trait>>
    }

    class Behaviors {
        +setup[T](factory: ActorContext[T] => Behavior[T]): Behavior[T]
        +receive[T](handler: (ActorContext[T], T) => Behavior[T]): Behavior[T]
        +same[T]: Behavior[T]
    }

    class ActorContext~T~ {
        +self: ActorRef[T]
        +spawn[U](behavior: Behavior[U], name: String): ActorRef[U]
        +log: Logger
    }

    ActorSystem~T~ --> ActorRef~T~
    ActorRef~T~ --> Behavior~T~
    Behaviors --> Behavior~T~
    ActorContext~T~ --> ActorRef~T~
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/apache-pekko-deep-dive
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
