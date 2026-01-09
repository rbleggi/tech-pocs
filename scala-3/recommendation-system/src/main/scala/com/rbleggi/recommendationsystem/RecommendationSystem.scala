package com.rbleggi.recommendationsystem

case class Rating(userId: String, itemId: String, score: Double)

case class Item(id: String, features: Map[String, Double])

case class Recommendation(itemId: String, score: Double, reason: String)

trait RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int = 5): List[Recommendation]
  def name: String
