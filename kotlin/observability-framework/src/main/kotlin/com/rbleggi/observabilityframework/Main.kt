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
    println("Starting Observability Core Framework POC...")

    val msStrategy = MillisecondsLatencyStrategy()
    val requestTrackerMs = MetricManager.getTracker("httpRequestLatencyMs", msStrategy)

    println("\nRecording latencies with Milliseconds Strategy:")
    for (i in 1..5) {
        val startTime = System.nanoTime()
        Thread.sleep((100 + i * 10).toLong())
        val endTime = System.nanoTime()
        requestTrackerMs.recordLatency(startTime, endTime)
        println("  Recorded latency for request $i: ${msStrategy.calculateLatency(startTime, endTime)} ms")
    }

    println("Average HTTP Request Latency (ms): ${requestTrackerMs.getAverageLatency()}")
    println("All HTTP Request Latencies (ms): ${requestTrackerMs.getAllLatencies()}")

    val nsStrategy = NanosecondsLatencyStrategy()
    val dbQueryTrackerNs = MetricManager.getTracker("dbQueryLatencyNs", nsStrategy)

    println("\nRecording latencies with Nanoseconds Strategy:")
    for (i in 1..3) {
        val startTime = System.nanoTime()
        val dummy = (1..1000).map { it * 2 }.sum()
        val endTime = System.nanoTime()
        dbQueryTrackerNs.recordLatency(startTime, endTime)
        println("  Recorded latency for DB query $i: ${nsStrategy.calculateLatency(startTime, endTime)} ns")
    }

    println("Average DB Query Latency (ns): ${dbQueryTrackerNs.getAverageLatency()}")
    println("All DB Query Latencies (ns): ${dbQueryTrackerNs.getAllLatencies()}")

    println("\nAll registered trackers:")
    MetricManager.getAllTrackerNames().forEach { name -> println("- $name") }
}
