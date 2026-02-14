package com.rbleggi.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationTest {
    private List<Movie> movies;
    private User joao;
    private Map<String, Set<String>> userMovies;

    @BeforeEach
    void setUp() {
        movies = List.of(
            new Movie("m1", "Cidade de Deus", "Drama", 9.5, Set.of("brasileiro", "acao")),
            new Movie("m2", "Tropa de Elite", "Acao", 9.2, Set.of("brasileiro", "policial")),
            new Movie("m3", "Central do Brasil", "Drama", 8.8, Set.of("brasileiro", "drama")),
            new Movie("m4", "O Auto da Compadecida", "Comedia", 9.0, Set.of("brasileiro", "comedia")),
            new Movie("m5", "Que Horas Ela Volta", "Drama", 8.5, Set.of("brasileiro", "social"))
        );

        joao = new User("u1", "Joao Silva", Set.of("m1"), Set.of("Drama", "Acao"));

        userMovies = Map.of(
            "u1", Set.of("m1"),
            "u2", Set.of("m1", "m3", "m5"),
            "u3", Set.of("m2", "m4")
        );
    }

    @Test
    @DisplayName("PopularityStrategy returns movies sorted by popularity")
    void popularityStrategy_sortsbyPopularity() {
        RecommendationSystem system = new RecommendationSystem(new PopularityStrategy());
        List<Recommendation> recommendations = system.getRecommendations(joao, movies, 3);

        assertEquals(3, recommendations.size());
        assertEquals("Tropa de Elite", recommendations.get(0).title());
        assertEquals(9.2, recommendations.get(0).score(), 0.01);
        assertTrue(recommendations.get(0).reason().contains("Popular"));
    }

    @Test
    @DisplayName("PopularityStrategy excludes already watched movies")
    void popularityStrategy_excludesWatchedMovies() {
        RecommendationSystem system = new RecommendationSystem(new PopularityStrategy());
        List<Recommendation> recommendations = system.getRecommendations(joao, movies, 5);

        assertTrue(recommendations.stream().noneMatch(r -> r.movieId().equals("m1")));
    }

    @Test
    @DisplayName("CategoryStrategy recommends movies from favorite categories")
    void categoryStrategy_recommendsFavoriteCategories() {
        RecommendationSystem system = new RecommendationSystem(new CategoryStrategy());
        List<Recommendation> recommendations = system.getRecommendations(joao, movies, 3);

        assertTrue(recommendations.size() > 0);
        for (Recommendation rec : recommendations) {
            Movie movie = movies.stream()
                .filter(m -> m.id().equals(rec.movieId()))
                .findFirst()
                .orElseThrow();
            assertTrue(joao.favoriteCategories().contains(movie.category()));
        }
    }

    @Test
    @DisplayName("CategoryStrategy applies category boost to score")
    void categoryStrategy_appliesCategoryBoost() {
        RecommendationSystem system = new RecommendationSystem(new CategoryStrategy());
        List<Recommendation> recommendations = system.getRecommendations(joao, movies, 1);

        assertTrue(recommendations.get(0).score() > 9.0);
    }

    @Test
    @DisplayName("CollaborativeFilteringStrategy recommends based on similar users")
    void collaborativeFiltering_recommendsSimilarUsers() {
        RecommendationSystem system = new RecommendationSystem(
            new CollaborativeFilteringStrategy(userMovies)
        );
        List<Recommendation> recommendations = system.getRecommendations(joao, movies, 3);

        assertTrue(recommendations.stream()
            .anyMatch(r -> r.reason().contains("similares")));
    }

    @Test
    @DisplayName("RecommendationSystem limits results to requested count")
    void recommendationSystem_limitsResults() {
        RecommendationSystem system = new RecommendationSystem(new PopularityStrategy());
        List<Recommendation> recommendations = system.getRecommendations(joao, movies, 2);

        assertEquals(2, recommendations.size());
    }

    @Test
    @DisplayName("RecommendationSystem getAverageScores returns score map")
    void recommendationSystem_returnsScoreMap() {
        RecommendationSystem system = new RecommendationSystem(new PopularityStrategy());
        Map<String, Double> scores = system.getAverageScores(joao, movies, 3);

        assertEquals(3, scores.size());
        assertTrue(scores.containsKey("m2"));
    }
}
