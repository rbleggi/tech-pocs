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

class UserBasedCollaborativeFiltering extends RecommendationStrategy:
  override def name: String = "User-Based Collaborative Filtering"

  override def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[Recommendation] =
    val userRatings = ratings.filter(_.userId == userId).map(r => r.itemId -> r.score).toMap
    val otherUsers = ratings.filter(_.userId != userId).map(_.userId).distinct

    val userSimilarities = otherUsers.map { otherUser =>
      val otherRatings = ratings.filter(_.userId == otherUser).map(r => r.itemId -> r.score).toMap
      val similarity = SimilarityMetrics.cosineSimilarity(userRatings, otherRatings)
      otherUser -> similarity
    }.toMap

    val candidateItems = items.filterNot(item => userRatings.contains(item.id))

    candidateItems.flatMap { item =>
      val ratingsForItem = ratings.filter(r => r.itemId == item.id && r.userId != userId)
      if ratingsForItem.isEmpty then None
      else
        val weightedSum = ratingsForItem.map { r =>
          r.score * userSimilarities.getOrElse(r.userId, 0.0)
        }.sum
        val similaritySum = ratingsForItem.map(r => Math.abs(userSimilarities.getOrElse(r.userId, 0.0))).sum

        if similaritySum == 0.0 then None
        else Some(Recommendation(item.id, weightedSum / similaritySum, s"Based on similar users"))
    }.sortBy(-_.score).take(topN)

class ItemBasedCollaborativeFiltering extends RecommendationStrategy:
  override def name: String = "Item-Based Collaborative Filtering"

  override def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[Recommendation] =
    val userRatings = ratings.filter(_.userId == userId)
    if userRatings.isEmpty then return List.empty

    val candidateItems = items.filterNot(item => userRatings.exists(_.itemId == item.id))

    candidateItems.flatMap { candidateItem =>
      val similarities = userRatings.map { userRating =>
        val ratedItem = items.find(_.id == userRating.itemId).get
        val similarity = SimilarityMetrics.cosineSimilarity(candidateItem.features, ratedItem.features)
        (userRating.score, similarity)
      }

      val weightedSum = similarities.map { case (rating, sim) => rating * sim }.sum
      val similaritySum = similarities.map { case (_, sim) => Math.abs(sim) }.sum

      if similaritySum == 0.0 then None
      else Some(Recommendation(candidateItem.id, weightedSum / similaritySum, s"Similar to items you liked"))
    }.sortBy(-_.score).take(topN)

class ContentBasedFiltering extends RecommendationStrategy:
  override def name: String = "Content-Based Filtering"

  override def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[Recommendation] =
    val userRatings = ratings.filter(_.userId == userId)
    if userRatings.isEmpty then return List.empty

    val userProfile = buildUserProfile(userRatings, items)
    val candidateItems = items.filterNot(item => userRatings.exists(_.itemId == item.id))

    candidateItems.map { item =>
      val similarity = SimilarityMetrics.cosineSimilarity(userProfile, item.features)
      Recommendation(item.id, similarity, s"Matches your preferences")
    }.sortBy(-_.score).take(topN)

  private def buildUserProfile(userRatings: List[Rating], items: List[Item]): Map[String, Double] =
    val weightedFeatures = userRatings.flatMap { rating =>
      items.find(_.id == rating.itemId).map { item =>
        item.features.map { case (feature, value) => (feature, value * rating.score) }
      }
    }.flatten

    weightedFeatures.groupBy(_._1).map { case (feature, values) =>
      feature -> (values.map(_._2).sum / userRatings.length)
    }

class HybridRecommendation(strategies: List[RecommendationStrategy], weights: List[Double]) extends RecommendationStrategy:
  require(strategies.length == weights.length, "Strategies and weights must have same length")
  require(weights.sum == 1.0, "Weights must sum to 1.0")

  override def name: String = "Hybrid Recommendation"

  override def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[Recommendation] =
    val allRecommendations = strategies.zip(weights).flatMap { case (strategy, weight) =>
      strategy.recommend(userId, ratings, items, topN * 2).map(r => r.copy(score = r.score * weight))
    }

    allRecommendations
      .groupBy(_.itemId)
      .map { case (itemId, recs) =>
        Recommendation(itemId, recs.map(_.score).sum, s"Hybrid: ${recs.map(_.reason).mkString(", ")}")
      }
      .toList
      .sortBy(-_.score)
      .take(topN)
