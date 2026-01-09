package com.rbleggi.recommendationsystem

case class Rating(userId: String, itemId: String, score: Double)
case class Item(id: String, category: String)

trait RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[String]

class RecommendationSystem(strategy: RecommendationStrategy):
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int = 5): List[String] =
    strategy.recommend(userId, ratings, items, topN)

@main def runRecommendationSystem(): Unit =
  val items = List(
    Item("movie1", "action"),
    Item("movie2", "drama"),
    Item("movie3", "comedy"),
    Item("movie4", "action"),
    Item("movie5", "drama"),
    Item("movie6", "action")
  )

  val ratings = List(
    Rating("user1", "movie1", 5.0),
    Rating("user1", "movie2", 3.0),
    Rating("user2", "movie1", 4.0),
    Rating("user2", "movie3", 5.0),
    Rating("user3", "movie2", 5.0),
    Rating("user4", "movie1", 5.0),
    Rating("user4", "movie4", 4.0)
  )

  println("=== Movie Recommendation System ===\n")
