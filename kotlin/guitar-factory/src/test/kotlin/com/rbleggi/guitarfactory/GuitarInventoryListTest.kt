package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GuitarInventoryListTest {
    @Test
    fun `listInventory prints current inventory header`() {
        val guitar = Guitar.Builder().model("TestList").build()
        GuitarInventory.addGuitar(guitar, 1)
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        GuitarInventory.listInventory()
        assertTrue(output.toString().contains("Current Inventory"))
    }
}
