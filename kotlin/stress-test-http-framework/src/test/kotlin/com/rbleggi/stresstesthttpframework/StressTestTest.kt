package com.rbleggi.stresstesthttpframework

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class StressTestTest {
    @Test
    fun testStressTestExecution() {
        val test = object : StressTestTemplate() {
            var prepared = false
            var executed = false
            var reported = false

            override fun prepare() { prepared = true }
            override fun execute() { executed = true }
            override fun report() { reported = true }
        }

        test.runTest()

        assert(test.prepared)
        assert(test.executed)
        assert(test.reported)
    }

    @Test
    fun testExecutionOrder() {
        val executionOrder = mutableListOf<String>()
        val test = object : StressTestTemplate() {
            override fun prepare() { executionOrder.add("prepare") }
            override fun execute() { executionOrder.add("execute") }
            override fun report() { executionOrder.add("report") }
        }

        test.runTest()

        assertEquals(listOf("prepare", "execute", "report"), executionOrder)
    }

    @Test
    fun testHttpStressTestWithSingleRequest() {
        val test = HttpStressTest("https://httpbin.org/get", 1, 1)

        assertDoesNotThrow {
            test.runTest()
        }
    }

    @Test
    fun testHttpStressTestWithMultipleRequests() {
        val test = HttpStressTest("https://httpbin.org/get", 5, 2)

        assertDoesNotThrow {
            test.runTest()
        }
    }

    @Test
    fun testHttpStressTestReportOutput() {
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val test = HttpStressTest("https://httpbin.org/get", 3, 1)
            test.runTest()

            val output = outputStream.toString()
            assertTrue(output.contains("Total requests:"))
            assertTrue(output.contains("Average response time (ns):"))
            assertTrue(output.contains("Min response time (ns):"))
            assertTrue(output.contains("Max response time (ns):"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun testStressTestTemplateCanBeExtended() {
        var customLogicExecuted = false

        val test = object : StressTestTemplate() {
            override fun prepare() {}
            override fun execute() { customLogicExecuted = true }
            override fun report() {}
        }

        test.runTest()
        assertTrue(customLogicExecuted)
    }

    @Test
    fun testHttpStressTestWithInvalidUrl() {
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val test = HttpStressTest("http://invalid-url-that-does-not-exist.com", 1, 1)

            assertDoesNotThrow {
                test.runTest()
            }

            val output = outputStream.toString()
            assertTrue(output.contains("Total requests:") || output.contains("Request failed:"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun testMultipleConsecutiveRuns() {
        val test = object : StressTestTemplate() {
            var runCount = 0
            override fun prepare() { runCount++ }
            override fun execute() {}
            override fun report() {}
        }

        test.runTest()
        test.runTest()
        test.runTest()

        assertEquals(3, test.runCount)
    }
}
