# Recommendation System

## Overview

Simple recommendation system demonstrating the Strategy Pattern with two different recommendation algorithms.

## Tech Stack

- **Scala 3** → Modern JVM-based language
- **SBT** → Scala's build tool
- **JDK 25** → Required to run

## Features

- **Popularity-Based** → Recommends items with highest average ratings
- **Category-Based** → Recommends items from user's favorite category
- **Strategy Pattern** → Easy to switch between algorithms

## Strategy Pattern

The **Strategy Pattern** allows different recommendation algorithms to be selected at runtime:

- `RecommendationStrategy` trait defines the recommendation contract
- `PopularityBased` and `CategoryBased` implement different strategies
- `RecommendationSystem` switches between strategies without changing code

## Algorithms

### Popularity-Based
Recommends items with highest average ratings from other users.

### Category-Based
Finds user's favorite category and recommends other items from that category.

## Setup

```shell
cd scala-3/recommendation-system
sbt compile run
sbt test
```
