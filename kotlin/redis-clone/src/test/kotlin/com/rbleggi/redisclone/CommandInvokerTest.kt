package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandInvokerTest {
    @Test
    fun `invoker executes command`() {
        val store = RedisStore()
        val invoker = CommandInvoker(store)
        assertEquals("OK", invoker.execute(SetCommand("key", "value")))
    }

    @Test
    fun `invoker uses shared store`() {
        val store = RedisStore()
        val invoker = CommandInvoker(store)
        invoker.execute(SetCommand("key", "value"))
        assertEquals("value", invoker.execute(GetCommand("key")))
    }
}
