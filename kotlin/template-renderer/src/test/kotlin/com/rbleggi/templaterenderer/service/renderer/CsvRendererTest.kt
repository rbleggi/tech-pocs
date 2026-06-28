package com.rbleggi.templaterenderer.service.renderer

import com.rbleggi.templaterenderer.model.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CsvRendererTest {

    @Test
    fun `renders title and content as comma-separated lines`() {
        val output = String(CsvRenderer().render(Document("Report", "Body")))
        assertEquals("title,Report\ncontent,Body", output)
    }
}
