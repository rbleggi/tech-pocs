package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertEquals

class GuitarBuilderTest {
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
    }
}
