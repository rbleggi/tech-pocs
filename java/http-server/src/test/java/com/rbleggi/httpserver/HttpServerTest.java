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

    @Test
    void httpRouter_registeredHandler_routesCorrectly() {
        var router = new HttpRouter();
        router.registerHandler(new HelloHandler());
        router.registerHandler(new PingHandler());

        assertEquals("Hello, World!", router.route("/hello"));
        assertEquals("pong", router.route("/ping"));
    }

    @Test
    void httpRouter_unknownPath_returns404() {
        var router = new HttpRouter();
        router.registerHandler(new HelloHandler());

        assertEquals("404", router.route("/unknown"));
    }

    @Test
    void httpRouter_noHandlers_returns404() {
        var router = new HttpRouter();
        assertEquals("404", router.route("/any"));
    }

    @Test
    void httpRouter_firstMatchingHandlerWins() {
        var router = new HttpRouter();
        router.registerHandler(path -> "/test".equals(path) ? "first" : null);
        router.registerHandler(path -> "/test".equals(path) ? "second" : null);

        assertEquals("first", router.route("/test"));
    }
}
