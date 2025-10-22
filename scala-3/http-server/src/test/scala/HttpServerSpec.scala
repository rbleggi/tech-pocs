package com.rbleggi.httpserver

import org.scalatest.funsuite.AnyFunSuite

class HttpServerSpec extends AnyFunSuite {
  test("HelloHandler returns Hello, World! for /hello") {
    val handler = new HelloHandler
    assert(handler.handle("/hello").contains("Hello, World!"))
    assert(handler.handle("/other").isEmpty)
  }

  test("PingHandler returns pong for /ping") {
    val handler = new PingHandler
    assert(handler.handle("/ping").contains("pong"))
    assert(handler.handle("/other").isEmpty)
  }
}
