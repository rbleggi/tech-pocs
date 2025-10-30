package com.rbleggi.httpserver

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

class HttpServerTest {

    @Test
    fun `HelloHandler returns Hello, World! for hello`() {
        val handler = HelloHandler()
        assertEquals("Hello, World!", handler.handle("/hello"))
        assertNull(handler.handle("/other"))
    }

    @Test
    fun `PingHandler returns pong for ping`() {
        val handler = PingHandler()
        assertEquals("pong", handler.handle("/ping"))
        assertNull(handler.handle("/other"))
    }

    @Test
    fun `HttpRouter should route to correct handler`() {
        val router = HttpRouter()
        router.registerHandler(HelloHandler())
        router.registerHandler(PingHandler())

        val response1 = router.route("/hello")
        assertEquals("Hello, World!", response1)

        val response2 = router.route("/ping")
        assertEquals("pong", response2)
    }

    @Test
    fun `HttpRouter should return 404 for unregistered paths`() {
        val router = HttpRouter()
        val response = router.route("/unknown")
        assertEquals("404", response)
    }

    @Test
    fun `HttpRouter should handle multiple handlers`() {
        val router = HttpRouter()
        router.registerHandler(HelloHandler())
        router.registerHandler(PingHandler())

        assertNotNull(router.route("/hello"))
        assertNotNull(router.route("/ping"))
    }

    @Test
    fun `HelloHandler should only handle hello path`() {
        val handler = HelloHandler()
        assertNotNull(handler.handle("/hello"))
        assertNull(handler.handle("/hello/world"))
        assertNull(handler.handle("/"))
    }

    @Test
    fun `PingHandler should only handle ping path`() {
        val handler = PingHandler()
        assertNotNull(handler.handle("/ping"))
        assertNull(handler.handle("/ping/pong"))
        assertNull(handler.handle("/"))
    }
}
