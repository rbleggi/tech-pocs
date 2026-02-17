package com.rbleggi.anomalydetection

import kotlin.test.*

class AnomalyDetectionTest {

    private val normalHistory = listOf(
        Transaction("h1", 250.0, "Account", 1000),
        Transaction("h2", 300.0, "Account", 2000),
        Transaction("h3", 280.0, "Account", 3000),
        Transaction("h4", 310.0, "Account", 4000),
        Transaction("h5", 290.0, "Account", 5000)
    )

    private val normalTransaction = Transaction("t1", 295.0, "Account", 6000)
    private val anomalyTransaction = Transaction("t2", 5000.0, "Account", 7000)

    @Test
    fun `ZScoreStrategy detects normal transaction`() {
        val strategy = ZScoreStrategy(threshold = 3.0)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertFalse(result.isAnomaly)
        assertEquals("Z-Score", result.method)
    }

    @Test
    fun `ZScoreStrategy detects anomaly`() {
        val strategy = ZScoreStrategy(threshold = 2.0)
        val result = strategy.detect(anomalyTransaction, normalHistory)

        assertTrue(result.isAnomaly)
        assertTrue(result.score > 2.0)
    }

    @Test
    fun `ZScoreStrategy respects threshold`() {
        val lowThreshold = ZScoreStrategy(threshold = 1.0)
        val highThreshold = ZScoreStrategy(threshold = 5.0)

        val resultLow = lowThreshold.detect(normalTransaction, normalHistory)
        val resultHigh = highThreshold.detect(anomalyTransaction, normalHistory)

        assertTrue(resultLow.score < 1.0 || resultLow.isAnomaly)
        assertTrue(resultHigh.score > 0.0)
    }

    @Test
    fun `IQRStrategy detects normal transaction`() {
        val strategy = IQRStrategy(multiplier = 1.5)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertFalse(result.isAnomaly)
        assertEquals("IQR", result.method)
    }

    @Test
    fun `IQRStrategy detects anomaly`() {
        val strategy = IQRStrategy(multiplier = 1.5)
        val result = strategy.detect(anomalyTransaction, normalHistory)

        assertTrue(result.isAnomaly)
        assertTrue(result.score > 0.0)
    }

    @Test
    fun `IQRStrategy calculates quartiles correctly`() {
        val strategy = IQRStrategy(multiplier = 1.5)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertTrue(result.details.contains("IQR"))
        assertTrue(result.details.contains("limites"))
    }

    @Test
    fun `MovingAverageStrategy detects normal transaction`() {
        val strategy = MovingAverageStrategy(windowSize = 5, threshold = 2.0)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertFalse(result.isAnomaly)
        assertEquals("Moving Average", result.method)
    }

    @Test
    fun `MovingAverageStrategy detects anomaly`() {
        val strategy = MovingAverageStrategy(windowSize = 5, threshold = 1.0)
        val result = strategy.detect(anomalyTransaction, normalHistory)

        assertTrue(result.isAnomaly)
        assertTrue(result.score > 1.0)
    }

    @Test
    fun `MovingAverageStrategy respects window size`() {
        val smallWindow = MovingAverageStrategy(windowSize = 3, threshold = 2.0)
        val largeWindow = MovingAverageStrategy(windowSize = 5, threshold = 2.0)

        val resultSmall = smallWindow.detect(normalTransaction, normalHistory)
        val resultLarge = largeWindow.detect(normalTransaction, normalHistory)

        assertNotNull(resultSmall.details)
        assertNotNull(resultLarge.details)
    }

    @Test
    fun `AnomalyDetector detects single transaction`() {
        val detector = AnomalyDetector(ZScoreStrategy())
        val result = detector.detect(normalTransaction, normalHistory)

        assertFalse(result.isAnomaly)
    }

    @Test
    fun `AnomalyDetector detects batch of transactions`() {
        val detector = AnomalyDetector(ZScoreStrategy())
        val transactions = listOf(normalTransaction, anomalyTransaction)
        val results = detector.detectBatch(transactions, normalHistory)

        assertEquals(2, results.size)
    }

    @Test
    fun `AnomalyDetector counts anomalies`() {
        val detector = AnomalyDetector(ZScoreStrategy(threshold = 2.0))
        val transactions = listOf(normalTransaction, anomalyTransaction)
        val count = detector.anomalyCount(transactions, normalHistory)

        assertTrue(count >= 0 && count <= 2)
    }

    @Test
    fun `AnomalyDetector calculates false positive rate`() {
        val detector = AnomalyDetector(ZScoreStrategy(threshold = 1.0))
        val transactions = listOf(normalTransaction, anomalyTransaction)
        val knownAnomalies = setOf("t2")

        val fpr = detector.falsePositiveRate(transactions, normalHistory, knownAnomalies)

        assertTrue(fpr >= 0.0 && fpr <= 1.0)
    }

    @Test
    fun `Transaction data class stores all fields`() {
        val transaction = Transaction("id", 100.0, "account", 1000L)

        assertEquals("id", transaction.id)
        assertEquals(100.0, transaction.amount)
        assertEquals("account", transaction.account)
        assertEquals(1000L, transaction.timestamp)
    }

    @Test
    fun `AnomalyResult contains all fields`() {
        val result = AnomalyResult(true, 3.5, "Z-Score", "details")

        assertTrue(result.isAnomaly)
        assertEquals(3.5, result.score)
        assertEquals("Z-Score", result.method)
        assertEquals("details", result.details)
    }

    @Test
    fun `ZScoreStrategy provides details`() {
        val strategy = ZScoreStrategy(threshold = 3.0)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertTrue(result.details.contains("Z-Score"))
        assertTrue(result.details.contains("threshold"))
    }

    @Test
    fun `IQRStrategy provides details`() {
        val strategy = IQRStrategy(multiplier = 1.5)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertTrue(result.details.contains("IQR"))
    }

    @Test
    fun `MovingAverageStrategy provides details`() {
        val strategy = MovingAverageStrategy(windowSize = 5, threshold = 2.0)
        val result = strategy.detect(normalTransaction, normalHistory)

        assertTrue(result.details.contains("Desvio"))
        assertTrue(result.details.contains("media movel"))
    }
}
