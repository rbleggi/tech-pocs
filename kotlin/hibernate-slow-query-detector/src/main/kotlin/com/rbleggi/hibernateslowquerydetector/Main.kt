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
    val detector = SlowQueryDetector(thresholdMs = 100)
    detector.addObserver(ConsoleLogger())

    detector.executeQuery("SELECT * FROM users", 50)
    detector.executeQuery("SELECT * FROM orders", 200)
    detector.executeQuery("UPDATE products SET price = 10", 300)
}
