package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapKeysCommandTest {
    @Test
    fun `mapkeys returns keys from existing map`() {
        val store = RedisStore()
        store.maps["map"] = mutableMapOf("a" to "1", "b" to "2")
        val result = MapKeysCommand("map").execute(store)
        assertTrue(result.contains("a"))
        assertTrue(result.contains("b"))
    }

    @Test
    fun `mapkeys returns nil for missing map`() {
        val store = RedisStore()
        assertEquals("(nil)", MapKeysCommand("missing").execute(store))
    }
}
