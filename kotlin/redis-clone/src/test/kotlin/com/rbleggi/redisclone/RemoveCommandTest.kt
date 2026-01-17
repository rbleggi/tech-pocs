package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RemoveCommandTest {
    @Test
    fun `remove existing key returns OK`() {
        val store = RedisStore()
        store.strings["key"] = "value"
        assertEquals("OK", RemoveCommand("key").execute(store))
    }

    @Test
    fun `remove deletes key from store`() {
        val store = RedisStore()
        store.strings["key"] = "value"
        RemoveCommand("key").execute(store)
        assertNull(store.strings["key"])
    }

    @Test
    fun `remove non-existing key returns nil`() {
        val store = RedisStore()
        assertEquals("(nil)", RemoveCommand("missing").execute(store))
    }
}
