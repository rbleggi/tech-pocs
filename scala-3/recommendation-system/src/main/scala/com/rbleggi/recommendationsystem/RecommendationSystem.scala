package com.rbleggi.recommendationsystem

case class Rating(userId: String, itemId: String, score: Double)

case class Item(id: String, features: Map[String, Double])

trait RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[String]

class RecommendationSystem(strategy: RecommendationStrategy):
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int = 5): List[String] =
    strategy.recommend(userId, ratings, items, topN)

@main def runRecommendationSystem(): Unit =
  val items = List(
    Item("movie1", Map("action" -> 0.8, "drama" -> 0.3, "comedy" -> 0.1)),
    Item("movie2", Map("action" -> 0.2, "drama" -> 0.9, "comedy" -> 0.1)),
    Item("movie3", Map("action" -> 0.1, "drama" -> 0.2, "comedy" -> 0.9)),
    Item("movie4", Map("action" -> 0.9, "drama" -> 0.1, "comedy" -> 0.2)),
    Item("movie5", Map("action" -> 0.3, "drama" -> 0.7, "comedy" -> 0.5)),
    Item("movie6", Map("action" -> 0.6, "drama" -> 0.4, "comedy" -> 0.3))
  )

  val ratings = List(
    Rating("user1", "movie1", 5.0),
    Rating("user1", "movie2", 3.0),
    Rating("user2", "movie1", 4.0),
    Rating("user2", "movie3", 5.0),
    Rating("user3", "movie2", 5.0),
    Rating("user3", "movie3", 2.0),
    Rating("user4", "movie1", 5.0),
    Rating("user4", "movie4", 4.0)
  )

  println("=== Movie Recommendation System ===\n")
