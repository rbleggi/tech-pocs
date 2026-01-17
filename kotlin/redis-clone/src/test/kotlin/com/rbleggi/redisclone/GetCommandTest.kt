package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals

class GetCommandTest {
    @Test
    fun `get existing key returns value`() {
        val store = RedisStore()
        store.strings["key"] = "value"
        assertEquals("value", GetCommand("key").execute(store))
    }

    @Test
    fun `get non-existing key returns nil`() {
        val store = RedisStore()
        assertEquals("(nil)", GetCommand("missing").execute(store))
    }
}
