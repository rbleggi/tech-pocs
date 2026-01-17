package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PastaCommandExecuteTest {
    @Test
    fun `execute prints preparation message`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        PastaCommand().execute()
        assertTrue(output.toString().contains("Preparing Pasta"))
    }

    @Test
    fun `execute includes prep time`() {
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        PastaCommand().execute()
        assertTrue(output.toString().contains("15 minutes"))
    }
}
