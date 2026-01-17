package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class SaladCommandExecuteTest {
    @Test
    fun `execute prints preparation message`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        SaladCommand().execute()
        assertTrue(output.toString().contains("Preparing Salad"))
    }

    @Test
    fun `execute includes prep time`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        SaladCommand().execute()
        assertTrue(output.toString().contains("5 minutes"))
    }
}
