package com.rbleggi.observabilityframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Observability Framework");
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
