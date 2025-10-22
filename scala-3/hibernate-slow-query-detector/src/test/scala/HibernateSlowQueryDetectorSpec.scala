package com.rbleggi.hibernate

import org.scalatest.funsuite.AnyFunSuite

class HibernateSlowQueryDetectorSpec extends AnyFunSuite {
  test("SlowQueryDetector notifies observer only for slow queries") {
    var notified: Option[(String, Long)] = None
    val observer = new QueryObserver {
      def notify(query: String, durationMs: Long): Unit = {
        notified = Some((query, durationMs))
      }
    }
    val detector = new SlowQueryDetector(thresholdMs = 100)
    detector.addObserver(observer)
    detector.executeQuery("SELECT * FROM users", 50)
    assert(notified.isEmpty)
    detector.executeQuery("SELECT * FROM orders", 200)
    assert(notified.contains(("SELECT * FROM orders", 200)))
  }
}
