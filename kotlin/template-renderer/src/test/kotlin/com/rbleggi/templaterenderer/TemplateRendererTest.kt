package com.rbleggi.templaterenderer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class RendererFactoryTest {

    @Test
    fun `should return HTMLRenderer for type html`() {
        val renderer: TemplateRenderer = RendererFactory.getRenderer("html")
        assertTrue(renderer is HTMLRenderer)
    }

    @Test
    fun `should return CSVRenderer for type csv`() {
        val renderer: TemplateRenderer = RendererFactory.getRenderer("csv")
        assertTrue(renderer is CSVRenderer)
    }

    @Test
    fun `should return PDFRenderer for type pdf`() {
        val renderer: TemplateRenderer = RendererFactory.getRenderer("pdf")
        assertTrue(renderer is PDFRenderer)
    }

    @Test
    fun `should throw exception for unknown format`() {
        val exception = assertThrows<IllegalArgumentException> {
            RendererFactory.getRenderer("xml")
        }
        assertEquals("Unknown format: xml", exception.message)
    }
}

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

class CSVRendererTest {

    @Test
    fun `should generate valid CSV output`() {
        val output = String(CSVRenderer().render(mapOf("Name" to "Roger", "Age" to "30")))
        assertEquals("Name,Roger\nAge,30", output)
    }

    @Test
    fun `should handle single field`() {
        val output = String(CSVRenderer().render(mapOf("Field" to "Value")))
        assertEquals("Field,Value", output)
    }

    @Test
    fun `should handle multiple fields`() {
        val output = String(CSVRenderer().render(mapOf(
            "Name" to "Alice",
            "Age" to "25",
            "City" to "New York",
            "Country" to "USA"
        )))
        assertTrue(output.contains("Name,Alice"))
        assertTrue(output.contains("Age,25"))
        assertTrue(output.contains("City,New York"))
        assertTrue(output.contains("Country,USA"))
    }

    @Test
    fun `should handle empty map`() {
        val output = String(CSVRenderer().render(emptyMap()))
        assertEquals("", output)
    }

    @Test
    fun `should handle values with commas`() {
        val output = String(CSVRenderer().render(mapOf("Address" to "123 Main St, Apt 4")))
        assertEquals("Address,123 Main St, Apt 4", output)
    }

    @Test
    fun `should separate entries with newlines`() {
        val output = String(CSVRenderer().render(mapOf("First" to "1", "Second" to "2")))
        assertTrue(output.contains("\n"))
        val lines = output.split("\n")
        assertEquals(2, lines.size)
    }
}

class PDFRendererTest {

    @Test
    fun `should generate valid PDF content`() {
        val pdfBytes = PDFRenderer().render(mapOf("title" to "PDF Test", "content" to "Testing PDF generation"))
        assertTrue(pdfBytes.isNotEmpty(), "Generated PDF content should not be empty")
    }

    @Test
    fun `should generate PDF with proper header`() {
        val pdfBytes = PDFRenderer().render(mapOf("title" to "Test", "content" to "Content"))
        val pdfString = String(pdfBytes)
        assertTrue(pdfString.startsWith("%PDF-"), "PDF should start with %PDF- header")
    }

    @Test
    fun `should handle empty title and content`() {
        val pdfBytes = PDFRenderer().render(mapOf("title" to "", "content" to ""))
        assertTrue(pdfBytes.isNotEmpty())
        assertTrue(pdfBytes.size > 100, "PDF should have minimum size even with empty content")
    }

    @Test
    fun `should handle missing keys`() {
        val pdfBytes = PDFRenderer().render(emptyMap())
        assertTrue(pdfBytes.isNotEmpty())
    }

    @Test
    fun `should generate different PDFs for different content`() {
        val pdf1 = PDFRenderer().render(mapOf("title" to "Title1", "content" to "Content1"))
        val pdf2 = PDFRenderer().render(mapOf("title" to "Title2", "content" to "Content2"))

        assertTrue(pdf1.isNotEmpty())
        assertTrue(pdf2.isNotEmpty())
        assertTrue(pdf1.contentEquals(pdf2).not(), "Different content should generate different PDFs")
    }

    @Test
    fun `should handle long content`() {
        val longContent = "This is a very long content. ".repeat(100)
        val pdfBytes = PDFRenderer().render(mapOf("title" to "Long Content Test", "content" to longContent))
        assertTrue(pdfBytes.isNotEmpty())
        assertTrue(pdfBytes.size > 500, "PDF with long content should be larger")
    }

    @Test
    fun `should handle special characters`() {
        val pdfBytes = PDFRenderer().render(mapOf(
            "title" to "Special: @#$%^&*()",
            "content" to "Content with special chars: <>&\""
        ))
        assertTrue(pdfBytes.isNotEmpty())
    }
}

class FileUtilTest {

    @Test
    fun `should write text content to a file`() {
        val tempFile = File.createTempFile("test_output", ".txt")
        try {
            FileUtil.saveToFile(tempFile.absolutePath, "test content.".toByteArray())
            assertTrue(tempFile.exists(), "File should be created")
            assertTrue(tempFile.readText() == "test content.", "File content should match")
        } finally {
            tempFile.delete()
        }
    }
}
