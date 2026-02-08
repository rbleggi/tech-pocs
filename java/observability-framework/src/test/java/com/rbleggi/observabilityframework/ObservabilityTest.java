package com.rbleggi.observabilityframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservabilityTest {

    @BeforeEach
    void setUp() {
        MetricManager.getAllTrackerNames().forEach(MetricManager::resetTracker);
    }

    @Test
    @DisplayName("MillisecondsLatencyStrategy should calculate latency in milliseconds")
    void millisecondsLatencyStrategy_calculatesInMilliseconds() {
        var strategy = new MillisecondsLatencyStrategy();
        long startTime = 1000_000_000L;
        long endTime = 1100_000_000L;
        long latency = strategy.calculateLatency(startTime, endTime);
        assertEquals(100, latency);
    }

    @Test
    @DisplayName("NanosecondsLatencyStrategy should calculate latency in nanoseconds")
    void nanosecondsLatencyStrategy_calculatesInNanoseconds() {
        var strategy = new NanosecondsLatencyStrategy();
        long startTime = 1000L;
        long endTime = 2000L;
        long latency = strategy.calculateLatency(startTime, endTime);
        assertEquals(1000, latency);
    }

    @Test
    @DisplayName("LatencyTracker should record single latency")
    void latencyTracker_recordSingle_storesLatency() {
        var strategy = new MillisecondsLatencyStrategy();
        var tracker = new LatencyTracker(strategy);
        long startTime = System.nanoTime();
        long endTime = startTime + 100_000_000L;
        tracker.recordLatency(startTime, endTime);
        assertEquals(1, tracker.getAllLatencies().size());
    }

    @Test
    @DisplayName("LatencyTracker should calculate average of multiple latencies")
    void latencyTracker_multipleLatencies_calculatesAverage() {
        var strategy = new NanosecondsLatencyStrategy();
        var tracker = new LatencyTracker(strategy);
        tracker.recordLatency(0, 100);
        tracker.recordLatency(0, 200);
        tracker.recordLatency(0, 300);
        double average = tracker.getAverageLatency();
        assertEquals(200.0, average, 0.1);
    }

    @Test
    @DisplayName("LatencyTracker should return 0 average for empty tracker")
    void latencyTracker_empty_returnsZeroAverage() {
        var strategy = new MillisecondsLatencyStrategy();
        var tracker = new LatencyTracker(strategy);
        assertEquals(0.0, tracker.getAverageLatency());
    }

    @Test
    @DisplayName("MetricManager should create and retrieve tracker")
    void metricManager_getTracker_createsAndRetrieves() {
        var strategy = new MillisecondsLatencyStrategy();
        var tracker1 = MetricManager.getTracker("test_metric", strategy);
        var tracker2 = MetricManager.getTracker("test_metric", strategy);
        assertSame(tracker1, tracker2);
    }

    @Test
    @DisplayName("MetricManager should track multiple trackers")
    void metricManager_multipleTrackers_tracksAll() {
        var msStrategy = new MillisecondsLatencyStrategy();
        var nsStrategy = new NanosecondsLatencyStrategy();
        MetricManager.getTracker("metric1", msStrategy);
        MetricManager.getTracker("metric2", nsStrategy);
        var names = MetricManager.getAllTrackerNames();
        assertTrue(names.contains("metric1"));
        assertTrue(names.contains("metric2"));
    }

    @Test
    @DisplayName("MetricManager should reset tracker")
    void metricManager_resetTracker_removesTracker() {
        var strategy = new MillisecondsLatencyStrategy();
        MetricManager.getTracker("to_reset", strategy);
        assertTrue(MetricManager.getAllTrackerNames().contains("to_reset"));
        MetricManager.resetTracker("to_reset");
        assertFalse(MetricManager.getAllTrackerNames().contains("to_reset"));
    }

    @Test
    @DisplayName("LatencyTracker should store all recorded latencies")
    void latencyTracker_getAllLatencies_returnsAllRecorded() {
        var strategy = new NanosecondsLatencyStrategy();
        var tracker = new LatencyTracker(strategy);
        tracker.recordLatency(0, 100);
        tracker.recordLatency(0, 200);
        tracker.recordLatency(0, 300);
        var latencies = tracker.getAllLatencies();
        assertEquals(3, latencies.size());
        assertTrue(latencies.contains(100L));
        assertTrue(latencies.contains(200L));
        assertTrue(latencies.contains(300L));
    }
}
