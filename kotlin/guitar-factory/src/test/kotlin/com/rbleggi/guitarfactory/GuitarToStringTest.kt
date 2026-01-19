package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertTrue

class GuitarToStringTest {
    @Test
    fun `toString contains type`() {
        val guitar = Guitar.Builder().type("Electric").build()
        assertTrue(guitar.toString().contains("Electric"))
    }

    @Test
    fun `toString contains model`() {
        val guitar = Guitar.Builder().model("Stratocaster").build()
        assertTrue(guitar.toString().contains("Stratocaster"))
    }
}
