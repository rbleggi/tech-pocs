# **Movie Recommendation System**

## Overview

Movie recommendation system demonstrating the **Strategy Pattern** with three different recommendation algorithms: popularity-based, category-based, and collaborative filtering using Brazilian movies and Portuguese content.

---

## Tech Stack

- **Kotlin 2.1.10** → Modern JVM language with concise syntax and null safety
- **Gradle** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Movie {
        +id: String
        +title: String
        +category: String
        +year: Int
        +rating: Double
        +views: Int
    }

    class User {
        +id: String
        +name: String
        +watchedMovies: List~String~
        +preferredCategories: List~String~
    }

    class Recommendation {
        +movie: Movie
        +score: Double
        +reason: String
    }

    class RecommendationStrategy {
        <<interface>>
        +recommend(user: User, movies: List~Movie~, limit: Int): List~Recommendation~
    }

    class PopularityBasedStrategy {
        +recommend(user: User, movies: List~Movie~, limit: Int): List~Recommendation~
    }

    class CategoryBasedStrategy {
        +recommend(user: User, movies: List~Movie~, limit: Int): List~Recommendation~
    }

    class CollaborativeFilteringStrategy {
        +recommend(user: User, movies: List~Movie~, limit: Int): List~Recommendation~
    }

    class RecommendationSystem {
        -strategy: RecommendationStrategy
        +getRecommendations(user: User, movies: List~Movie~, limit: Int): List~Recommendation~
        +getTopRecommendation(user: User, movies: List~Movie~): Recommendation?
        +compareStrategies(user: User, movies: List~Movie~, strategies: List~RecommendationStrategy~): Map~String, List~Recommendation~~
    }

    RecommendationStrategy <|.. PopularityBasedStrategy
    RecommendationStrategy <|.. CategoryBasedStrategy
    RecommendationStrategy <|.. CollaborativeFilteringStrategy
    RecommendationSystem --> RecommendationStrategy
    RecommendationStrategy --> Recommendation
    Recommendation --> Movie
    RecommendationStrategy --> User
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/recommendation-system
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run the Application
```bash
./gradlew run
```

### 4 - Run Tests
```bash
./gradlew test
```
