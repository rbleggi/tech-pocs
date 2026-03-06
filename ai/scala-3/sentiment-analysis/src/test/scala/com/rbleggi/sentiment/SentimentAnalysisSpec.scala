package com.rbleggi.sentiment

import org.scalatest.funsuite.AnyFunSuite

class SentimentAnalysisSpec extends AnyFunSuite:

  test("LexiconStrategy should identify positive sentiment"):
    val strategy = LexiconStrategy()
    val (sentiment, score) = strategy.analyze("Product excellent and great")
    assert(sentiment == Sentiment.Positive)
    assert(score > 0)

  test("LexiconStrategy should identify negative sentiment"):
    val strategy = LexiconStrategy()
    val (sentiment, score) = strategy.analyze("Product terrible and bad")
    assert(sentiment == Sentiment.Negative)
    assert(score < 0)

  test("LexiconStrategy should identify neutral sentiment"):
    val strategy = LexiconStrategy()
    val (sentiment, score) = strategy.analyze("The product arrived today")
    assert(sentiment == Sentiment.Neutral)

  test("RuleBasedStrategy should identify positive with strong rule"):
    val strategy = RuleBasedStrategy()
    val (sentiment, score) = strategy.analyze("Great product, very good")
    assert(sentiment == Sentiment.Positive)
    assert(score > 0.3)

  test("RuleBasedStrategy should identify negative with strong rule"):
    val strategy = RuleBasedStrategy()
    val (sentiment, score) = strategy.analyze("Not recommend, product terrible")
    assert(sentiment == Sentiment.Negative)
    assert(score < -0.3)

  test("RuleBasedStrategy should identify neutral without matching rules"):
    val strategy = RuleBasedStrategy()
    val (sentiment, score) = strategy.analyze("Normal product")
    assert(sentiment == Sentiment.Neutral)

  test("HybridStrategy should combine both approaches"):
    val strategy = HybridStrategy()
    val (sentiment, score) = strategy.analyze("Excellent product, recommend")
    assert(sentiment == Sentiment.Positive)

  test("SentimentAnalyzer should analyze single review"):
    val analyzer = SentimentAnalyzer(LexiconStrategy())
    val review = Review("Product wonderful", "Notebook")
    val result = analyzer.analyze(review)
    assert(result.sentiment == Sentiment.Positive)
    assert(result.review.product == "Notebook")

  test("SentimentAnalyzer should analyze batch of reviews"):
    val analyzer = SentimentAnalyzer(LexiconStrategy())
    val reviews = List(
      Review("Great product", "Mouse"),
      Review("Product bad", "Keyboard")
    )
    val results = analyzer.analyzeBatch(reviews)
    assert(results.length == 2)
    assert(results(0).sentiment == Sentiment.Positive)
    assert(results(1).sentiment == Sentiment.Negative)

  test("Review should store text and product"):
    val review = Review("Good product", "Monitor")
    assert(review.text == "Good product")
    assert(review.product == "Monitor")

  test("AnalysisResult should contain all analysis data"):
    val review = Review("Test", "Product")
    val result = AnalysisResult(review, Sentiment.Positive, 0.5)
    assert(result.sentiment == Sentiment.Positive)
    assert(result.score == 0.5)
    assert(result.review.text == "Test")
