package com.rbleggi.observabilityframework

interface LatencyStrategy {
    fun calculateLatency(startTime: Long, endTime: Long): Long
}

class MillisecondsLatencyStrategy : LatencyStrategy {
    override fun calculateLatency(startTime: Long, endTime: Long): Long {
        return (endTime - startTime) / 1_000_000L
    }
}

class NanosecondsLatencyStrategy : LatencyStrategy {
    override fun calculateLatency(startTime: Long, endTime: Long): Long {
        return endTime - startTime
    }
}

class LatencyTracker(private val strategy: LatencyStrategy) {
    private val latencies = mutableListOf<Long>()

    fun recordLatency(startTime: Long, endTime: Long) {
        val latency = strategy.calculateLatency(startTime, endTime)
        latencies.add(latency)
    }

    fun getAverageLatency(): Double {
        return if (latencies.isEmpty()) 0.0 else latencies.average()
    }

    fun getAllLatencies(): List<Long> = latencies.toList()
}

object MetricManager {
    private val trackers = mutableMapOf<String, LatencyTracker>()

    fun getTracker(name: String, strategy: LatencyStrategy): LatencyTracker {
        return trackers.getOrPut(name) { LatencyTracker(strategy) }
    }

    fun resetTracker(name: String) {
        trackers.remove(name)
    }

    fun getAllTrackerNames(): List<String> = trackers.keys.toList()
}

fun main() {
    println("Observability Framework")
}
