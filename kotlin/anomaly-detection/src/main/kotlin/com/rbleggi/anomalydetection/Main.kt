package com.rbleggi.anomalydetection

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Transaction(
    val id: String,
    val amount: Double,
    val account: String,
    val timestamp: Long
)

data class AnomalyResult(
    val isAnomaly: Boolean,
    val score: Double,
    val method: String,
    val details: String
)

sealed interface AnomalyDetectionStrategy {
    fun detect(transaction: Transaction, history: List<Transaction>): AnomalyResult
}

class ZScoreStrategy(private val threshold: Double = 3.0) : AnomalyDetectionStrategy {
    override fun detect(transaction: Transaction, history: List<Transaction>): AnomalyResult {
        val amounts = history.map { it.amount }
        val mean = amounts.average()
        val stdDev = calculateStdDev(amounts, mean)

        val zScore = if (stdDev > 0) abs((transaction.amount - mean) / stdDev) else 0.0
        val isAnomaly = zScore > threshold

        return AnomalyResult(
            isAnomaly = isAnomaly,
            score = zScore,
            method = "Z-Score",
            details = "Z-Score: %.2f (threshold: %.2f)".format(zScore, threshold)
        )
    }

    private fun calculateStdDev(values: List<Double>, mean: Double): Double {
        val variance = values.map { (it - mean).pow(2) }.average()
        return sqrt(variance)
    }
}

class IQRStrategy(private val multiplier: Double = 1.5) : AnomalyDetectionStrategy {
    override fun detect(transaction: Transaction, history: List<Transaction>): AnomalyResult {
        val sortedAmounts = history.map { it.amount }.sorted()
        val size = sortedAmounts.size

        val q1 = sortedAmounts[size / 4]
        val q3 = sortedAmounts[(3 * size) / 4]
        val iqr = q3 - q1

        val lowerBound = q1 - (multiplier * iqr)
        val upperBound = q3 + (multiplier * iqr)

        val isAnomaly = transaction.amount < lowerBound || transaction.amount > upperBound
        val score = if (transaction.amount > upperBound) {
            (transaction.amount - upperBound) / iqr
        } else if (transaction.amount < lowerBound) {
            (lowerBound - transaction.amount) / iqr
        } else {
            0.0
        }

        return AnomalyResult(
            isAnomaly = isAnomaly,
            score = score,
            method = "IQR",
            details = "IQR: %.2f (limites: %.2f - %.2f)".format(iqr, lowerBound, upperBound)
        )
    }
}

class MovingAverageStrategy(
    private val windowSize: Int = 5,
    private val threshold: Double = 2.0
) : AnomalyDetectionStrategy {
    override fun detect(transaction: Transaction, history: List<Transaction>): AnomalyResult {
        val recentTransactions = history.takeLast(windowSize)
        val avgAmount = recentTransactions.map { it.amount }.average()

        val deviation = abs(transaction.amount - avgAmount)
        val relativeDeviation = if (avgAmount > 0) deviation / avgAmount else 0.0

        val isAnomaly = relativeDeviation > threshold

        return AnomalyResult(
            isAnomaly = isAnomaly,
            score = relativeDeviation,
            method = "Moving Average",
            details = "Desvio: %.2f%% (media movel: R$ %.2f)".format(relativeDeviation * 100, avgAmount)
        )
    }
}

class AnomalyDetector(private val strategy: AnomalyDetectionStrategy) {
    fun detect(transaction: Transaction, history: List<Transaction>): AnomalyResult =
        strategy.detect(transaction, history)

    fun detectBatch(transactions: List<Transaction>, history: List<Transaction>): List<AnomalyResult> =
        transactions.map { detect(it, history) }

    fun anomalyCount(transactions: List<Transaction>, history: List<Transaction>): Int =
        detectBatch(transactions, history).count { it.isAnomaly }

    fun falsePositiveRate(
        transactions: List<Transaction>,
        history: List<Transaction>,
        knownAnomalies: Set<String>
    ): Double {
        val results = detectBatch(transactions, history)
        val detectedAnomalies = results.zip(transactions)
            .filter { it.first.isAnomaly }
            .map { it.second.id }
            .toSet()

        val falsePositives = detectedAnomalies - knownAnomalies
        return if (detectedAnomalies.isNotEmpty()) {
            falsePositives.size.toDouble() / detectedAnomalies.size
        } else {
            0.0
        }
    }
}

fun main() {
    val normalHistory = listOf(
        Transaction("t1", 250.0, "João Silva", 1000),
        Transaction("t2", 320.0, "João Silva", 2000),
        Transaction("t3", 280.0, "João Silva", 3000),
        Transaction("t4", 310.0, "João Silva", 4000),
        Transaction("t5", 290.0, "João Silva", 5000),
        Transaction("t6", 305.0, "João Silva", 6000),
        Transaction("t7", 275.0, "João Silva", 7000),
        Transaction("t8", 295.0, "João Silva", 8000)
    )

    val testTransactions = listOf(
        Transaction("test1", 300.0, "João Silva", 9000),
        Transaction("test2", 5000.0, "João Silva", 10000),
        Transaction("test3", 280.0, "João Silva", 11000)
    )

    println("=== Sistema de Deteccao de Anomalias ===\n")

    println("--- Estrategia: Z-Score ---")
    val zScoreDetector = AnomalyDetector(ZScoreStrategy(threshold = 2.5))
    testTransactions.forEach { transaction ->
        val result = zScoreDetector.detect(transaction, normalHistory)
        println("  Transacao: ${transaction.id} - R$ %.2f".format(transaction.amount))
        println("  Anomalia: ${result.isAnomaly}")
        println("  Score: %.2f".format(result.score))
        println("  ${result.details}\n")
    }

    println("--- Estrategia: IQR ---")
    val iqrDetector = AnomalyDetector(IQRStrategy(multiplier = 1.5))
    testTransactions.forEach { transaction ->
        val result = iqrDetector.detect(transaction, normalHistory)
        println("  Transacao: ${transaction.id} - R$ %.2f".format(transaction.amount))
        println("  Anomalia: ${result.isAnomaly}")
        println("  Score: %.2f".format(result.score))
        println("  ${result.details}\n")
    }

    println("--- Estrategia: Moving Average ---")
    val maDetector = AnomalyDetector(MovingAverageStrategy(windowSize = 5, threshold = 2.0))
    testTransactions.forEach { transaction ->
        val result = maDetector.detect(transaction, normalHistory)
        println("  Transacao: ${transaction.id} - R$ %.2f".format(transaction.amount))
        println("  Anomalia: ${result.isAnomaly}")
        println("  Score: %.2f".format(result.score))
        println("  ${result.details}\n")
    }

    println("--- Contagem de Anomalias ---")
    println("  Z-Score: ${zScoreDetector.anomalyCount(testTransactions, normalHistory)} anomalias")
    println("  IQR: ${iqrDetector.anomalyCount(testTransactions, normalHistory)} anomalias")
    println("  Moving Average: ${maDetector.anomalyCount(testTransactions, normalHistory)} anomalias")
}
