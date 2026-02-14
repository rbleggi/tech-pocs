package com.rbleggi.sentiment;

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
            "bom", 1.0, "otimo", 2.0, "excelente", 2.5, "perfeito", 2.5,
            "adorei", 2.0, "maravilhoso", 2.5, "recomendo", 1.5,
            "satisfeito", 1.5, "qualidade", 1.0, "rapido", 1.0
        );
        this.negativeWords = Map.of(
            "ruim", -1.0, "pessimo", -2.0, "horrivel", -2.5, "terrivel", -2.5,
            "odiei", -2.0, "decepcionado", -1.5, "problema", -1.0,
            "defeito", -1.5, "lento", -1.0, "caro", -0.5
        );
    }

    @Override
    public SentimentResult analyze(Review review) {
        String[] words = review.text().toLowerCase().split("\\s+");
        double score = 0.0;

        for (String word : words) {
            word = word.replaceAll("[^a-záàâãéèêíïóôõöúçñ]", "");
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

        if (text.contains("não") || text.contains("nao")) {
            if (text.contains("bom") || text.contains("otimo")) {
                score -= 2.0;
            }
            if (text.contains("ruim") || text.contains("pessimo")) {
                score += 1.5;
            }
        }

        if (text.contains("mas") || text.contains("porem") || text.contains("porém")) {
            String[] parts = text.split("mas|porem|porém");
            if (parts.length > 1) {
                String afterBut = parts[parts.length - 1];
                if (afterBut.contains("bom") || afterBut.contains("otimo")) {
                    score += 2.0;
                } else if (afterBut.contains("ruim") || afterBut.contains("pessimo")) {
                    score -= 2.0;
                }
            }
        }

        if (text.contains("!!") || text.contains("!!!")) {
            if (score > 0) score *= 1.5;
            else if (score < 0) score *= 1.5;
        }

        if (text.matches(".*[A-ZÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ]{3,}.*")) {
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
        List<Review> reviews = List.of(
            new Review("r1", "Produto excelente! Muito bom, adorei a qualidade.", "Notebook Dell"),
            new Review("r2", "Pessimo, chegou com defeito e o atendimento foi horrivel.", "Mouse Logitech"),
            new Review("r3", "Não é ruim, mas esperava mais pela descrição.", "Teclado Mecânico"),
            new Review("r4", "Produto bom, mas o preço está muito caro.", "Monitor LG"),
            new Review("r5", "MARAVILHOSO!!! Recomendo muito, perfeito!", "Cadeira Gamer"),
            new Review("r6", "Satisfeito com a compra, entrega rápida.", "SSD Samsung")
        );

        System.out.println("=== Sistema de Analise de Sentimento ===\n");

        Review review = reviews.get(0);
        System.out.println("Analise da Review: \"" + review.text() + "\"\n");

        System.out.println("Estrategia: Lexico");
        SentimentAnalysisSystem lexiconSystem = new SentimentAnalysisSystem(new LexiconStrategy());
        SentimentResult lexiconResult = lexiconSystem.analyze(review);
        System.out.printf("  Sentimento: %s%n", lexiconResult.sentiment());
        System.out.printf("  Score: %.2f%n", lexiconResult.score());
        System.out.printf("  Metodo: %s%n", lexiconResult.method());

        System.out.println("\nEstrategia: Regras");
        SentimentAnalysisSystem ruleSystem = new SentimentAnalysisSystem(new RuleBasedStrategy());
        SentimentResult ruleResult = ruleSystem.analyze(review);
        System.out.printf("  Sentimento: %s%n", ruleResult.sentiment());
        System.out.printf("  Score: %.2f%n", ruleResult.score());
        System.out.printf("  Metodo: %s%n", ruleResult.method());

        System.out.println("\nEstrategia: Hibrida");
        SentimentAnalysisSystem hybridSystem = new SentimentAnalysisSystem(new HybridStrategy());
        SentimentResult hybridResult = hybridSystem.analyze(review);
        System.out.printf("  Sentimento: %s%n", hybridResult.sentiment());
        System.out.printf("  Score: %.2f%n", hybridResult.score());
        System.out.printf("  Metodo: %s%n", hybridResult.method());

        System.out.println("\nDistribuicao de Sentimentos (Todas as Reviews):");
        Map<Sentiment, Long> distribution = hybridSystem.getSentimentDistribution(reviews);
        System.out.printf("  Positivo: %d%n", distribution.getOrDefault(Sentiment.POSITIVE, 0L));
        System.out.printf("  Negativo: %d%n", distribution.getOrDefault(Sentiment.NEGATIVE, 0L));
        System.out.printf("  Neutro: %d%n", distribution.getOrDefault(Sentiment.NEUTRAL, 0L));

        System.out.println("\nSentimento Medio:");
        System.out.printf("  Score: %.2f%n", hybridSystem.getAverageSentiment(reviews));
    }
}
