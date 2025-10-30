package com.rbleggi.httpserver

import java.net.ServerSocket
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

interface GetRouteHandler {
    fun handle(path: String): String?
}

class HelloHandler : GetRouteHandler {
    override fun handle(path: String): String? =
        if (path == "/hello") "Hello, World!" else null
}

class PingHandler : GetRouteHandler {
    override fun handle(path: String): String? =
        if (path == "/ping") "pong" else null
}

class HttpRouter {
    private val handlers = mutableListOf<GetRouteHandler>()

    fun registerHandler(handler: GetRouteHandler) {
        handlers.add(handler)
    }

    fun route(path: String): String =
        handlers.firstNotNullOfOrNull { it.handle(path) } ?: "404"
}

fun main() {
    val port = 8080
    val server = ServerSocket(port)
    println("HTTP server running on http://localhost:$port ...")

    val handlers = listOf(HelloHandler(), PingHandler())

    while (true) {
        val client = server.accept()
        Thread { handleClient(client, handlers) }.start()
    }
}

fun handleClient(client: Socket, handlers: List<GetRouteHandler>) {
    client.use {
        val input = BufferedReader(InputStreamReader(it.getInputStream()))
        val output = PrintWriter(it.getOutputStream())

        val requestLine = input.readLine()
        if (requestLine != null && requestLine.startsWith("GET ")) {
            val path = requestLine.split(" ")[1]
            val response = handlers.firstNotNullOfOrNull { handler -> handler.handle(path) } ?: "Not Found"
            val status = if (response == "Not Found") "404 Not Found" else "200 OK"
            output.println("HTTP/1.1 $status\r\nContent-Type: text/plain\r\n\r\n$response")
        } else {
            output.println("HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nBad Request")
        }
        output.flush()
    }
}
