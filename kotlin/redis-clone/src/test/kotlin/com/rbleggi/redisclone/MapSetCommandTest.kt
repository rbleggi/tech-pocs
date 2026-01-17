package com.rbleggi.redisclone

import kotlin.test.Test
import kotlin.test.assertEquals

class MapSetCommandTest {
    @Test
    fun `mapset returns OK`() {
        val store = RedisStore()
        val result = MapSetCommand("map", "key", "value").execute(store)
        assertEquals("OK", result)
    }

    @Test
    fun `mapset creates map and stores value`() {
        val store = RedisStore()
        MapSetCommand("map", "key", "value").execute(store)
        assertEquals("value", store.maps["map"]?.get("key"))
    }
}
