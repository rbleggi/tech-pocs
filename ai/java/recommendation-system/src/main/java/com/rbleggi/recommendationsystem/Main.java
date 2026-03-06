package com.rbleggi.recommendationsystem;

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
                "Popular among all users"
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
        System.out.println("Recommendation System");
    }
}
