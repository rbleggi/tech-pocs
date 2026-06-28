package com.rbleggi.templaterenderer.service.renderer

import com.rbleggi.templaterenderer.model.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PdfRendererTest {

    private val document = Document("Title", "Content")

    @Test
    fun `render produces a non-empty pdf`() {
        assertTrue(PdfRenderer().render(document).isNotEmpty())
    }

    @Test
    fun `render produces bytes with the pdf header`() {
        assertTrue(String(PdfRenderer().render(document)).startsWith("%PDF-"))
    }

    @Test
    fun `different documents produce different pdfs`() {
        val other = Document("Other", "Different")
        assertFalse(PdfRenderer().render(document).contentEquals(PdfRenderer().render(other)))
    }
}
