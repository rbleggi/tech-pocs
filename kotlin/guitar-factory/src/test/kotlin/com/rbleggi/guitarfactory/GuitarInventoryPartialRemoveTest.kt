package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GuitarInventoryPartialRemoveTest {
    @Test
    fun `removeGuitar decreases quantity`() {
        val guitar = Guitar.Builder().model("TestPartial").build()
        GuitarInventory.addGuitar(guitar, 5)
        GuitarInventory.removeGuitar(guitar, 2)
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        GuitarInventory.listInventory()
        assertTrue(output.toString().contains("Quantity: 3"))
    }
}
