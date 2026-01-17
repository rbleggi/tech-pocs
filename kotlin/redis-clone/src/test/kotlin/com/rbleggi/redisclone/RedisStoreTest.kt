package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertTrue

class RedisStoreTest {
    @Test
    fun `store initializes with empty strings`() {
        val store = RedisStore()
        assertTrue(store.strings.isEmpty())
    }

    @Test
    fun `store initializes with empty maps`() {
        val store = RedisStore()
        assertTrue(store.maps.isEmpty())
    }

    @Test
    fun `strings and maps are isolated`() {
        val store = RedisStore()
        store.strings["key"] = "value"
        store.maps["key"] = mutableMapOf("a" to "b")
        assertTrue(store.strings.containsKey("key"))
        assertTrue(store.maps.containsKey("key"))
    }
}
