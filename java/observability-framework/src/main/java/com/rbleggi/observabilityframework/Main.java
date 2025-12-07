package com.rbleggi.observabilityframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Observability Core Framework POC...");

        var msStrategy = new MillisecondsLatencyStrategy();
        var requestTrackerMs = MetricManager.getTracker("httpRequestLatencyMs", msStrategy);

        System.out.println("\nRecording latencies with Milliseconds Strategy:");
        for (int i = 1; i <= 5; i++) {
            var startTime = System.nanoTime();
            try {
                Thread.sleep(100 + i * 10L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            var endTime = System.nanoTime();
            requestTrackerMs.recordLatency(startTime, endTime);
            System.out.println("  Recorded latency for request " + i + ": " + msStrategy.calculateLatency(startTime, endTime) + " ms");
        }

        System.out.println("Average HTTP Request Latency (ms): " + requestTrackerMs.getAverageLatency());
        System.out.println("All HTTP Request Latencies (ms): " + requestTrackerMs.getAllLatencies());

        var nsStrategy = new NanosecondsLatencyStrategy();
        var dbQueryTrackerNs = MetricManager.getTracker("dbQueryLatencyNs", nsStrategy);

        System.out.println("\nRecording latencies with Nanoseconds Strategy:");
        for (int i = 1; i <= 3; i++) {
            var startTime = System.nanoTime();
            var dummy = java.util.stream.IntStream.rangeClosed(1, 1000).map(n -> n * 2).sum();
            var endTime = System.nanoTime();
            dbQueryTrackerNs.recordLatency(startTime, endTime);
            System.out.println("  Recorded latency for DB query " + i + ": " + nsStrategy.calculateLatency(startTime, endTime) + " ns");
        }

        System.out.println("Average DB Query Latency (ns): " + dbQueryTrackerNs.getAverageLatency());
        System.out.println("All DB Query Latencies (ns): " + dbQueryTrackerNs.getAllLatencies());

        System.out.println("\nAll registered trackers:");
        MetricManager.getAllTrackerNames().forEach(name -> System.out.println("- " + name));
    }
}

interface LatencyStrategy {
    long calculateLatency(long startTime, long endTime);
}

class MillisecondsLatencyStrategy implements LatencyStrategy {
    @Override
    public long calculateLatency(long startTime, long endTime) {
        return (endTime - startTime) / 1_000_000L;
    }
}

class NanosecondsLatencyStrategy implements LatencyStrategy {
    @Override
    public long calculateLatency(long startTime, long endTime) {
        return endTime - startTime;
    }
}

class LatencyTracker {
    private final LatencyStrategy strategy;
    private final List<Long> latencies = new ArrayList<>();

    LatencyTracker(LatencyStrategy strategy) {
        this.strategy = strategy;
    }

    void recordLatency(long startTime, long endTime) {
        var latency = strategy.calculateLatency(startTime, endTime);
        latencies.add(latency);
    }

    double getAverageLatency() {
        return latencies.isEmpty() ? 0.0 : latencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
    }

    List<Long> getAllLatencies() {
        return new ArrayList<>(latencies);
    }
}

class MetricManager {
    private static final Map<String, LatencyTracker> trackers = new HashMap<>();

    static LatencyTracker getTracker(String name, LatencyStrategy strategy) {
        return trackers.computeIfAbsent(name, k -> new LatencyTracker(strategy));
    }

    static void resetTracker(String name) {
        trackers.remove(name);
    }

    static List<String> getAllTrackerNames() {
        return new ArrayList<>(trackers.keySet());
    }
}
