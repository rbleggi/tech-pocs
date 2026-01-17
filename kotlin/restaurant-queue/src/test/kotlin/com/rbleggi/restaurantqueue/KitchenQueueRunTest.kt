package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class KitchenQueueRunTest {
    @Test
    fun `runQueue prints total time`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        val queue = KitchenQueue()
        queue.addCommand(BurgerCommand())
        queue.runQueue()
        assertTrue(output.toString().contains("Total preparation time: 10 minutes"))
    }
}
