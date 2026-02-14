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
    val trainingSet = listOf(
        Image("t1", "Onca Pantanal", listOf(0.9, 0.8, 0.2, 0.1), Category.Onca),
        Image("t2", "Onca Amazonia", listOf(0.85, 0.75, 0.25, 0.15), Category.Onca),
        Image("t3", "Arara Azul", listOf(0.2, 0.3, 0.9, 0.8), Category.Arara),
        Image("t4", "Arara Vermelha", listOf(0.25, 0.35, 0.85, 0.75), Category.Arara),
        Image("t5", "Capivara SP", listOf(0.5, 0.6, 0.4, 0.5), Category.Capivara),
        Image("t6", "Capivara Pantanal", listOf(0.55, 0.65, 0.45, 0.55), Category.Capivara)
    )

    val testImages = listOf(
        Image("i1", "Onca Teste", listOf(0.88, 0.78, 0.22, 0.12), Category.Onca),
        Image("i2", "Arara Teste", listOf(0.22, 0.32, 0.88, 0.78), Category.Arara),
        Image("i3", "Capivara Teste", listOf(0.52, 0.62, 0.42, 0.52), Category.Capivara)
    )

    println("=== Sistema de Classificacao de Imagens ===\n")

    println("--- Estrategia: Threshold ---")
    val thresholds = mapOf(
        Category.Onca to listOf(0.9, 0.8, 0.2, 0.1),
        Category.Arara to listOf(0.2, 0.3, 0.9, 0.8),
        Category.Capivara to listOf(0.5, 0.6, 0.4, 0.5)
    )
    val thresholdClassifier = ImageClassifier(ThresholdStrategy(thresholds))

    testImages.forEach { image ->
        val result = thresholdClassifier.classify(image)
        println("  ${image.name}")
        println("    Categoria: ${result.category}")
        println("    Confianca: %.2f".format(result.confidence))
        println("    ${result.details}")
    }

    println("\n--- Estrategia: KNN (k=3) ---")
    val knnClassifier = ImageClassifier(KNNStrategy(trainingSet, k = 3))

    testImages.forEach { image ->
        val result = knnClassifier.classify(image)
        println("  ${image.name}")
        println("    Categoria: ${result.category}")
        println("    Confianca: %.2f".format(result.confidence))
        println("    ${result.details}")
    }

    println("\n--- Estrategia: Rede Neural ---")
    val weights = mapOf(
        Category.Onca to listOf(0.8, 0.7, -0.3, -0.2),
        Category.Arara to listOf(-0.3, -0.2, 0.8, 0.7),
        Category.Capivara to listOf(0.1, 0.2, 0.1, 0.2)
    )
    val nnClassifier = ImageClassifier(NeuralNetworkLikeStrategy(weights))

    testImages.forEach { image ->
        val result = nnClassifier.classify(image)
        println("  ${image.name}")
        println("    Categoria: ${result.category}")
        println("    Confianca: %.2f".format(result.confidence))
        println("    ${result.details}")
    }

    println("\n--- Acuracia dos Modelos ---")
    println("  Threshold: %.2f%%".format(thresholdClassifier.accuracy(testImages) * 100))
    println("  KNN: %.2f%%".format(knnClassifier.accuracy(testImages) * 100))
    println("  Rede Neural: %.2f%%".format(nnClassifier.accuracy(testImages) * 100))
}
