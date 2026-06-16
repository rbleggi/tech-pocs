package com.rbleggi.taxsystem.model

import kotlin.reflect.KMutableProperty1
import kotlin.test.*

class DataClassBehaviorTest {

    private class PlainClass(val x: Int, val y: Int)
    private data class DataClass(val x: Int, val y: Int)

    @Test
    fun `two data classes with the same values are equal`() {
        assertNotEquals(PlainClass(1, 2), PlainClass(1, 2))
        assertEquals(DataClass(1, 2), DataClass(1, 2))
    }

    @Test
    fun `two data classes with the same values have the same hashCode`() {
        assertNotEquals(PlainClass(1, 2).hashCode(), PlainClass(1, 2).hashCode())
        assertEquals(DataClass(1, 2).hashCode(), DataClass(1, 2).hashCode())
    }

    @Test
    fun `data class prints its values as text`() {
        assertEquals("DataClass(x=1, y=2)", DataClass(1, 2).toString())
        assertTrue(PlainClass(1, 2).toString().startsWith("com.rbleggi"))
    }

    @Test
    fun `data class can be split into its values`() {
        val (x, y) = DataClass(1, 2)

        assertEquals(1, x)
        assertEquals(2, y)
    }

    @Test
    fun `copy makes a new data class and keeps the old one`() {
        val original = DataClass(1, 2)
        val changed = original.copy(x = 9)

        assertEquals(9, changed.x)
        assertEquals(1, original.x)
        assertNotSame(original, changed)
    }
}
