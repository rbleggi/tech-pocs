package com.rbleggi.observabilityframework

import scala.collection.mutable.ListBuffer

trait LatencyStrategy:
  def calculateLatency(startTime: Long, endTime: Long): Long

class MillisecondsLatencyStrategy extends LatencyStrategy:
  override def calculateLatency(startTime: Long, endTime: Long): Long =
    (endTime - startTime) / 1_000_000L

class NanosecondsLatencyStrategy extends LatencyStrategy:
  override def calculateLatency(startTime: Long, endTime: Long): Long =
    endTime - startTime

class LatencyTracker(strategy: LatencyStrategy):
  private val latencies = ListBuffer[Long]()

  def recordLatency(startTime: Long, endTime: Long): Unit =
    val latency = strategy.calculateLatency(startTime, endTime)
    latencies += latency

  def getAverageLatency: Double =
    if latencies.isEmpty then 0.0
    else latencies.sum.toDouble / latencies.size

  def getAllLatencies: List[Long] = latencies.toList

object MetricManager:
  private val trackers = scala.collection.mutable.Map[String, LatencyTracker]()

  def getTracker(name: String, strategy: LatencyStrategy): LatencyTracker =
    trackers.getOrElseUpdate(name, new LatencyTracker(strategy))

  def resetTracker(name: String): Unit =
    trackers.remove(name)

  def getAllTrackerNames: List[String] = trackers.keys.toList

@main def run(): Unit =
  println("Observability Framework")
