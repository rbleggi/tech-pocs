package com.rbleggi.templaterenderer.service.renderer

class HtmlRenderer : TemplateRenderer() {
    override fun renderTitle(title: String): String = "<h1>$title</h1>"

    override fun renderContent(content: String): String = "<p>$content</p>"

    override fun assemble(title: String, content: String): ByteArray =
        "<html><body>\n$title\n$content\n</body></html>".toByteArray()
}
