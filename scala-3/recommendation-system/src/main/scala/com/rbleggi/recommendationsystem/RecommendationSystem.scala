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

  def pearsonCorrelation(v1: Map[String, Double], v2: Map[String, Double]): Double =
    val commonKeys = v1.keys.toSet.intersect(v2.keys.toSet)
    if commonKeys.size < 2 then return 0.0

    val vals1 = commonKeys.map(v1).toList
    val vals2 = commonKeys.map(v2).toList

    val mean1 = vals1.sum / vals1.length
    val mean2 = vals2.sum / vals2.length

    val numerator = vals1.zip(vals2).map { case (a, b) => (a - mean1) * (b - mean2) }.sum
    val denom1 = Math.sqrt(vals1.map(a => Math.pow(a - mean1, 2)).sum)
    val denom2 = Math.sqrt(vals2.map(b => Math.pow(b - mean2, 2)).sum)

    if denom1 == 0.0 || denom2 == 0.0 then 0.0
    else numerator / (denom1 * denom2)

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
