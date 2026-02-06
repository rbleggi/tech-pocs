package com.rbleggi.httpserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    @Test
    void helloHandler_validPath_returnsGreeting() {
        var handler = new HelloHandler();
        assertEquals("Hello, World!", handler.handle("/hello"));
    }

    @Test
    void helloHandler_invalidPath_returnsNull() {
        var handler = new HelloHandler();
        assertNull(handler.handle("/other"));
    }

    @Test
    void pingHandler_validPath_returnsPong() {
        var handler = new PingHandler();
        assertEquals("pong", handler.handle("/ping"));
    }

    @Test
    void pingHandler_invalidPath_returnsNull() {
        var handler = new PingHandler();
        assertNull(handler.handle("/other"));
    }
}
