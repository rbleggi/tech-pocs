package com.rbleggi.recommendationsystem

case class Rating(userId: String, itemId: String, score: Double)

case class Item(id: String, features: Map[String, Double])

case class Recommendation(itemId: String, score: Double, reason: String)

trait RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int = 5): List[Recommendation]
  def name: String

object SimilarityMetrics:
  def cosineSimilarity(v1: Map[String, Double], v2: Map[String, Double]): Double =
    val allKeys = (v1.keys ++ v2.keys).toSet
    val dotProduct = allKeys.map(k => v1.getOrElse(k, 0.0) * v2.getOrElse(k, 0.0)).sum
    val mag1 = Math.sqrt(allKeys.map(k => Math.pow(v1.getOrElse(k, 0.0), 2)).sum)
    val mag2 = Math.sqrt(allKeys.map(k => Math.pow(v2.getOrElse(k, 0.0), 2)).sum)

    if mag1 == 0.0 || mag2 == 0.0 then 0.0
    else dotProduct / (mag1 * mag2)

class RecommendationSystem(strategy: RecommendationStrategy):
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int = 5): List[Recommendation] =
    strategy.recommend(userId, ratings, items, topN)

  def getStrategyName: String = strategy.name

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
