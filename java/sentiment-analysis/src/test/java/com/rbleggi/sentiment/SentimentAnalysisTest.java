package com.rbleggi.sentiment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SentimentAnalysisTest {
    private Review positiveReview;
    private Review negativeReview;
    private Review neutralReview;

    @BeforeEach
    void setUp() {
        positiveReview = new Review("r1", "Produto excelente, adorei a qualidade!", "Notebook");
        negativeReview = new Review("r2", "Pessimo, horrivel, odiei!", "Mouse");
        neutralReview = new Review("r3", "Produto ok, nada especial.", "Teclado");
    }

    @Test
    @DisplayName("LexiconStrategy detects positive sentiment")
    void lexiconStrategy_detectsPositiveSentiment() {
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new LexiconStrategy());
        SentimentResult result = system.analyze(positiveReview);

        assertEquals(Sentiment.POSITIVE, result.sentiment());
        assertTrue(result.score() > 1.0);
        assertEquals("Lexicon-based", result.method());
    }

    @Test
    @DisplayName("LexiconStrategy detects negative sentiment")
    void lexiconStrategy_detectsNegativeSentiment() {
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new LexiconStrategy());
        SentimentResult result = system.analyze(negativeReview);

        assertEquals(Sentiment.NEGATIVE, result.sentiment());
        assertTrue(result.score() < -1.0);
    }

    @Test
    @DisplayName("RuleBasedStrategy handles negation")
    void ruleBasedStrategy_handlesNegation() {
        Review review = new Review("r4", "Não é bom, muito ruim.", "Produto");
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new RuleBasedStrategy());
        SentimentResult result = system.analyze(review);

        assertTrue(result.score() < 0);
        assertEquals("Rule-based", result.method());
    }

    @Test
    @DisplayName("RuleBasedStrategy prioritizes text after 'mas'")
    void ruleBasedStrategy_prioritizesAfterMas() {
        Review review = new Review("r5", "Produto ruim, mas é bom pelo preço.", "Item");
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new RuleBasedStrategy());
        SentimentResult result = system.analyze(review);

        assertTrue(result.score() > 0);
    }

    @Test
    @DisplayName("HybridStrategy combines lexicon and rules")
    void hybridStrategy_combinesStrategies() {
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new HybridStrategy());
        SentimentResult result = system.analyze(positiveReview);

        assertEquals(Sentiment.POSITIVE, result.sentiment());
        assertEquals("Hybrid", result.method());
    }

    @Test
    @DisplayName("SentimentAnalysisSystem analyzes batch of reviews")
    void analysisSystem_analyzesBatch() {
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new LexiconStrategy());
        List<Review> reviews = List.of(positiveReview, negativeReview, neutralReview);
        List<SentimentResult> results = system.analyzeBatch(reviews);

        assertEquals(3, results.size());
        assertEquals(Sentiment.POSITIVE, results.get(0).sentiment());
        assertEquals(Sentiment.NEGATIVE, results.get(1).sentiment());
    }

    @Test
    @DisplayName("SentimentAnalysisSystem calculates distribution")
    void analysisSystem_calculatesDistribution() {
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new LexiconStrategy());
        List<Review> reviews = List.of(positiveReview, negativeReview);
        Map<Sentiment, Long> distribution = system.getSentimentDistribution(reviews);

        assertEquals(1L, distribution.get(Sentiment.POSITIVE));
        assertEquals(1L, distribution.get(Sentiment.NEGATIVE));
    }

    @Test
    @DisplayName("SentimentAnalysisSystem calculates average sentiment")
    void analysisSystem_calculatesAverageSentiment() {
        SentimentAnalysisSystem system = new SentimentAnalysisSystem(new LexiconStrategy());
        List<Review> reviews = List.of(positiveReview, negativeReview);
        double average = system.getAverageSentiment(reviews);

        assertTrue(average != 0.0);
    }
}
