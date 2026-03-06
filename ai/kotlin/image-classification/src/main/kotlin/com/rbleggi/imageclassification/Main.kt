package com.rbleggi.imageclassification

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

enum class Category {
    Onca, Arara, Capivara, PaoDeQueijo, Feijoada, Acai
}

data class Image(
    val id: String,
    val name: String,
    val features: List<Double>,
    val actualCategory: Category? = null
)

data class ClassificationResult(
    val category: Category,
    val confidence: Double,
    val details: String
)

sealed interface ClassificationStrategy {
    fun classify(image: Image): ClassificationResult
}

class ThresholdStrategy(
    private val thresholds: Map<Category, List<Double>>
) : ClassificationStrategy {
    override fun classify(image: Image): ClassificationResult {
        var bestMatch: Category? = null
        var bestScore = 0.0

        for ((category, categoryThresholds) in thresholds) {
            val matches = image.features.zip(categoryThresholds).count { (feature, threshold) ->
                abs(feature - threshold) < 0.3
            }
            val score = matches.toDouble() / image.features.size

            if (score > bestScore) {
                bestScore = score
                bestMatch = category
            }
        }

        return ClassificationResult(
            category = bestMatch ?: Category.Capivara,
            confidence = bestScore,
            details = "Threshold matching: ${(bestScore * 100).toInt()}% dos atributos correspondem"
        )
    }
}

class KNNStrategy(
    private val trainingSet: List<Image>,
    private val k: Int = 3
) : ClassificationStrategy {
    override fun classify(image: Image): ClassificationResult {
        val distances = trainingSet.map { trainImage ->
            val distance = euclideanDistance(image.features, trainImage.features)
            trainImage.actualCategory!! to distance
        }.sortedBy { it.second }.take(k)

        val categoryVotes = distances.groupingBy { it.first }.eachCount()
        val bestCategory = categoryVotes.maxByOrNull { it.value }!!.key
        val confidence = categoryVotes[bestCategory]!!.toDouble() / k

        return ClassificationResult(
            category = bestCategory,
            confidence = confidence,
            details = "KNN (k=$k): ${categoryVotes[bestCategory]} de $k vizinhos"
        )
    }

    private fun euclideanDistance(a: List<Double>, b: List<Double>): Double {
        return sqrt(a.zip(b).sumOf { (x, y) -> (x - y).pow(2) })
    }
}

class NeuralNetworkLikeStrategy(
    private val weights: Map<Category, List<Double>>
) : ClassificationStrategy {
    override fun classify(image: Image): ClassificationResult {
        val scores = weights.mapValues { (_, categoryWeights) ->
            val rawScore = image.features.zip(categoryWeights).sumOf { (feature, weight) ->
                feature * weight
            }
            sigmoid(rawScore)
        }

        val bestCategory = scores.maxByOrNull { it.value }!!.key
        val confidence = scores[bestCategory]!!

        return ClassificationResult(
            category = bestCategory,
            confidence = confidence,
            details = "Rede neural: ativacao %.3f".format(confidence)
        )
    }

    private fun sigmoid(x: Double): Double {
        return 1.0 / (1.0 + kotlin.math.exp(-x))
    }
}

class ImageClassifier(private val strategy: ClassificationStrategy) {
    fun classify(image: Image): ClassificationResult =
        strategy.classify(image)

    fun classifyBatch(images: List<Image>): List<ClassificationResult> =
        images.map { classify(it) }

    fun accuracy(images: List<Image>): Double {
        val correct = images.count { image ->
            val result = classify(image)
            result.category == image.actualCategory
        }
        return correct.toDouble() / images.size
    }
}

fun main() {
    println("Image Classification")
}
