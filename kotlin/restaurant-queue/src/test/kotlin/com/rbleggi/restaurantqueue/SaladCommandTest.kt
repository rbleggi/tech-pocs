package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertEquals

class SaladCommandTest {
    @Test
    fun `default name is Salad`() {
        assertEquals("Salad", SaladCommand().name)
    }

    @Test
    fun `prepTime returns 5`() {
        assertEquals(5, SaladCommand().prepTime())
    }

    @Test
    fun `custom name is used`() {
        assertEquals("Caesar Salad", SaladCommand("Caesar Salad").name)
    }
}
