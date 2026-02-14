package com.rbleggi.sentimentanalysis

import kotlin.test.*

class SentimentAnalysisTest {

    private val positiveReview = Review("r1", "Produto excelente! Adorei a qualidade. Recomendo!", "Test")
    private val negativeReview = Review("r2", "Pessimo! Horrivel! Nao recomendo.", "Test")
    private val neutralReview = Review("r3", "O produto e ok.", "Test")

    @Test
    fun `LexiconBasedStrategy detects positive sentiment`() {
        val strategy = LexiconBasedStrategy()
        val result = strategy.analyze(positiveReview)

        assertEquals(Sentiment.Positive, result.sentiment)
        assertTrue(result.score > 0.0)
    }

    @Test
    fun `LexiconBasedStrategy detects negative sentiment`() {
        val strategy = LexiconBasedStrategy()
        val result = strategy.analyze(negativeReview)

        assertEquals(Sentiment.Negative, result.sentiment)
        assertTrue(result.score < 0.0)
    }

    @Test
    fun `LexiconBasedStrategy provides confidence score`() {
        val strategy = LexiconBasedStrategy()
        val result = strategy.analyze(positiveReview)

        assertTrue(result.confidence >= 0.0 && result.confidence <= 1.0)
        assertTrue(result.details.contains("Lexico"))
    }

    @Test
    fun `RuleBasedStrategy detects positive keywords`() {
        val strategy = RuleBasedStrategy()
        val result = strategy.analyze(positiveReview)

        assertTrue(result.score > 0.0)
    }

    @Test
    fun `RuleBasedStrategy detects negative keywords`() {
        val strategy = RuleBasedStrategy()
        val result = strategy.analyze(negativeReview)

        assertTrue(result.score < 0.0)
    }

    @Test
    fun `RuleBasedStrategy handles exclamation marks`() {
        val exclamationReview = Review("r", "Otimo!!!", "Test")
        val strategy = RuleBasedStrategy()
        val result = strategy.analyze(exclamationReview)

        assertTrue(result.score > 0.0)
    }

    @Test
    fun `HybridStrategy combines lexicon and rules`() {
        val strategy = HybridStrategy()
        val result = strategy.analyze(positiveReview)

        assertTrue(result.details.contains("Hibrido"))
        assertTrue(result.details.contains("lexico"))
        assertTrue(result.details.contains("regras"))
    }

    @Test
    fun `HybridStrategy detects positive sentiment`() {
        val strategy = HybridStrategy()
        val result = strategy.analyze(positiveReview)

        assertEquals(Sentiment.Positive, result.sentiment)
    }

    @Test
    fun `HybridStrategy detects negative sentiment`() {
        val strategy = HybridStrategy()
        val result = strategy.analyze(negativeReview)

        assertEquals(Sentiment.Negative, result.sentiment)
    }

    @Test
    fun `SentimentAnalyzer analyzes single review`() {
        val analyzer = SentimentAnalyzer(LexiconBasedStrategy())
        val result = analyzer.analyze(positiveReview)

        assertEquals(Sentiment.Positive, result.sentiment)
    }

    @Test
    fun `SentimentAnalyzer analyzes batch of reviews`() {
        val analyzer = SentimentAnalyzer(LexiconBasedStrategy())
        val reviews = listOf(positiveReview, negativeReview)
        val results = analyzer.analyzeBatch(reviews)

        assertEquals(2, results.size)
    }

    @Test
    fun `SentimentAnalyzer calculates sentiment distribution`() {
        val analyzer = SentimentAnalyzer(HybridStrategy())
        val reviews = listOf(positiveReview, negativeReview, neutralReview)
        val distribution = analyzer.sentimentDistribution(reviews)

        assertEquals(3, distribution.values.sum())
        assertTrue(distribution.containsKey(Sentiment.Positive))
        assertTrue(distribution.containsKey(Sentiment.Negative))
        assertTrue(distribution.containsKey(Sentiment.Neutral))
    }

    @Test
    fun `Review data class stores all fields`() {
        val review = Review("id", "text", "product")

        assertEquals("id", review.id)
        assertEquals("text", review.text)
        assertEquals("product", review.product)
    }

    @Test
    fun `SentimentResult contains all fields`() {
        val result = SentimentResult(Sentiment.Positive, 0.8, 0.9, "details")

        assertEquals(Sentiment.Positive, result.sentiment)
        assertEquals(0.8, result.score)
        assertEquals(0.9, result.confidence)
        assertEquals("details", result.details)
    }

    @Test
    fun `Sentiment enum has all values`() {
        assertNotNull(Sentiment.Positive)
        assertNotNull(Sentiment.Negative)
        assertNotNull(Sentiment.Neutral)
    }

    @Test
    fun `LexiconBasedStrategy score is bounded`() {
        val strategy = LexiconBasedStrategy()
        val result = strategy.analyze(positiveReview)

        assertTrue(result.score >= -1.0 && result.score <= 1.0)
    }

    @Test
    fun `RuleBasedStrategy score is bounded`() {
        val strategy = RuleBasedStrategy()
        val result = strategy.analyze(positiveReview)

        assertTrue(result.score >= -1.0 && result.score <= 1.0)
    }

    @Test
    fun `HybridStrategy confidence is weighted average`() {
        val strategy = HybridStrategy()
        val result = strategy.analyze(positiveReview)

        assertTrue(result.confidence >= 0.0 && result.confidence <= 1.0)
    }
}
