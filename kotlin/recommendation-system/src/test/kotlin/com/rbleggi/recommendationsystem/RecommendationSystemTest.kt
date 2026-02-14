package com.rbleggi.recommendationsystem

import kotlin.test.*

class RecommendationSystemTest {

    private val movies = listOf(
        Movie("m1", "Cidade de Deus", "Drama", 2002, 8.6, 12500),
        Movie("m2", "Tropa de Elite", "Acao", 2007, 8.0, 15000),
        Movie("m3", "Central do Brasil", "Drama", 1998, 8.0, 8000),
        Movie("m4", "O Auto da Compadecida", "Comedia", 2000, 8.7, 18000),
        Movie("m5", "Bacurau", "Drama", 2019, 7.5, 9500)
    )

    private val user = User(
        id = "u1",
        name = "Joao Silva",
        watchedMovies = listOf("m1", "m3"),
        preferredCategories = listOf("Drama", "Comedia")
    )

    @Test
    fun `PopularityBasedStrategy recommends most viewed movies`() {
        val strategy = PopularityBasedStrategy()
        val recommendations = strategy.recommend(user, movies, 3)

        assertEquals(3, recommendations.size)
        assertEquals("O Auto da Compadecida", recommendations[0].movie.title)
        assertTrue(recommendations[0].score > 0.0)
    }

    @Test
    fun `PopularityBasedStrategy excludes watched movies`() {
        val strategy = PopularityBasedStrategy()
        val recommendations = strategy.recommend(user, movies, 5)

        assertFalse(recommendations.any { it.movie.id == "m1" })
        assertFalse(recommendations.any { it.movie.id == "m3" })
    }

    @Test
    fun `PopularityBasedStrategy orders by views descending`() {
        val strategy = PopularityBasedStrategy()
        val recommendations = strategy.recommend(user, movies, 3)

        assertTrue(recommendations[0].movie.views >= recommendations[1].movie.views)
        assertTrue(recommendations[1].movie.views >= recommendations[2].movie.views)
    }

    @Test
    fun `CategoryBasedStrategy recommends preferred categories`() {
        val strategy = CategoryBasedStrategy()
        val recommendations = strategy.recommend(user, movies, 3)

        assertTrue(recommendations.all {
            user.preferredCategories.contains(it.movie.category)
        })
    }

    @Test
    fun `CategoryBasedStrategy excludes watched movies`() {
        val strategy = CategoryBasedStrategy()
        val recommendations = strategy.recommend(user, movies, 5)

        assertFalse(recommendations.any { it.movie.id == "m1" })
        assertFalse(recommendations.any { it.movie.id == "m3" })
    }

    @Test
    fun `CategoryBasedStrategy prioritizes rating and views`() {
        val strategy = CategoryBasedStrategy()
        val recommendations = strategy.recommend(user, movies, 2)

        assertTrue(recommendations.isNotEmpty())
        assertTrue(recommendations[0].movie.rating >= 7.0)
    }

    @Test
    fun `CollaborativeFilteringStrategy recommends based on watched categories`() {
        val strategy = CollaborativeFilteringStrategy()
        val recommendations = strategy.recommend(user, movies, 3)

        assertTrue(recommendations.isNotEmpty())
        assertTrue(recommendations.any { it.movie.category == "Drama" })
    }

    @Test
    fun `CollaborativeFilteringStrategy excludes watched movies`() {
        val strategy = CollaborativeFilteringStrategy()
        val recommendations = strategy.recommend(user, movies, 5)

        assertFalse(recommendations.any { it.movie.id == "m1" })
        assertFalse(recommendations.any { it.movie.id == "m3" })
    }

    @Test
    fun `CollaborativeFilteringStrategy combines multiple factors`() {
        val strategy = CollaborativeFilteringStrategy()
        val recommendations = strategy.recommend(user, movies, 1)

        assertTrue(recommendations.isNotEmpty())
        assertTrue(recommendations[0].score > 0.0)
        assertTrue(recommendations[0].reason.contains("Baseado em filmes assistidos"))
    }

    @Test
    fun `RecommendationSystem returns recommendations with limit`() {
        val system = RecommendationSystem(PopularityBasedStrategy())
        val recommendations = system.getRecommendations(user, movies, 2)

        assertEquals(2, recommendations.size)
    }

    @Test
    fun `RecommendationSystem returns top recommendation`() {
        val system = RecommendationSystem(CollaborativeFilteringStrategy())
        val topRec = system.getTopRecommendation(user, movies)

        assertNotNull(topRec)
        assertTrue(topRec.score > 0.0)
    }

    @Test
    fun `RecommendationSystem compares multiple strategies`() {
        val system = RecommendationSystem(PopularityBasedStrategy())
        val strategies = listOf(
            PopularityBasedStrategy(),
            CategoryBasedStrategy(),
            CollaborativeFilteringStrategy()
        )

        val comparison = system.compareStrategies(user, movies, strategies)

        assertEquals(3, comparison.size)
        assertTrue(comparison.all { it.value.size <= 3 })
    }

    @Test
    fun `Movie data class stores all fields correctly`() {
        val movie = Movie("m1", "Teste", "Drama", 2020, 8.5, 10000)

        assertEquals("m1", movie.id)
        assertEquals("Teste", movie.title)
        assertEquals("Drama", movie.category)
        assertEquals(2020, movie.year)
        assertEquals(8.5, movie.rating)
        assertEquals(10000, movie.views)
    }

    @Test
    fun `User data class stores all fields correctly`() {
        val testUser = User("u1", "Maria", listOf("m1"), listOf("Drama"))

        assertEquals("u1", testUser.id)
        assertEquals("Maria", testUser.name)
        assertEquals(1, testUser.watchedMovies.size)
        assertEquals(1, testUser.preferredCategories.size)
    }

    @Test
    fun `Recommendation contains movie, score and reason`() {
        val movie = Movie("m1", "Teste", "Drama", 2020, 8.5, 10000)
        val rec = Recommendation(movie, 0.85, "Teste")

        assertEquals(movie, rec.movie)
        assertEquals(0.85, rec.score)
        assertEquals("Teste", rec.reason)
    }

    @Test
    fun `PopularityBasedStrategy handles empty watched list`() {
        val newUser = User("u2", "Ana", emptyList(), listOf("Drama"))
        val strategy = PopularityBasedStrategy()
        val recommendations = strategy.recommend(newUser, movies, 3)

        assertEquals(3, recommendations.size)
    }

    @Test
    fun `CategoryBasedStrategy handles no preferred categories`() {
        val newUser = User("u2", "Carlos", emptyList(), emptyList())
        val strategy = CategoryBasedStrategy()
        val recommendations = strategy.recommend(newUser, movies, 3)

        assertEquals(0, recommendations.size)
    }

    @Test
    fun `CollaborativeFilteringStrategy handles new user`() {
        val newUser = User("u2", "Pedro", emptyList(), emptyList())
        val strategy = CollaborativeFilteringStrategy()
        val recommendations = strategy.recommend(newUser, movies, 3)

        assertTrue(recommendations.size <= 3)
    }
}
