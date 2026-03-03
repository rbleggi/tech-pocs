package com.rbleggi.hibernateslowquerydetector

trait QueryObserver {
  def notify(query: String, durationMs: Long): Unit
}

class SlowQueryDetector(thresholdMs: Long) {
  private var observers: List[QueryObserver] = Nil

  def addObserver(observer: QueryObserver): Unit = {
    observers = observer :: observers
  }

  def removeObserver(observer: QueryObserver): Unit = {
    observers = observers.filterNot(_ == observer)
  }

  def executeQuery(query: String, simulatedDurationMs: Long): Unit = {
    if (simulatedDurationMs > thresholdMs) {
      observers.foreach(_.notify(query, simulatedDurationMs))
    }
  }
}

class ConsoleLogger extends QueryObserver {
  def notify(query: String, durationMs: Long): Unit = {
    println(s"SLOW QUERY DETECTED: [$query] took $durationMs ms")
  }
}

@main def run(): Unit =
  println("Hibernate Slow Query Detector")
