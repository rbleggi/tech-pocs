package com.rbleggi.templaterenderer.renderers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HTMLRendererTest {

    @Test
    fun `should generate valid HTML output`() {
        val output = String(HTMLRenderer().render(mapOf("title" to "Test Title", "content" to "Test Content")))
        assertEquals(
            """
            <html>
            <head>
                <title>Test Title</title>
            </head>
            <body>
                <h1>Test Title</h1>
                <p>Test Content</p>
            </body>
            </html>
        """.trimIndent(), output
        )
    }

    @Test
    fun `should handle empty title and content`() {
        val output = String(HTMLRenderer().render(mapOf("title" to "", "content" to "")))
        assertTrue(output.contains("<title></title>"))
        assertTrue(output.contains("<h1></h1>"))
        assertTrue(output.contains("<p></p>"))
    }

    @Test
    fun `should handle missing title key`() {
        val output = String(HTMLRenderer().render(mapOf("content" to "Just Content")))
        assertTrue(output.contains("<title>null</title>"))
        assertTrue(output.contains("<p>Just Content</p>"))
    }

    @Test
    fun `should handle missing content key`() {
        val output = String(HTMLRenderer().render(mapOf("title" to "Just Title")))
        assertTrue(output.contains("<title>Just Title</title>"))
        assertTrue(output.contains("<p>null</p>"))
    }

    @Test
    fun `should generate valid HTML structure`() {
        val output = String(HTMLRenderer().render(mapOf("title" to "My Page", "content" to "Hello World")))
        assertTrue(output.contains("<html>"))
        assertTrue(output.contains("</html>"))
        assertTrue(output.contains("<head>"))
        assertTrue(output.contains("</head>"))
        assertTrue(output.contains("<body>"))
        assertTrue(output.contains("</body>"))
    }

    @Test
    fun `should handle special characters in content`() {
        val output = String(HTMLRenderer().render(mapOf("title" to "Test & Title", "content" to "Content with <tags>")))
        assertTrue(output.contains("Test & Title"))
        assertTrue(output.contains("Content with <tags>"))
    }
}
