package com.rbleggi.hibernate

import org.scalatest.funsuite.AnyFunSuite
import com.rbleggi.hibernateslowquerydetector.{QueryObserver, SlowQueryDetector}

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

  test("SlowQueryDetector should not notify for queries at exact threshold") {
    var notified: Option[(String, Long)] = None
    val observer = new QueryObserver {
      def notify(query: String, durationMs: Long): Unit = {
        notified = Some((query, durationMs))
      }
    }
    val detector = new SlowQueryDetector(thresholdMs = 100)
    detector.addObserver(observer)
    detector.executeQuery("SELECT * FROM products", 100)
    assert(notified.isEmpty)
  }

  test("SlowQueryDetector should notify multiple observers") {
    var notified1: Option[(String, Long)] = None
    var notified2: Option[(String, Long)] = None

    val observer1 = new QueryObserver {
      def notify(query: String, durationMs: Long): Unit = {
        notified1 = Some((query, durationMs))
      }
    }
    val observer2 = new QueryObserver {
      def notify(query: String, durationMs: Long): Unit = {
        notified2 = Some((query, durationMs))
      }
    }

    val detector = new SlowQueryDetector(thresholdMs = 100)
    detector.addObserver(observer1)
    detector.addObserver(observer2)

    detector.executeQuery("SELECT * FROM customers", 150)
    assert(notified1.contains(("SELECT * FROM customers", 150)))
    assert(notified2.contains(("SELECT * FROM customers", 150)))
  }

  test("SlowQueryDetector should remove observer") {
    var notified: Option[(String, Long)] = None
    val observer = new QueryObserver {
      def notify(query: String, durationMs: Long): Unit = {
        notified = Some((query, durationMs))
      }
    }

    val detector = new SlowQueryDetector(thresholdMs = 100)
    detector.addObserver(observer)
    detector.removeObserver(observer)

    detector.executeQuery("SELECT * FROM items", 200)
    assert(notified.isEmpty)
  }

  test("SlowQueryDetector should handle zero threshold") {
    var notificationCount = 0
    val observer = new QueryObserver {
      def notify(query: String, durationMs: Long): Unit = {
        notificationCount += 1
      }
    }

    val detector = new SlowQueryDetector(thresholdMs = 0)
    detector.addObserver(observer)

    detector.executeQuery("SELECT 1", 1)
    detector.executeQuery("SELECT 2", 10)
    assert(notificationCount == 2)
  }
}
