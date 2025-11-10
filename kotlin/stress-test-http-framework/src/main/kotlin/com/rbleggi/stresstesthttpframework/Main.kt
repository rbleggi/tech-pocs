package com.rbleggi.stresstesthttpframework

import kotlinx.coroutines.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

abstract class StressTestTemplate {
    abstract fun prepare()
    abstract fun execute()
    abstract fun report()

    fun runTest() {
        prepare()
        execute()
        report()
    }
}

class HttpStressTest(
    private val url: String,
    private val requests: Int,
    private val concurrency: Int
) : StressTestTemplate() {
    private var results: List<Long> = emptyList()
    private val client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build()

    override fun prepare() {
        results = emptyList()
    }

    override fun execute() {
        results = runBlocking {
            (1..requests).map {
                async(Dispatchers.IO) {
                    val start = System.nanoTime()
                    val request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build()
                    try {
                        client.send(request, HttpResponse.BodyHandlers.discarding())
                    } catch (e: Exception) {
                        println("Request failed: ${e.message}")
                    }
                    val end = System.nanoTime()
                    end - start
                }
            }.awaitAll()
        }
    }

    override fun report() {
        val total = results.size
        val avg = if (total > 0) results.average().toLong() else 0L
        println("Total requests: $total")
        println("Average response time (ns): $avg")
        println("Min response time (ns): ${if (results.isNotEmpty()) results.minOrNull() else 0L}")
        println("Max response time (ns): ${if (results.isNotEmpty()) results.maxOrNull() else 0L}")
    }
}

fun main() {
    val url = "https://httpbin.org/get"
    val requests = 50
    val concurrency = 10
    val test = HttpStressTest(url, requests, concurrency)
    test.runTest()
}
