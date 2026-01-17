package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapValuesCommandTest {
    @Test
    fun `mapvalues returns values from existing map`() {
        val store = RedisStore()
        store.maps["map"] = mutableMapOf("a" to "1", "b" to "2")
        val result = MapValuesCommand("map").execute(store)
        assertTrue(result.contains("1"))
        assertTrue(result.contains("2"))
    }

    @Test
    fun `mapvalues returns nil for missing map`() {
        val store = RedisStore()
        assertEquals("(nil)", MapValuesCommand("missing").execute(store))
    }
}
