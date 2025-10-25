package com.rbleggi.observabilityframework

class ObservabilityFrameworkSpec {
  test("MillisecondsLatencyStrategy should calculate latency in milliseconds") {
    val strategy = new MillisecondsLatencyStrategy()
    val startTime = 0L
    val endTime = 5_000_000L
    val latency = strategy.calculateLatency(startTime, endTime)
    assert(latency == 5L)
  }

  test("NanosecondsLatencyStrategy should calculate latency in nanoseconds") {
    val strategy = new NanosecondsLatencyStrategy()
    val startTime = 0L
    val endTime = 5_000_000L
    val latency = strategy.calculateLatency(startTime, endTime)
    assert(latency == 5_000_000L)
  }

  test("LatencyTracker should record and calculate average latency") {
    val strategy = new MillisecondsLatencyStrategy()
    val tracker = new LatencyTracker(strategy)

    tracker.recordLatency(0L, 10_000_000L)
    tracker.recordLatency(0L, 20_000_000L)
    tracker.recordLatency(0L, 30_000_000L)

    assert(tracker.getAverageLatency == 20.0)
  }

  test("LatencyTracker should return all recorded latencies") {
    val strategy = new MillisecondsLatencyStrategy()
    val tracker = new LatencyTracker(strategy)

    tracker.recordLatency(0L, 10_000_000L)
    tracker.recordLatency(0L, 20_000_000L)

    val latencies = tracker.getAllLatencies
    assert(latencies.length == 2)
    assert(latencies.contains(10L))
    assert(latencies.contains(20L))
  }

  test("LatencyTracker should return 0.0 average for empty tracker") {
    val strategy = new MillisecondsLatencyStrategy()
    val tracker = new LatencyTracker(strategy)

    assert(tracker.getAverageLatency == 0.0)
  }

  test("MetricManager should store and retrieve trackers") {
    val strategy = new MillisecondsLatencyStrategy()
    val tracker = MetricManager.getTracker("test-tracker", strategy)

    tracker.recordLatency(0L, 10_000_000L)

    val sameTracker = MetricManager.getTracker("test-tracker", strategy)
    assert(sameTracker.getAllLatencies.length == 1)

    MetricManager.resetTracker("test-tracker")
  }

  test("MetricManager should reset tracker") {
    val strategy = new MillisecondsLatencyStrategy()
    val tracker = MetricManager.getTracker("test-reset", strategy)
    tracker.recordLatency(0L, 10_000_000L)

    MetricManager.resetTracker("test-reset")
    val newTracker = MetricManager.getTracker("test-reset", strategy)

    assert(newTracker.getAllLatencies.isEmpty)
    MetricManager.resetTracker("test-reset")
  }

  test("MetricManager should list all tracker names") {
    MetricManager.resetTracker("tracker1")
    MetricManager.resetTracker("tracker2")

    val strategy = new MillisecondsLatencyStrategy()
    MetricManager.getTracker("tracker1", strategy)
    MetricManager.getTracker("tracker2", strategy)

    val names = MetricManager.getAllTrackerNames
    assert(names.contains("tracker1"))
    assert(names.contains("tracker2"))

    MetricManager.resetTracker("tracker1")
    MetricManager.resetTracker("tracker2")
  }
}

