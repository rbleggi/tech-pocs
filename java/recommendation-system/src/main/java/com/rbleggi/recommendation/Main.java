package com.rbleggi.recommendation;

import java.util.*;
import java.util.stream.Collectors;

record Movie(String id, String title, String category, double popularity, Set<String> tags) {}

record User(String id, String name, Set<String> viewHistory, Set<String> favoriteCategories) {}

record Recommendation(String movieId, String title, double score, String reason) {}

sealed interface RecommendationStrategy permits PopularityStrategy, CategoryStrategy, CollaborativeFilteringStrategy {
    List<Recommendation> recommend(User user, List<Movie> allMovies, int count);
}

final class PopularityStrategy implements RecommendationStrategy {
    @Override
    public List<Recommendation> recommend(User user, List<Movie> allMovies, int count) {
        return allMovies.stream()
            .filter(movie -> !user.viewHistory().contains(movie.id()))
            .sorted(Comparator.comparingDouble(Movie::popularity).reversed())
            .limit(count)
            .map(movie -> new Recommendation(
                movie.id(),
                movie.title(),
                movie.popularity(),
                "Popular entre todos os usuarios"
            ))
            .toList();
    }
}

final class CategoryStrategy implements RecommendationStrategy {
    @Override
    public List<Recommendation> recommend(User user, List<Movie> allMovies, int count) {
        return allMovies.stream()
            .filter(movie -> !user.viewHistory().contains(movie.id()))
            .filter(movie -> user.favoriteCategories().contains(movie.category()))
            .sorted(Comparator.comparingDouble(Movie::popularity).reversed())
            .limit(count)
            .map(movie -> new Recommendation(
                movie.id(),
                movie.title(),
                movie.popularity() * 1.2,
                "Categoria preferida: " + movie.category()
            ))
            .toList();
    }
}

final class CollaborativeFilteringStrategy implements RecommendationStrategy {
    private final Map<String, Set<String>> userMovies;

    public CollaborativeFilteringStrategy(Map<String, Set<String>> userMovies) {
        this.userMovies = userMovies;
    }

    @Override
    public List<Recommendation> recommend(User user, List<Movie> allMovies, int count) {
        Set<String> similarUsersMovies = findSimilarUsersMovies(user);

        return allMovies.stream()
            .filter(movie -> !user.viewHistory().contains(movie.id()))
            .filter(movie -> similarUsersMovies.contains(movie.id()))
            .map(movie -> {
                long commonCount = similarUsersMovies.stream()
                    .filter(id -> id.equals(movie.id()))
                    .count();
                double score = movie.popularity() + (commonCount * 0.1);
                return new Recommendation(
                    movie.id(),
                    movie.title(),
                    score,
                    "Usuarios similares assistiram"
                );
            })
            .sorted(Comparator.comparingDouble(Recommendation::score).reversed())
            .limit(count)
            .toList();
    }

    private Set<String> findSimilarUsersMovies(User user) {
        return userMovies.entrySet().stream()
            .filter(entry -> !entry.getKey().equals(user.id()))
            .filter(entry -> hasCommonMovies(user.viewHistory(), entry.getValue()))
            .flatMap(entry -> entry.getValue().stream())
            .collect(Collectors.toSet());
    }

    private boolean hasCommonMovies(Set<String> history1, Set<String> history2) {
        return history1.stream().anyMatch(history2::contains);
    }
}

class RecommendationSystem {
    private final RecommendationStrategy strategy;

    public RecommendationSystem(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Recommendation> getRecommendations(User user, List<Movie> movies, int count) {
        return strategy.recommend(user, movies, count);
    }

    public Map<String, Double> getAverageScores(User user, List<Movie> movies, int count) {
        return strategy.recommend(user, movies, count).stream()
            .collect(Collectors.toMap(
                Recommendation::movieId,
                Recommendation::score
            ));
    }
}

public class Main {
    public static void main(String[] args) {
        List<Movie> movies = List.of(
            new Movie("m1", "Cidade de Deus", "Drama", 9.5, Set.of("brasileiro", "acao")),
            new Movie("m2", "Tropa de Elite", "Acao", 9.2, Set.of("brasileiro", "policial")),
            new Movie("m3", "Central do Brasil", "Drama", 8.8, Set.of("brasileiro", "drama")),
            new Movie("m4", "O Auto da Compadecida", "Comedia", 9.0, Set.of("brasileiro", "comedia")),
            new Movie("m5", "Que Horas Ela Volta", "Drama", 8.5, Set.of("brasileiro", "social")),
            new Movie("m6", "Bacurau", "Suspense", 8.7, Set.of("brasileiro", "suspense")),
            new Movie("m7", "Aquarius", "Drama", 8.3, Set.of("brasileiro", "drama")),
            new Movie("m8", "Elite Squad 2", "Acao", 9.1, Set.of("brasileiro", "policial"))
        );

        User joao = new User(
            "u1",
            "Joao Silva",
            Set.of("m1", "m2"),
            Set.of("Acao", "Drama")
        );

        Map<String, Set<String>> allUserMovies = Map.of(
            "u1", Set.of("m1", "m2"),
            "u2", Set.of("m1", "m3", "m5"),
            "u3", Set.of("m2", "m8", "m6")
        );

        System.out.println("=== Sistema de Recomendacao de Filmes ===\n");

        System.out.println("Usuario: " + joao.name());
        System.out.println("Historico: Cidade de Deus, Tropa de Elite\n");

        System.out.println("Estrategia: Popularidade");
        RecommendationSystem popSystem = new RecommendationSystem(new PopularityStrategy());
        List<Recommendation> popRecs = popSystem.getRecommendations(joao, movies, 3);
        for (Recommendation rec : popRecs) {
            System.out.printf("  %s (Score: %.2f) - %s%n", rec.title(), rec.score(), rec.reason());
        }

        System.out.println("\nEstrategia: Categoria");
        RecommendationSystem catSystem = new RecommendationSystem(new CategoryStrategy());
        List<Recommendation> catRecs = catSystem.getRecommendations(joao, movies, 3);
        for (Recommendation rec : catRecs) {
            System.out.printf("  %s (Score: %.2f) - %s%n", rec.title(), rec.score(), rec.reason());
        }

        System.out.println("\nEstrategia: Filtragem Colaborativa");
        RecommendationSystem collabSystem = new RecommendationSystem(
            new CollaborativeFilteringStrategy(allUserMovies)
        );
        List<Recommendation> collabRecs = collabSystem.getRecommendations(joao, movies, 3);
        for (Recommendation rec : collabRecs) {
            System.out.printf("  %s (Score: %.2f) - %s%n", rec.title(), rec.score(), rec.reason());
        }
    }
}
