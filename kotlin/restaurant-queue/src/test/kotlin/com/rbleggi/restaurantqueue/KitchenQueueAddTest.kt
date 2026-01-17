package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class KitchenQueueAddTest {
    @Test
    fun `addCommand prints confirmation`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        val queue = KitchenQueue()
        queue.addCommand(BurgerCommand())
        assertTrue(output.toString().contains("Added Burger to the queue"))
    }
}
