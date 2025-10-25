package com.rbleggi.httpserver

class HttpServerSpec {
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

  test("HttpRouter should route to correct handler") {
    val router = new HttpRouter()
    router.registerHandler(new HelloHandler)
    router.registerHandler(new PingHandler)

    val response1 = router.route("/hello")
    assert(response1.contains("Hello, World!"))

    val response2 = router.route("/ping")
    assert(response2.contains("pong"))
  }

  test("HttpRouter should return 404 for unregistered paths") {
    val router = new HttpRouter()
    val response = router.route("/unknown")
    assert(response.contains("404"))
  }

  test("HttpRouter should handle multiple handlers") {
    val router = new HttpRouter()
    router.registerHandler(new HelloHandler)
    router.registerHandler(new PingHandler)

    assert(router.route("/hello").isDefined)
    assert(router.route("/ping").isDefined)
  }

  test("HelloHandler should only handle /hello path") {
    val handler = new HelloHandler
    assert(handler.handle("/hello").isDefined)
    assert(handler.handle("/hello/world").isEmpty)
    assert(handler.handle("/").isEmpty)
  }

  test("PingHandler should only handle /ping path") {
    val handler = new PingHandler
    assert(handler.handle("/ping").isDefined)
    assert(handler.handle("/ping/pong").isEmpty)
    assert(handler.handle("/").isEmpty)
  }
}
