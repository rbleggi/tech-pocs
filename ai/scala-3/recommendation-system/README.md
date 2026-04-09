# **Recommendation System**

## Overview

Simple recommendation system demonstrating the Strategy Pattern with two different recommendation algorithms. Supports popularity-based and category-based strategies, enabling flexible item recommendations based on user ratings and preferences.

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

    class RecommendationStrategy {
        <<trait>>
        +recommend(user: User, items: List[Item]): List[Item]
    }

    class PopularityBased {
        +recommend(user: User, items: List[Item]): List[Item]
    }

    class CategoryBased {
        +recommend(user: User, items: List[Item]): List[Item]
    }

    class RecommendationSystem {
        -strategy: RecommendationStrategy
        +setStrategy(strategy: RecommendationStrategy): Unit
        +getRecommendations(user: User, items: List[Item]): List[Item]
    }

    class User {
        +id: String
        +ratings: Map[String, Double]
    }

    class Item {
        +id: String
        +category: String
        +avgRating: Double
    }

    RecommendationStrategy <|-- PopularityBased
    RecommendationStrategy <|-- CategoryBased
    RecommendationSystem --> RecommendationStrategy
    RecommendationSystem --> User
    RecommendationSystem --> Item
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/recommendation-system
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
