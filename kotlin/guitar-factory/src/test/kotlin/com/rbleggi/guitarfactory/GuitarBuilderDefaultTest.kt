package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertEquals

class GuitarBuilderDefaultTest {
    @Test
    fun `build with no properties uses empty strings`() {
        val guitar = Guitar.Builder().build()
        assertEquals("", guitar.type)
        assertEquals("", guitar.model)
        assertEquals("", guitar.specs)
        assertEquals("", guitar.os)
    }
}
