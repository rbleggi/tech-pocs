package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GuitarInventoryAddTest {
    @Test
    fun `addGuitar adds to inventory`() {
        val guitar = Guitar.Builder().model("TestAdd").build()
        GuitarInventory.addGuitar(guitar, 1)
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        GuitarInventory.listInventory()
        assertTrue(output.toString().contains("TestAdd"))
    }
}
