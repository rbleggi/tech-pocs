package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GuitarInventoryQuantityTest {
    @Test
    fun `addGuitar with quantity shows correct amount`() {
        val guitar = Guitar.Builder().model("TestQty").build()
        GuitarInventory.addGuitar(guitar, 5)
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        GuitarInventory.listInventory()
        assertTrue(output.toString().contains("Quantity: 5"))
    }
}
