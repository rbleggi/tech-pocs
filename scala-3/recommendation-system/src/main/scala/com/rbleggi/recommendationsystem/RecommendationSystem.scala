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
