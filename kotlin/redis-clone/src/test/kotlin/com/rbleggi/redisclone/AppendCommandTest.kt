package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals

class AppendCommandTest {
    @Test
    fun `append to new key creates value`() {
        val store = RedisStore()
        val result = AppendCommand("key", "value").execute(store)
        assertEquals("value", result)
    }

    @Test
    fun `append to existing key concatenates`() {
        val store = RedisStore()
        store.strings["key"] = "hello"
        val result = AppendCommand("key", "world").execute(store)
        assertEquals("helloworld", result)
    }
}
