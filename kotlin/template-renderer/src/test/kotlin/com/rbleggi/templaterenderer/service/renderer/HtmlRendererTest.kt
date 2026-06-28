package com.rbleggi.templaterenderer.service.renderer

import com.rbleggi.templaterenderer.model.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HtmlRendererTest {

    @Test
    fun `renders the title as h1 and content as p wrapped in html`() {
        val output = String(HtmlRenderer().render(Document("My Page", "Hello World")))
        assertEquals("<html><body>\n<h1>My Page</h1>\n<p>Hello World</p>\n</body></html>", output)
    }
}
