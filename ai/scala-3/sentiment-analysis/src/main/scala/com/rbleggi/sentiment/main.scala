package com.rbleggi.sentiment

enum Sentiment:
  case Positive, Negative, Neutral

case class Review(text: String, product: String)

case class AnalysisResult(review: Review, sentiment: Sentiment, score: Double)

trait SentimentStrategy:
  def analyze(text: String): (Sentiment, Double)

class LexiconStrategy extends SentimentStrategy:
  private val positiveWords = Set(
    "good", "great", "excellent", "wonderful", "perfect", "loved",
    "fantastic", "amazing", "recommend", "quality", "fast", "efficient"
  )

  private val negativeWords = Set(
    "bad", "terrible", "horrible", "awful", "defect", "broken",
    "slow", "delayed", "not recommend", "disappointing", "problem"
  )

  def analyze(text: String): (Sentiment, Double) =
    val cleanText = text.toLowerCase.split("\\s+")
    val positiveCount = cleanText.count(positiveWords.contains)
    val negativeCount = cleanText.count(negativeWords.contains)

    val score = (positiveCount - negativeCount).toDouble / cleanText.length

    if score > 0.05 then (Sentiment.Positive, score)
    else if score < -0.05 then (Sentiment.Negative, score)
    else (Sentiment.Neutral, score)

class RuleBasedStrategy extends SentimentStrategy:
  def analyze(text: String): (Sentiment, Double) =
    val textLower = text.toLowerCase

    val positiveRules = List(
      "great product" -> 0.8,
      "very good" -> 0.7,
      "recommend" -> 0.6,
      "loved" -> 0.8,
      "excellent" -> 0.9
    )

    val negativeRules = List(
      "not recommend" -> -0.8,
      "terrible" -> -0.9,
      "not working" -> -0.7,
      "defect" -> -0.6
    )

    var score = 0.0

    positiveRules.foreach { (rule, weight) =>
      if textLower.contains(rule) then score += weight
    }

    negativeRules.foreach { (rule, weight) =>
      if textLower.contains(rule) then score += weight
    }

    if score > 0.3 then (Sentiment.Positive, score)
    else if score < -0.3 then (Sentiment.Negative, score)
    else (Sentiment.Neutral, score)

class HybridStrategy extends SentimentStrategy:
  private val lexicon = LexiconStrategy()
  private val ruleBased = RuleBasedStrategy()

  def analyze(text: String): (Sentiment, Double) =
    val (sent1, score1) = lexicon.analyze(text)
    val (sent2, score2) = ruleBased.analyze(text)

    val combinedScore = (score1 + score2) / 2

    if combinedScore > 0.2 then (Sentiment.Positive, combinedScore)
    else if combinedScore < -0.2 then (Sentiment.Negative, combinedScore)
    else (Sentiment.Neutral, combinedScore)

class SentimentAnalyzer(strategy: SentimentStrategy):
  def analyze(review: Review): AnalysisResult =
    val (sentiment, score) = strategy.analyze(review.text)
    AnalysisResult(review, sentiment, score)

  def analyzeBatch(reviews: List[Review]): List[AnalysisResult] =
    reviews.map(analyze)

@main def run(): Unit =
  println("Sentiment Analysis")
