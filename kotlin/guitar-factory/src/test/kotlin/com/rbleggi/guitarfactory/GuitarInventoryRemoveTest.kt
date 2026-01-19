package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertFalse
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GuitarInventoryRemoveTest {
    @Test
    fun `removeGuitar removes from inventory`() {
        val guitar = Guitar.Builder().model("TestRemove").build()
        GuitarInventory.addGuitar(guitar, 1)
        GuitarInventory.removeGuitar(guitar, 1)
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        GuitarInventory.listInventory()
        assertFalse(output.toString().contains("TestRemove"))
    }
}
