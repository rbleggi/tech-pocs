package com.rbleggi.calendar

import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun `user has correct id`() {
        val user = User("1", "Alice")
        assertEquals("1", user.id)
    }

    @Test
    fun `user has correct name`() {
        val user = User("1", "Alice")
        assertEquals("Alice", user.name)
    }
}
