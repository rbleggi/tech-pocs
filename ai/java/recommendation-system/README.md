# **Recommendation System**

## Overview

Movie recommendation system demonstrating the **Strategy Pattern** with multiple recommendation algorithms including popularity-based ranking, category-based filtering, and collaborative filtering using Brazilian movie examples.

---

## Tech Stack

- **Java 25** → Latest JDK with modern language features including records and sealed interfaces.
- **Gradle** → Build automation and dependency management.
- **JUnit 5** → Testing framework for unit tests.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Movie {
        +id: String
        +title: String
        +category: String
        +popularity: double
        +tags: Set~String~
    }

    class User {
        +id: String
        +name: String
        +viewHistory: Set~String~
        +favoriteCategories: Set~String~
    }

    class Recommendation {
        +movieId: String
        +title: String
        +score: double
        +reason: String
    }

    class RecommendationStrategy {
        <<sealed interface>>
        +recommend(user: User, allMovies: List~Movie~, count: int): List~Recommendation~
    }

    class PopularityStrategy {
        +recommend(user: User, allMovies: List~Movie~, count: int): List~Recommendation~
    }

    class CategoryStrategy {
        +recommend(user: User, allMovies: List~Movie~, count: int): List~Recommendation~
    }

    class CollaborativeFilteringStrategy {
        -userMovies: Map~String, Set~String~~
        +recommend(user: User, allMovies: List~Movie~, count: int): List~Recommendation~
    }

    class RecommendationSystem {
        -strategy: RecommendationStrategy
        +getRecommendations(user: User, movies: List~Movie~, count: int): List~Recommendation~
        +getAverageScores(user: User, movies: List~Movie~, count: int): Map~String, Double~
    }

    RecommendationStrategy <|.. PopularityStrategy
    RecommendationStrategy <|.. CategoryStrategy
    RecommendationStrategy <|.. CollaborativeFilteringStrategy
    RecommendationSystem --> RecommendationStrategy
    RecommendationStrategy --> Recommendation
    RecommendationStrategy --> User
    RecommendationStrategy --> Movie
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/recommendation-system
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
