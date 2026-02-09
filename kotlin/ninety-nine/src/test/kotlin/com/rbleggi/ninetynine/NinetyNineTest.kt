package com.rbleggi.ninetynine

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NinetyNineTest {
    @Test
    fun testLast() {
        assertEquals(8, P01.last(listOf(1, 1, 2, 3, 5, 8)))
    }

    @Test
    fun testPenultimate() {
        assertEquals(5, P02.penultimate(listOf(1, 1, 2, 3, 5, 8)))
    }

    @Test
    fun testNth() {
        assertEquals(5, P03.nth(4, listOf(1, 1, 2, 3, 5, 8)))
    }
}
