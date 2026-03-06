package com.rbleggi.realestatepriceprediction

import kotlin.test.*

class RealEstatePricePredictionTest {

    private val trainingSet = listOf(
        Property("p1", "Sao Paulo", "Jardins", 120.0, 3, 5, 950000.0),
        Property("p2", "Sao Paulo", "Jardins", 80.0, 2, 10, 650000.0),
        Property("p3", "Curitiba", "Batel", 100.0, 2, 3, 580000.0)
    )

    private val testProperty = Property("t1", "Sao Paulo", "Jardins", 100.0, 2, 6, 750000.0)

    @Test
    fun `LinearRegressionStrategy predicts price`() {
        val strategy = LinearRegressionStrategy()
        val result = strategy.predict(testProperty, trainingSet)

        assertTrue(result.predictedPrice > 0.0)
        assertEquals("Regressao Linear", result.method)
    }

    @Test
    fun `LinearRegressionStrategy considers area`() {
        val smallProperty = Property("t", "SP", "Centro", 50.0, 2, 5, null)
        val largeProperty = Property("t", "SP", "Centro", 200.0, 2, 5, null)
        val strategy = LinearRegressionStrategy()

        val smallResult = strategy.predict(smallProperty, trainingSet)
        val largeResult = strategy.predict(largeProperty, trainingSet)

        assertTrue(largeResult.predictedPrice > smallResult.predictedPrice)
    }

    @Test
    fun `PolynomialRegressionStrategy predicts price`() {
        val strategy = PolynomialRegressionStrategy()
        val result = strategy.predict(testProperty, trainingSet)

        assertTrue(result.predictedPrice > 0.0)
        assertEquals("Regressao Polinomial", result.method)
    }

    @Test
    fun `PolynomialRegressionStrategy uses polynomial features`() {
        val strategy = PolynomialRegressionStrategy()
        val result = strategy.predict(testProperty, trainingSet)

        assertTrue(result.confidence > 0.7)
    }

    @Test
    fun `KNNRegressionStrategy finds nearest neighbors`() {
        val strategy = KNNRegressionStrategy(k = 3)
        val result = strategy.predict(testProperty, trainingSet)

        assertTrue(result.predictedPrice > 0.0)
        assertTrue(result.method.contains("KNN"))
    }

    @Test
    fun `KNNRegressionStrategy respects k parameter`() {
        val strategy1 = KNNRegressionStrategy(k = 1)
        val strategy3 = KNNRegressionStrategy(k = 3)

        val result1 = strategy1.predict(testProperty, trainingSet)
        val result3 = strategy3.predict(testProperty, trainingSet)

        assertTrue(result1.method.contains("k=1"))
        assertTrue(result3.method.contains("k=3"))
    }

    @Test
    fun `PricePredictionSystem predicts single property`() {
        val system = PricePredictionSystem(LinearRegressionStrategy())
        val result = system.predict(testProperty, trainingSet)

        assertTrue(result.predictedPrice > 0.0)
    }

    @Test
    fun `PricePredictionSystem predicts batch of properties`() {
        val system = PricePredictionSystem(LinearRegressionStrategy())
        val properties = listOf(testProperty, testProperty)
        val results = system.predictBatch(properties, trainingSet)

        assertEquals(2, results.size)
    }

    @Test
    fun `PricePredictionSystem calculates mean absolute error`() {
        val system = PricePredictionSystem(KNNRegressionStrategy(k = 3))
        val testSet = listOf(testProperty)
        val mae = system.meanAbsoluteError(testSet, trainingSet)

        assertTrue(mae >= 0.0)
    }

    @Test
    fun `Property data class stores all fields`() {
        val property = Property("id", "city", "neighborhood", 100.0, 3, 5, 500000.0)

        assertEquals("id", property.id)
        assertEquals("city", property.city)
        assertEquals("neighborhood", property.neighborhood)
        assertEquals(100.0, property.area)
        assertEquals(3, property.bedrooms)
        assertEquals(5, property.age)
        assertEquals(500000.0, property.actualPrice)
    }

    @Test
    fun `PredictionResult contains all fields`() {
        val result = PredictionResult(750000.0, 0.85, "Linear")

        assertEquals(750000.0, result.predictedPrice)
        assertEquals(0.85, result.confidence)
        assertEquals("Linear", result.method)
    }

    @Test
    fun `LinearRegressionStrategy price is always positive`() {
        val strategy = LinearRegressionStrategy()
        val tinyProperty = Property("t", "SP", "Centro", 10.0, 1, 50, null)
        val result = strategy.predict(tinyProperty, trainingSet)

        assertTrue(result.predictedPrice >= 100000.0)
    }

    @Test
    fun `PolynomialRegressionStrategy handles large areas`() {
        val strategy = PolynomialRegressionStrategy()
        val largeProperty = Property("t", "SP", "Centro", 300.0, 4, 1, null)
        val result = strategy.predict(largeProperty, trainingSet)

        assertTrue(result.predictedPrice > 1000000.0)
    }

    @Test
    fun `KNNRegressionStrategy averages neighbor prices`() {
        val identicalProperty = Property("t", "Sao Paulo", "Jardins", 120.0, 3, 5, null)
        val strategy = KNNRegressionStrategy(k = 1)
        val result = strategy.predict(identicalProperty, trainingSet)

        assertEquals(950000.0, result.predictedPrice, 50000.0)
    }
}
