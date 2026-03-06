package com.rbleggi.realestatepriceprediction

import kotlin.math.pow
import kotlin.math.sqrt

data class Property(
    val id: String,
    val city: String,
    val neighborhood: String,
    val area: Double,
    val bedrooms: Int,
    val age: Int,
    val actualPrice: Double? = null
)

data class PredictionResult(
    val predictedPrice: Double,
    val confidence: Double,
    val method: String
)

sealed interface PredictionStrategy {
    fun predict(property: Property, trainingSet: List<Property>): PredictionResult
}

class LinearRegressionStrategy : PredictionStrategy {
    override fun predict(property: Property, trainingSet: List<Property>): PredictionResult {
        val basePrice = 5000.0
        val pricePerSqm = 8000.0
        val bedroomBonus = 50000.0
        val agePenalty = 5000.0

        val predictedPrice = basePrice +
                (property.area * pricePerSqm) +
                (property.bedrooms * bedroomBonus) -
                (property.age * agePenalty)

        val confidence = 0.75

        return PredictionResult(
            predictedPrice = predictedPrice.coerceAtLeast(100000.0),
            confidence = confidence,
            method = "Regressao Linear"
        )
    }
}

class PolynomialRegressionStrategy : PredictionStrategy {
    override fun predict(property: Property, trainingSet: List<Property>): PredictionResult {
        val basePrice = 100000.0
        val areaCoeff = 7500.0
        val areaSquaredCoeff = 50.0
        val bedroomCoeff = 60000.0
        val ageCoeff = -4000.0

        val predictedPrice = basePrice +
                (property.area * areaCoeff) +
                (property.area.pow(2) * areaSquaredCoeff) +
                (property.bedrooms * bedroomCoeff) +
                (property.age * ageCoeff)

        val confidence = 0.82

        return PredictionResult(
            predictedPrice = predictedPrice.coerceAtLeast(150000.0),
            confidence = confidence,
            method = "Regressao Polinomial"
        )
    }
}

class KNNRegressionStrategy(private val k: Int = 3) : PredictionStrategy {
    override fun predict(property: Property, trainingSet: List<Property>): PredictionResult {
        val distances = trainingSet.map { trainProperty ->
            val distance = calculateDistance(property, trainProperty)
            trainProperty to distance
        }.sortedBy { it.second }.take(k)

        val predictedPrice = distances.map { it.first.actualPrice!! }.average()

        val maxDistance = distances.maxOfOrNull { it.second } ?: 1.0
        val confidence = 1.0 - (maxDistance / 100.0).coerceIn(0.0, 0.5)

        return PredictionResult(
            predictedPrice = predictedPrice,
            confidence = confidence,
            method = "KNN (k=$k)"
        )
    }

    private fun calculateDistance(p1: Property, p2: Property): Double {
        val areaDiff = (p1.area - p2.area) / 100.0
        val bedroomDiff = (p1.bedrooms - p2.bedrooms).toDouble() * 10.0
        val ageDiff = (p1.age - p2.age) / 5.0

        return sqrt(areaDiff.pow(2) + bedroomDiff.pow(2) + ageDiff.pow(2))
    }
}

class PricePredictionSystem(private val strategy: PredictionStrategy) {
    fun predict(property: Property, trainingSet: List<Property>): PredictionResult =
        strategy.predict(property, trainingSet)

    fun predictBatch(properties: List<Property>, trainingSet: List<Property>): List<PredictionResult> =
        properties.map { predict(it, trainingSet) }

    fun meanAbsoluteError(testSet: List<Property>, trainingSet: List<Property>): Double {
        val errors = testSet.map { property ->
            val prediction = predict(property, trainingSet)
            kotlin.math.abs(prediction.predictedPrice - property.actualPrice!!)
        }
        return errors.average()
    }
}

fun main() {
    println("Real Estate Price Prediction")
}
