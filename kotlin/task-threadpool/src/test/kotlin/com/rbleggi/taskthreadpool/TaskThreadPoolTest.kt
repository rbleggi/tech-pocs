package com.rbleggi.taskthreadpool

import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TaskThreadPoolTest {
    @Test
    fun testTaskExecution() {
        val pool = TaskThreadPool(4)
        val counter = AtomicInteger(0)

        (1..10).forEach { _ ->
            pool.submit(object : Task {
                override fun run() {
                    counter.incrementAndGet()
                }
            })
        }

        Thread.sleep(500)
        pool.shutdown()

        assertEquals(10, counter.get())
    }

    @Test
    fun testShutdown() {
        val pool = TaskThreadPool(2)
        pool.shutdown()

        assertFailsWith<IllegalStateException> {
            pool.submit(object : Task {
                override fun run() {}
            })
        }
    }
}
