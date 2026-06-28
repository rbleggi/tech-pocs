package com.rbleggi.templaterenderer.service

import com.rbleggi.templaterenderer.service.renderer.CsvRenderer
import com.rbleggi.templaterenderer.service.renderer.HtmlRenderer
import com.rbleggi.templaterenderer.service.renderer.PdfRenderer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RendererFactoryTest {

    @Test
    fun `returns an html renderer for type html`() {
        assertTrue(RendererFactory.getRenderer("html") is HtmlRenderer)
    }

    @Test
    fun `returns a csv renderer for type csv`() {
        assertTrue(RendererFactory.getRenderer("csv") is CsvRenderer)
    }

    @Test
    fun `returns a pdf renderer for type pdf`() {
        assertTrue(RendererFactory.getRenderer("pdf") is PdfRenderer)
    }

    @Test
    fun `throws for an unknown format`() {
        val exception = assertThrows<IllegalArgumentException> { RendererFactory.getRenderer("xml") }
        assertEquals("Unknown format: xml", exception.message)
    }
}
