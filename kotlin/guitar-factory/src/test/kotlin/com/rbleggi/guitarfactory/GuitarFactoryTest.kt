package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GuitarFactoryTest {

    @Test
    fun `build creates guitar with all properties`() {
        val guitar = Guitar.Builder()
            .type("Electric")
            .model("Stratocaster")
            .specs("Alder body")
            .os("v1.0")
            .build()
        assertEquals("Electric", guitar.type)
        assertEquals("Stratocaster", guitar.model)
        assertEquals("Alder body", guitar.specs)
        assertEquals("v1.0", guitar.os)
    }

    @Test
    fun `build with no properties uses empty strings`() {
        val guitar = Guitar.Builder().build()
        assertEquals("", guitar.type)
        assertEquals("", guitar.model)
        assertEquals("", guitar.specs)
        assertEquals("", guitar.os)
    }

    @Test
    fun `builder methods return same builder for chaining`() {
        val builder = Guitar.Builder()
        assertSame(builder, builder.type("Electric"))
        assertSame(builder, builder.model("Les Paul"))
        assertSame(builder, builder.specs("Specs"))
        assertSame(builder, builder.os("v1.0"))
    }

    @Test
    fun `toString contains all properties`() {
        val guitar = Guitar.Builder()
            .type("Electric")
            .model("Stratocaster")
            .specs("Alder body")
            .os("v1.0")
            .build()
        assertTrue(guitar.toString().contains("Electric"))
        assertTrue(guitar.toString().contains("Stratocaster"))
        assertTrue(guitar.toString().contains("Alder body"))
        assertTrue(guitar.toString().contains("v1.0"))
    }

    @Test
    fun `addGuitar adds to inventory`() {
        val inventory = GuitarInventory()
        val guitar = Guitar.Builder().model("TestAdd").build()
        inventory.addGuitar(guitar, 3)
        assertEquals(3, inventory.getQuantity(guitar))
    }

    @Test
    fun `addGuitar accumulates quantity`() {
        val inventory = GuitarInventory()
        val guitar = Guitar.Builder().model("TestAccumulate").build()
        inventory.addGuitar(guitar, 2)
        inventory.addGuitar(guitar, 3)
        assertEquals(5, inventory.getQuantity(guitar))
    }

    @Test
    fun `removeGuitar removes from inventory`() {
        val inventory = GuitarInventory()
        val guitar = Guitar.Builder().model("TestRemove").build()
        inventory.addGuitar(guitar, 1)
        inventory.removeGuitar(guitar, 1)
        assertFalse(inventory.contains(guitar))
    }

    @Test
    fun `removeGuitar decreases quantity partially`() {
        val inventory = GuitarInventory()
        val guitar = Guitar.Builder().model("TestPartial").build()
        inventory.addGuitar(guitar, 5)
        inventory.removeGuitar(guitar, 2)
        assertEquals(3, inventory.getQuantity(guitar))
    }

    @Test
    fun `listInventory returns all items`() {
        val inventory = GuitarInventory()
        val guitar1 = Guitar.Builder().model("Model1").build()
        val guitar2 = Guitar.Builder().model("Model2").build()
        inventory.addGuitar(guitar1, 1)
        inventory.addGuitar(guitar2, 2)
        val list = inventory.listInventory()
        assertEquals(2, list.size)
    }

    @Test
    fun `isEmpty returns true for empty inventory`() {
        val inventory = GuitarInventory()
        assertTrue(inventory.isEmpty())
    }

    @Test
    fun `isEmpty returns false for non-empty inventory`() {
        val inventory = GuitarInventory()
        val guitar = Guitar.Builder().model("Test").build()
        inventory.addGuitar(guitar)
        assertFalse(inventory.isEmpty())
    }
}
