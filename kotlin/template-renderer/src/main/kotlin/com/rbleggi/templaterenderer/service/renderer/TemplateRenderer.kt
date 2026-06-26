package com.rbleggi.templaterenderer.service.renderer

import com.rbleggi.templaterenderer.model.Document

abstract class TemplateRenderer {
    fun render(document: Document): ByteArray {
        val title = renderTitle(document.title)
        val content = renderContent(document.content)
        return assemble(title, content)
    }

    protected abstract fun renderTitle(title: String): String

    protected abstract fun renderContent(content: String): String

    protected open fun assemble(title: String, content: String): ByteArray =
        "$title\n$content".toByteArray()
}
