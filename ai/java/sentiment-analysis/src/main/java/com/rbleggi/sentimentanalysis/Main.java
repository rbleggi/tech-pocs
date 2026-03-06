package com.rbleggi.sentimentanalysis;

import java.util.*;
import java.util.stream.Collectors;

record Review(String id, String text, String product) {}

enum Sentiment {
    POSITIVE, NEGATIVE, NEUTRAL
}

record SentimentResult(String reviewId, Sentiment sentiment, double score, String method) {}

sealed interface SentimentStrategy permits LexiconStrategy, RuleBasedStrategy, HybridStrategy {
    SentimentResult analyze(Review review);
}

final class LexiconStrategy implements SentimentStrategy {
    private final Map<String, Double> positiveWords;
    private final Map<String, Double> negativeWords;

    public LexiconStrategy() {
        this.positiveWords = Map.of(
            "good", 1.0, "great", 2.0, "excellent", 2.5, "perfect", 2.5,
            "loved", 2.0, "wonderful", 2.5, "recommend", 1.5,
            "satisfied", 1.5, "quality", 1.0, "fast", 1.0
        );
        this.negativeWords = Map.of(
            "bad", -1.0, "terrible", -2.0, "horrible", -2.5, "awful", -2.5,
            "hated", -2.0, "disappointed", -1.5, "problem", -1.0,
            "defect", -1.5, "slow", -1.0, "expensive", -0.5
        );
    }

    @Override
    public SentimentResult analyze(Review review) {
        String[] words = review.text().toLowerCase().split("\\s+");
        double score = 0.0;

        for (String word : words) {
            word = word.replaceAll("[^a-z]", "");
            score += positiveWords.getOrDefault(word, 0.0);
            score += negativeWords.getOrDefault(word, 0.0);
        }

        Sentiment sentiment;
        if (score > 1.0) {
            sentiment = Sentiment.POSITIVE;
        } else if (score < -1.0) {
            sentiment = Sentiment.NEGATIVE;
        } else {
            sentiment = Sentiment.NEUTRAL;
        }

        return new SentimentResult(review.id(), sentiment, score, "Lexicon-based");
    }
}

final class RuleBasedStrategy implements SentimentStrategy {
    @Override
    public SentimentResult analyze(Review review) {
        String text = review.text().toLowerCase();
        double score = 0.0;

        if (text.contains("not")) {
            if (text.contains("good") || text.contains("great")) {
                score -= 2.0;
            }
            if (text.contains("bad") || text.contains("terrible")) {
                score += 1.5;
            }
        }

        if (text.contains("but") || text.contains("however")) {
            String[] parts = text.split("but|however");
            if (parts.length > 1) {
                String afterBut = parts[parts.length - 1];
                if (afterBut.contains("good") || afterBut.contains("great")) {
                    score += 2.0;
                } else if (afterBut.contains("bad") || afterBut.contains("terrible")) {
                    score -= 2.0;
                }
            }
        }

        if (text.contains("!!") || text.contains("!!!")) {
            if (score > 0) score *= 1.5;
            else if (score < 0) score *= 1.5;
        }

        if (text.matches(".*[A-Z]{3,}.*")) {
            if (score > 0) score *= 1.3;
            else if (score < 0) score *= 1.3;
        }

        Sentiment sentiment;
        if (score > 0.5) {
            sentiment = Sentiment.POSITIVE;
        } else if (score < -0.5) {
            sentiment = Sentiment.NEGATIVE;
        } else {
            sentiment = Sentiment.NEUTRAL;
        }

        return new SentimentResult(review.id(), sentiment, score, "Rule-based");
    }
}

final class HybridStrategy implements SentimentStrategy {
    private final LexiconStrategy lexicon;
    private final RuleBasedStrategy ruleBased;

    public HybridStrategy() {
        this.lexicon = new LexiconStrategy();
        this.ruleBased = new RuleBasedStrategy();
    }

    @Override
    public SentimentResult analyze(Review review) {
        SentimentResult lexiconResult = lexicon.analyze(review);
        SentimentResult ruleResult = ruleBased.analyze(review);

        double combinedScore = (lexiconResult.score() * 0.6) + (ruleResult.score() * 0.4);

        Sentiment sentiment;
        if (combinedScore > 1.0) {
            sentiment = Sentiment.POSITIVE;
        } else if (combinedScore < -1.0) {
            sentiment = Sentiment.NEGATIVE;
        } else {
            sentiment = Sentiment.NEUTRAL;
        }

        return new SentimentResult(review.id(), sentiment, combinedScore, "Hybrid");
    }
}

class SentimentAnalysisSystem {
    private final SentimentStrategy strategy;

    public SentimentAnalysisSystem(SentimentStrategy strategy) {
        this.strategy = strategy;
    }

    public SentimentResult analyze(Review review) {
        return strategy.analyze(review);
    }

    public List<SentimentResult> analyzeBatch(List<Review> reviews) {
        return reviews.stream()
            .map(this::analyze)
            .toList();
    }

    public Map<Sentiment, Long> getSentimentDistribution(List<Review> reviews) {
        return analyzeBatch(reviews).stream()
            .collect(Collectors.groupingBy(SentimentResult::sentiment, Collectors.counting()));
    }

    public double getAverageSentiment(List<Review> reviews) {
        return analyzeBatch(reviews).stream()
            .mapToDouble(SentimentResult::score)
            .average()
            .orElse(0.0);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Sentiment Analysis");
    }
}
