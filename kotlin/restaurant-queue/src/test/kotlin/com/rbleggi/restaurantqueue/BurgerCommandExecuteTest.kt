package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class BurgerCommandExecuteTest {
    @Test
    fun `execute prints preparation message`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        BurgerCommand().execute()
        assertTrue(output.toString().contains("Preparing Burger"))
    }

    @Test
    fun `execute includes prep time`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        BurgerCommand().execute()
        assertTrue(output.toString().contains("10 minutes"))
    }
}
