package com.rbleggi.templaterenderer.service.renderer

import com.rbleggi.templaterenderer.model.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TemplateRendererTest {

    private val renderer = object : TemplateRenderer() {
        override fun renderTitle(title: String): String = "T($title)"
        override fun renderContent(content: String): String = "C($content)"
    }

    @Test
    fun `render applies the hooks and the default assembly`() {
        assertEquals("T(Hi)\nC(There)", String(renderer.render(Document("Hi", "There"))))
    }
}
