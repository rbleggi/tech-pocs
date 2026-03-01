package com.rbleggi.hibernateslowquerydetector

interface QueryObserver {
    fun notify(query: String, durationMs: Long)
}

class SlowQueryDetector(private val thresholdMs: Long) {
    private val observers = mutableListOf<QueryObserver>()

    fun addObserver(observer: QueryObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: QueryObserver) {
        observers.remove(observer)
    }

    fun executeQuery(query: String, simulatedDurationMs: Long) {
        if (simulatedDurationMs > thresholdMs) {
            observers.forEach { it.notify(query, simulatedDurationMs) }
        }
    }
}

class ConsoleLogger : QueryObserver {
    override fun notify(query: String, durationMs: Long) {
        println("SLOW QUERY DETECTED: [$query] took $durationMs ms")
    }
}

fun main() {
    println("Hibernate Slow Query Detector")
}
