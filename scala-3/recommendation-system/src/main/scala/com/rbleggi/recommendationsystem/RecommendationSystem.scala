package com.rbleggi.recommendationsystem

case class Rating(userId: String, itemId: String, score: Double)
case class Item(id: String, category: String)

trait RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[String]

class PopularityBased extends RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[String] =
    val userRated = ratings.filter(_.userId == userId).map(_.itemId).toSet
    ratings
      .filterNot(r => userRated.contains(r.itemId))
      .groupBy(_.itemId)
      .map { case (itemId, rs) => (itemId, rs.map(_.score).sum / rs.size) }
      .toList
      .sortBy(-_._2)
      .take(topN)
      .map(_._1)

class CategoryBased extends RecommendationStrategy:
  def recommend(userId: String, ratings: List[Rating], items: List[Item], topN: Int): List[String] =
    val userRatings = ratings.filter(_.userId == userId)
    if userRatings.isEmpty then return List.empty

    val favoriteCategory = userRatings
      .flatMap(r => items.find(_.id == r.itemId).map(i => (i.category, r.score)))
      .groupBy(_._1)
      .map { case (cat, scores) => (cat, scores.map(_._2).sum) }
      .maxBy(_._2)
      ._1

    val userRated = userRatings.map(_.itemId).toSet
    items
      .filter(i => i.category == favoriteCategory && !userRated.contains(i.id))
      .map(_.id)
      .take(topN)

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

  println("=== Recommendation System ===\n")

  val popularitySystem = RecommendationSystem(PopularityBased())
  println("Popularity-Based:")
  println(popularitySystem.recommend("user1", ratings, items, 3).mkString(", "))

  val categorySystem = RecommendationSystem(CategoryBased())
  println("\nCategory-Based:")
  println(categorySystem.recommend("user1", ratings, items, 3).mkString(", "))
