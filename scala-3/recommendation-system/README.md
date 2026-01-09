# Movie Recommendation System

## Overview

This project implements a recommendation system using collaborative filtering, content-based filtering, and hybrid approaches with the Strategy Pattern. It demonstrates various algorithms for suggesting items based on user preferences and item features.

---

## Tech Stack

- **Scala 3** → Modern JVM-based language with advanced type safety and functional programming features.
- **SBT** → Scala's official build tool.
- **JDK 25** → Required to run the application.

---

## Features

- **User-Based Collaborative Filtering** → Recommends based on similar users
- **Item-Based Collaborative Filtering** → Recommends based on similar items
- **Content-Based Filtering** → Recommends based on item features
- **Hybrid Strategy** → Combines multiple approaches with weighted voting
- **Similarity Metrics** → Cosine similarity and Pearson correlation
- **Strategy Pattern** → Easy to switch between recommendation algorithms

---

## Strategy Pattern

The **Strategy Pattern** allows different recommendation algorithms to be selected at runtime. The system:

- Uses `RecommendationStrategy` as an interface defining the recommendation contract
- Implements multiple concrete strategies: User-Based CF, Item-Based CF, Content-Based, Hybrid
- Allows the `RecommendationSystem` to switch between strategies without changing its code
- Makes it easy to add new recommendation methods by implementing the trait

---

## Recommendation Algorithms

### User-Based Collaborative Filtering
- Finds users similar to the target user
- Uses cosine similarity between user rating vectors
- Recommends items that similar users liked
- **Best for**: Systems with stable user preferences

### Item-Based Collaborative Filtering
- Compares item features to find similar items
- Uses cosine similarity between item feature vectors
- Recommends items similar to what user already rated highly
- **Best for**: Systems with stable item catalog

### Content-Based Filtering
- Builds user profile from rated items
- Matches item features against user profile
- Recommends items matching user's preference pattern
- **Best for**: New items without rating history

### Hybrid Strategy
- Combines multiple strategies with configurable weights
- Aggregates scores from different approaches
- Provides more robust recommendations
- **Best for**: Production systems requiring accuracy

---

## Similarity Metrics

### Cosine Similarity
- Measures angle between two vectors
- Range: [-1, 1], where 1 = identical, 0 = orthogonal
- Formula: `(A · B) / (||A|| * ||B||)`
- Used for both user and item similarity

### Pearson Correlation
- Measures linear correlation between vectors
- Range: [-1, 1], where 1 = perfect correlation
- Accounts for different rating scales
- Used for user similarity in collaborative filtering

---

## Setup Instructions

### 1️ - Clone the Repository

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/recommendation-system
```

### 2️ - Compile & Run the Application

```shell
sbt compile run
```

### 3️ - Run Tests

```shell
sbt test
```

---

## Example Usage

```scala
// Define items with features
val items = List(
  Item("movie1", Map("action" -> 0.8, "drama" -> 0.3)),
  Item("movie2", Map("action" -> 0.2, "drama" -> 0.9)),
  Item("movie3", Map("action" -> 0.1, "comedy" -> 0.9))
)

// User ratings
val ratings = List(
  Rating("user1", "movie1", 5.0),
  Rating("user1", "movie2", 3.0),
  Rating("user2", "movie1", 4.0)
)

// User-based collaborative filtering
val userCF = RecommendationSystem(UserBasedCollaborativeFiltering())
val recs = userCF.recommend("user1", ratings, items, topN = 3)

// Content-based filtering
val contentBased = RecommendationSystem(ContentBasedFiltering())
val cbRecs = contentBased.recommend("user1", ratings, items, topN = 3)

// Hybrid approach
val hybrid = RecommendationSystem(
  HybridRecommendation(
    List(UserBasedCollaborativeFiltering(), ContentBasedFiltering()),
    List(0.7, 0.3)
  )
)
val hybridRecs = hybrid.recommend("user1", ratings, items, topN = 5)
```

---

## Sample Output

```
=== Movie Recommendation System ===

=== User-Based CF ===
movie3: Score 4.50 - Based on similar users
movie4: Score 4.20 - Based on similar users
movie5: Score 3.80 - Based on similar users

=== Item-Based CF ===
movie4: Score 4.35 - Similar to items you liked
movie6: Score 3.90 - Similar to items you liked
movie5: Score 3.75 - Similar to items you liked

=== Content-Based ===
movie4: Score 0.92 - Matches your preferences
movie6: Score 0.78 - Matches your preferences
movie5: Score 0.65 - Matches your preferences

=== Hybrid Strategy ===
movie4: Score 3.62
movie3: Score 2.70
movie6: Score 2.43
```
