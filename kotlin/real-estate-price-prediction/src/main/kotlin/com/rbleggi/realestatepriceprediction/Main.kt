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
    val trainingSet = listOf(
        Property("p1", "Sao Paulo", "Jardins", 120.0, 3, 5, 950000.0),
        Property("p2", "Sao Paulo", "Jardins", 80.0, 2, 10, 650000.0),
        Property("p3", "Curitiba", "Batel", 100.0, 2, 3, 580000.0),
        Property("p4", "Curitiba", "Batel", 150.0, 3, 8, 820000.0),
        Property("p5", "Belo Horizonte", "Savassi", 90.0, 2, 2, 520000.0),
        Property("p6", "Belo Horizonte", "Savassi", 110.0, 3, 7, 680000.0)
    )

    val testProperties = listOf(
        Property("t1", "Sao Paulo", "Jardins", 100.0, 2, 6, 750000.0),
        Property("t2", "Curitiba", "Batel", 130.0, 3, 5, 720000.0),
        Property("t3", "Belo Horizonte", "Savassi", 95.0, 2, 4, 580000.0)
    )

    println("=== Sistema de Predicao de Precos de Imoveis ===\n")

    println("--- Estrategia: Regressao Linear ---")
    val linearSystem = PricePredictionSystem(LinearRegressionStrategy())
    testProperties.forEach { property ->
        val result = linearSystem.predict(property, trainingSet)
        println("  ${property.city} - ${property.neighborhood}")
        println("    Area: ${property.area}m2 | Quartos: ${property.bedrooms} | Idade: ${property.age} anos")
        println("    Preco Previsto: R$ %.2f".format(result.predictedPrice))
        println("    Preco Real: R$ %.2f".format(property.actualPrice))
        println("    Confianca: %.2f | Metodo: ${result.method}\n".format(result.confidence))
    }

    println("--- Estrategia: Regressao Polinomial ---")
    val polynomialSystem = PricePredictionSystem(PolynomialRegressionStrategy())
    val polyResult = polynomialSystem.predict(testProperties[0], trainingSet)
    println("  Preco Previsto: R$ %.2f".format(polyResult.predictedPrice))
    println("  Confianca: %.2f | Metodo: ${polyResult.method}\n".format(polyResult.confidence))

    println("--- Estrategia: KNN (k=3) ---")
    val knnSystem = PricePredictionSystem(KNNRegressionStrategy(k = 3))
    val knnResult = knnSystem.predict(testProperties[0], trainingSet)
    println("  Preco Previsto: R$ %.2f".format(knnResult.predictedPrice))
    println("  Confianca: %.2f | Metodo: ${knnResult.method}\n".format(knnResult.confidence))

    println("--- Erro Absoluto Medio (MAE) ---")
    println("  Linear: R$ %.2f".format(linearSystem.meanAbsoluteError(testProperties, trainingSet)))
    println("  Polinomial: R$ %.2f".format(polynomialSystem.meanAbsoluteError(testProperties, trainingSet)))
    println("  KNN: R$ %.2f".format(knnSystem.meanAbsoluteError(testProperties, trainingSet)))
}
