package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class KitchenQueueEmptyTest {
    @Test
    fun `runQueue with empty queue prints zero time`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        val queue = KitchenQueue()
        queue.runQueue()
        assertTrue(output.toString().contains("Total preparation time: 0 minutes"))
    }
}
