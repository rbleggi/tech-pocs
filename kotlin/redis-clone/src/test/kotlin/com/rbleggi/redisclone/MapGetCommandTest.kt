package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals

class MapGetCommandTest {
    @Test
    fun `mapget returns value from existing map`() {
        val store = RedisStore()
        store.maps["map"] = mutableMapOf("key" to "value")
        assertEquals("value", MapGetCommand("map", "key").execute(store))
    }

    @Test
    fun `mapget returns nil for missing map`() {
        val store = RedisStore()
        assertEquals("(nil)", MapGetCommand("missing", "key").execute(store))
    }

    @Test
    fun `mapget returns nil for missing key`() {
        val store = RedisStore()
        store.maps["map"] = mutableMapOf()
        assertEquals("(nil)", MapGetCommand("map", "missing").execute(store))
    }
}
