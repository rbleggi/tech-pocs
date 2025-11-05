package com.rbleggi.observabilityframework

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObservabilityTest {

    @BeforeEach
    fun setup() {
        MetricManager.getAllTrackerNames().forEach { MetricManager.resetTracker(it) }
    }

    @Test
    fun testMillisecondsStrategyCalculation() {
        val strategy = MillisecondsLatencyStrategy()
        val startTime = 1_000_000_000L
        val endTime = 1_150_000_000L

        val result = strategy.calculateLatency(startTime, endTime)

        assertEquals(150L, result)
    }

    @Test
    fun testNanosecondsStrategyCalculation() {
        val strategy = NanosecondsLatencyStrategy()
        val startTime = 1_000_000L
        val endTime = 1_500_000L

        val result = strategy.calculateLatency(startTime, endTime)

        assertEquals(500_000L, result)
    }

    @Test
    fun testLatencyTrackerRecordsCorrectly() {
        val strategy = MillisecondsLatencyStrategy()
        val tracker = LatencyTracker(strategy)

        val start = System.nanoTime()
        Thread.sleep(10)
        val end = System.nanoTime()

        tracker.recordLatency(start, end)

        assertTrue(tracker.getAllLatencies().isNotEmpty())
        assertTrue(tracker.getAverageLatency() >= 10.0)
    }

    @Test
    fun testLatencyTrackerAverageWithMultipleValues() {
        val strategy = NanosecondsLatencyStrategy()
        val tracker = LatencyTracker(strategy)

        tracker.recordLatency(1000L, 2000L)
        tracker.recordLatency(1000L, 3000L)
        tracker.recordLatency(1000L, 4000L)

        assertEquals(2000.0, tracker.getAverageLatency())
        assertEquals(listOf(1000L, 2000L, 3000L), tracker.getAllLatencies())
    }

    @Test
    fun testLatencyTrackerEmptyAverage() {
        val strategy = MillisecondsLatencyStrategy()
        val tracker = LatencyTracker(strategy)

        assertEquals(0.0, tracker.getAverageLatency())
        assertTrue(tracker.getAllLatencies().isEmpty())
    }

    @Test
    fun testMetricManagerGetOrCreateTracker() {
        val strategy = NanosecondsLatencyStrategy()
        val tracker = MetricManager.getTracker("testTracker", strategy)

        assertTrue(MetricManager.getAllTrackerNames().contains("testTracker"))
        assertEquals(tracker, MetricManager.getTracker("testTracker", strategy))
    }

    @Test
    fun testMetricManagerResetTracker() {
        val strategy = MillisecondsLatencyStrategy()
        MetricManager.getTracker("resetTest", strategy)

        assertTrue(MetricManager.getAllTrackerNames().contains("resetTest"))

        MetricManager.resetTracker("resetTest")

        assertTrue(!MetricManager.getAllTrackerNames().contains("resetTest"))
    }

    @Test
    fun testMetricManagerMultipleTrackers() {
        val msStrategy = MillisecondsLatencyStrategy()
        val nsStrategy = NanosecondsLatencyStrategy()

        MetricManager.getTracker("tracker1", msStrategy)
        MetricManager.getTracker("tracker2", nsStrategy)

        val trackerNames = MetricManager.getAllTrackerNames()
        assertEquals(2, trackerNames.size)
        assertTrue(trackerNames.contains("tracker1"))
        assertTrue(trackerNames.contains("tracker2"))
    }
}
