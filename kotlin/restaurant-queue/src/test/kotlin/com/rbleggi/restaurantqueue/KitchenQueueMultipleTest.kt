package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class KitchenQueueMultipleTest {
    @Test
    fun `runQueue calculates total for multiple commands`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        val queue = KitchenQueue()
        queue.addCommand(BurgerCommand())
        queue.addCommand(PastaCommand())
        queue.addCommand(SaladCommand())
        queue.runQueue()
        assertTrue(output.toString().contains("Total preparation time: 30 minutes"))
    }
}
