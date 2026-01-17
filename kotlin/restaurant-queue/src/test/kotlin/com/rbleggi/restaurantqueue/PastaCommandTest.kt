package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertEquals

class PastaCommandTest {
    @Test
    fun `default name is Pasta`() {
        assertEquals("Pasta", PastaCommand().name)
    }

    @Test
    fun `prepTime returns 15`() {
        assertEquals(15, PastaCommand().prepTime())
    }

    @Test
    fun `custom name is used`() {
        assertEquals("Spaghetti", PastaCommand("Spaghetti").name)
    }
}
