package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertEquals

class GuitarBuilderSpecsTest {
    @Test
    fun `build sets specs correctly`() {
        val guitar = Guitar.Builder()
            .specs("Mahogany body")
            .build()
        assertEquals("Mahogany body", guitar.specs)
    }

    @Test
    fun `build sets os correctly`() {
        val guitar = Guitar.Builder()
            .os("CustomOS v2.0")
            .build()
        assertEquals("CustomOS v2.0", guitar.os)
    }
}
