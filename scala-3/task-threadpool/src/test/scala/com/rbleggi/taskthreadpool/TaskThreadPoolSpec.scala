package com.rbleggi.taskthreadpool

import java.util.concurrent.atomic.AtomicInteger
import org.scalatest.funsuite.AnyFunSuite

class TaskThreadPoolSpec extends AnyFunSuite {

  test("TaskThreadPool should execute all submitted tasks") {
    val pool = new TaskThreadPool(2)
    val counter = new AtomicInteger(0)
    val totalTasks = 5
    (1 to totalTasks).foreach { _ =>
      pool.submit(new Task {
        def run(): Unit = counter.incrementAndGet()
      })
    }
    val maxWaitMs = 2000
    val start = System.currentTimeMillis()
    while (counter.get() < totalTasks && System.currentTimeMillis() - start < maxWaitMs) {
      Thread.sleep(10)
    }
    pool.shutdown()
    assert(counter.get() == totalTasks)
  }
}
