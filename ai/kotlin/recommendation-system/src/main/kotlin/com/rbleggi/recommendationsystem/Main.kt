package com.rbleggi.recommendationsystem

data class Movie(
    val id: String,
    val title: String,
    val category: String,
    val year: Int,
    val rating: Double,
    val views: Int
)

data class User(
    val id: String,
    val name: String,
    val watchedMovies: List<String>,
    val preferredCategories: List<String>
)

data class Recommendation(
    val movie: Movie,
    val score: Double,
    val reason: String
)

sealed interface RecommendationStrategy {
    fun recommend(user: User, movies: List<Movie>, limit: Int = 5): List<Recommendation>
}

class PopularityBasedStrategy : RecommendationStrategy {
    override fun recommend(user: User, movies: List<Movie>, limit: Int): List<Recommendation> {
        val unwatched = movies.filter { !user.watchedMovies.contains(it.id) }

        return unwatched
            .sortedByDescending { it.views }
            .take(limit)
            .map { movie ->
                val score = (movie.views / 10000.0).coerceIn(0.0, 1.0)
                Recommendation(movie, score, "Popular com ${movie.views} visualizacoes")
            }
    }
}

class CategoryBasedStrategy : RecommendationStrategy {
    override fun recommend(user: User, movies: List<Movie>, limit: Int): List<Recommendation> {
        val unwatched = movies.filter { !user.watchedMovies.contains(it.id) }

        return unwatched
            .filter { user.preferredCategories.contains(it.category) }
            .sortedWith(compareByDescending<Movie> { it.rating }.thenByDescending { it.views })
            .take(limit)
            .map { movie ->
                val categoryMatch = if (user.preferredCategories.contains(movie.category)) 0.3 else 0.0
                val ratingScore = movie.rating / 10.0
                val score = (categoryMatch + ratingScore) / 1.3
                Recommendation(movie, score, "Categoria ${movie.category} - nota ${movie.rating}")
            }
    }
}

class CollaborativeFilteringStrategy : RecommendationStrategy {
    override fun recommend(user: User, movies: List<Movie>, limit: Int): List<Recommendation> {
        val unwatched = movies.filter { !user.watchedMovies.contains(it.id) }

        val watchedCategories = movies
            .filter { user.watchedMovies.contains(it.id) }
            .map { it.category }
            .toSet()

        return unwatched
            .map { movie ->
                val categoryBonus = if (watchedCategories.contains(movie.category)) 0.4 else 0.0
                val ratingScore = movie.rating / 10.0
                val popularityScore = (movie.views / 15000.0).coerceIn(0.0, 0.3)
                val score = (categoryBonus + ratingScore * 0.5 + popularityScore).coerceIn(0.0, 1.0)

                val reason = buildString {
                    append("Baseado em filmes assistidos")
                    if (watchedCategories.contains(movie.category)) {
                        append(" - Categoria similar")
                    }
                }

                Recommendation(movie, score, reason)
            }
            .sortedByDescending { it.score }
            .take(limit)
    }
}

class RecommendationSystem(private val strategy: RecommendationStrategy) {
    fun getRecommendations(user: User, movies: List<Movie>, limit: Int = 5): List<Recommendation> =
        strategy.recommend(user, movies, limit)

    fun getTopRecommendation(user: User, movies: List<Movie>): Recommendation? =
        strategy.recommend(user, movies, 1).firstOrNull()

    fun compareStrategies(user: User, movies: List<Movie>, strategies: List<RecommendationStrategy>): Map<String, List<Recommendation>> {
        return strategies.mapIndexed { index, strat ->
            "Strategy ${index + 1}" to strat.recommend(user, movies, 3)
        }.toMap()
    }
}

fun main() {
    println("Recommendation System")
}
