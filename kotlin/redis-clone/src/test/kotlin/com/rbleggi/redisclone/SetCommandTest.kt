package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals

class SetCommandTest {
    @Test
    fun `set returns OK`() {
        val store = RedisStore()
        val result = SetCommand("key", "value").execute(store)
        assertEquals("OK", result)
    }

    @Test
    fun `set stores value`() {
        val store = RedisStore()
        SetCommand("key", "value").execute(store)
        assertEquals("value", store.strings["key"])
    }
}
