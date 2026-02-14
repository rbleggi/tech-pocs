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
    val movies = listOf(
        Movie("m1", "Cidade de Deus", "Drama", 2002, 8.6, 12500),
        Movie("m2", "Tropa de Elite", "Acao", 2007, 8.0, 15000),
        Movie("m3", "Central do Brasil", "Drama", 1998, 8.0, 8000),
        Movie("m4", "O Auto da Compadecida", "Comedia", 2000, 8.7, 18000),
        Movie("m5", "Bacurau", "Drama", 2019, 7.5, 9500),
        Movie("m6", "Que Horas Ela Volta", "Drama", 2015, 7.7, 7000),
        Movie("m7", "Aquarius", "Drama", 2016, 7.6, 6500),
        Movie("m8", "Elite Squad 2", "Acao", 2010, 8.0, 14000),
        Movie("m9", "Meu Nome Nao e Johnny", "Drama", 2008, 7.2, 8500),
        Movie("m10", "Carandiru", "Drama", 2003, 7.6, 10000)
    )

    val user = User(
        id = "u1",
        name = "Joao Silva",
        watchedMovies = listOf("m1", "m3"),
        preferredCategories = listOf("Drama", "Comedia")
    )

    println("=== Sistema de Recomendacao de Filmes ===\n")

    println("Usuario: ${user.name}")
    println("Filmes assistidos: ${user.watchedMovies.size}")
    println("Categorias preferidas: ${user.preferredCategories.joinToString(", ")}\n")

    println("--- Estrategia: Popularidade ---")
    val popularitySystem = RecommendationSystem(PopularityBasedStrategy())
    val popularRecs = popularitySystem.getRecommendations(user, movies, 3)
    popularRecs.forEach { rec ->
        println("  ${rec.movie.title} (${rec.movie.year})")
        println("    Score: %.2f | ${rec.reason}".format(rec.score))
    }

    println("\n--- Estrategia: Categoria ---")
    val categorySystem = RecommendationSystem(CategoryBasedStrategy())
    val categoryRecs = categorySystem.getRecommendations(user, movies, 3)
    categoryRecs.forEach { rec ->
        println("  ${rec.movie.title} (${rec.movie.year})")
        println("    Score: %.2f | ${rec.reason}".format(rec.score))
    }

    println("\n--- Estrategia: Filtragem Colaborativa ---")
    val collaborativeSystem = RecommendationSystem(CollaborativeFilteringStrategy())
    val collaborativeRecs = collaborativeSystem.getRecommendations(user, movies, 3)
    collaborativeRecs.forEach { rec ->
        println("  ${rec.movie.title} (${rec.movie.year})")
        println("    Score: %.2f | ${rec.reason}".format(rec.score))
    }

    println("\n--- Top Recomendacao (Colaborativa) ---")
    val topRec = collaborativeSystem.getTopRecommendation(user, movies)
    topRec?.let {
        println("  ${it.movie.title}")
        println("  Score: %.2f".format(it.score))
        println("  Razao: ${it.reason}")
    }
}
