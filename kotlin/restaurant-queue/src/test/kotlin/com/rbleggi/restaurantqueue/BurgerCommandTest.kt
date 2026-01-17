package com.rbleggi.restaurantqueue

import kotlin.test.Test
import kotlin.test.assertEquals

class BurgerCommandTest {
    @Test
    fun `default name is Burger`() {
        assertEquals("Burger", BurgerCommand().name)
    }

    @Test
    fun `prepTime returns 10`() {
        assertEquals(10, BurgerCommand().prepTime())
    }

    @Test
    fun `custom name is used`() {
        assertEquals("Cheeseburger", BurgerCommand("Cheeseburger").name)
    }
}
