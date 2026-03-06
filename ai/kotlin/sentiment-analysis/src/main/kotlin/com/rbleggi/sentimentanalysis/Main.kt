package com.rbleggi.sentimentanalysis

enum class Sentiment {
    Positive, Negative, Neutral
}

data class Review(
    val id: String,
    val text: String,
    val product: String
)

data class SentimentResult(
    val sentiment: Sentiment,
    val score: Double,
    val confidence: Double,
    val details: String
)

sealed interface SentimentStrategy {
    fun analyze(review: Review): SentimentResult
}

class LexiconBasedStrategy : SentimentStrategy {
    private val positiveWords = setOf(
        "bom", "otimo", "excelente", "maravilhoso", "perfeito",
        "adorei", "recomendo", "qualidade", "rapido", "eficiente",
        "incrivel", "fantastico", "melhor", "top", "satisfeito"
    )

    private val negativeWords = setOf(
        "ruim", "pessimo", "horrivel", "defeito", "problema",
        "lento", "caro", "decepcionante", "nao recomendo", "quebrado",
        "terrivel", "pior", "insatisfeito", "fraude", "falso"
    )

    override fun analyze(review: Review): SentimentResult {
        val words = review.text.lowercase().split(" ", ",", ".", "!", "?")

        val positiveCount = words.count { positiveWords.contains(it) }
        val negativeCount = words.count { negativeWords.contains(it) }
        val totalWords = words.size

        val score = when {
            positiveCount > negativeCount -> (positiveCount.toDouble() / totalWords) * 2.0
            negativeCount > positiveCount -> -(negativeCount.toDouble() / totalWords) * 2.0
            else -> 0.0
        }.coerceIn(-1.0, 1.0)

        val sentiment = when {
            score > 0.2 -> Sentiment.Positive
            score < -0.2 -> Sentiment.Negative
            else -> Sentiment.Neutral
        }

        val confidence = kotlin.math.abs(score)

        return SentimentResult(
            sentiment = sentiment,
            score = score,
            confidence = confidence,
            details = "Lexico: $positiveCount positivas, $negativeCount negativas"
        )
    }
}

class RuleBasedStrategy : SentimentStrategy {
    override fun analyze(review: Review): SentimentResult {
        val text = review.text.lowercase()

        var score = 0.0

        if (text.contains("recomendo") || text.contains("adorei")) score += 0.5
        if (text.contains("nao recomendo") || text.contains("nunca mais")) score -= 0.5
        if (text.contains("excelente") || text.contains("perfeito")) score += 0.4
        if (text.contains("pessimo") || text.contains("horrivel")) score -= 0.4
        if (text.contains("!")) score += 0.1
        if (text.contains("?") && text.contains("nao")) score -= 0.2
        if (text.contains("mas") || text.contains("porem")) score -= 0.1

        val exclamationCount = text.count { it == '!' }
        score += exclamationCount * 0.05

        score = score.coerceIn(-1.0, 1.0)

        val sentiment = when {
            score > 0.3 -> Sentiment.Positive
            score < -0.3 -> Sentiment.Negative
            else -> Sentiment.Neutral
        }

        val confidence = kotlin.math.abs(score) * 0.8

        return SentimentResult(
            sentiment = sentiment,
            score = score,
            confidence = confidence,
            details = "Regras: score ${String.format("%.2f", score)}"
        )
    }
}

class HybridStrategy : SentimentStrategy {
    private val lexiconStrategy = LexiconBasedStrategy()
    private val ruleStrategy = RuleBasedStrategy()

    override fun analyze(review: Review): SentimentResult {
        val lexiconResult = lexiconStrategy.analyze(review)
        val ruleResult = ruleStrategy.analyze(review)

        val combinedScore = (lexiconResult.score * 0.6 + ruleResult.score * 0.4)
        val combinedConfidence = (lexiconResult.confidence * 0.6 + ruleResult.confidence * 0.4)

        val sentiment = when {
            combinedScore > 0.25 -> Sentiment.Positive
            combinedScore < -0.25 -> Sentiment.Negative
            else -> Sentiment.Neutral
        }

        return SentimentResult(
            sentiment = sentiment,
            score = combinedScore,
            confidence = combinedConfidence,
            details = "Hibrido: lexico (${lexiconResult.sentiment}) + regras (${ruleResult.sentiment})"
        )
    }
}

class SentimentAnalyzer(private val strategy: SentimentStrategy) {
    fun analyze(review: Review): SentimentResult =
        strategy.analyze(review)

    fun analyzeBatch(reviews: List<Review>): List<SentimentResult> =
        reviews.map { analyze(it) }

    fun sentimentDistribution(reviews: List<Review>): Map<Sentiment, Int> {
        val results = analyzeBatch(reviews)
        return mapOf(
            Sentiment.Positive to results.count { it.sentiment == Sentiment.Positive },
            Sentiment.Negative to results.count { it.sentiment == Sentiment.Negative },
            Sentiment.Neutral to results.count { it.sentiment == Sentiment.Neutral }
        )
    }
}

fun main() {
    println("Sentiment Analysis")
}
