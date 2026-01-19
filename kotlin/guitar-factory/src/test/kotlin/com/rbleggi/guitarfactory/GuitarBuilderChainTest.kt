package com.rbleggi.guitarfactory

import kotlin.test.Test
import kotlin.test.assertSame

class GuitarBuilderChainTest {
    @Test
    fun `type returns same builder`() {
        val builder = Guitar.Builder()
        assertSame(builder, builder.type("Electric"))
    }

    @Test
    fun `model returns same builder`() {
        val builder = Guitar.Builder()
        assertSame(builder, builder.model("Les Paul"))
    }

    @Test
    fun `specs returns same builder`() {
        val builder = Guitar.Builder()
        assertSame(builder, builder.specs("Specs"))
    }
}
