package com.rbleggi.templaterenderer.renderers

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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
