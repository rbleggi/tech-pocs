package com.rbleggi.hibernateslowquerydetector

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SlowQueryDetectorTest {
    @Test
    fun testSlowQueryDetection() {
        val detector = SlowQueryDetector(thresholdMs = 100)
        var notificationCount = 0

        detector.addObserver(object : QueryObserver {
            override fun notify(query: String, durationMs: Long) {
                notificationCount++
            }
        })

        detector.executeQuery("SELECT * FROM users", 50)
        detector.executeQuery("SELECT * FROM orders", 200)
        detector.executeQuery("UPDATE products SET price = 10", 300)

        assertEquals(2, notificationCount)
    }
}
