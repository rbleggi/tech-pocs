package com.rbleggi.templaterenderer.service

import com.rbleggi.templaterenderer.service.renderer.CsvRenderer
import com.rbleggi.templaterenderer.service.renderer.HtmlRenderer
import com.rbleggi.templaterenderer.service.renderer.PdfRenderer
import com.rbleggi.templaterenderer.service.renderer.TemplateRenderer

object RendererFactory {
    fun getRenderer(type: String): TemplateRenderer = when (type.lowercase()) {
        "html" -> HtmlRenderer()
        "csv" -> CsvRenderer()
        "pdf" -> PdfRenderer()
        else -> throw IllegalArgumentException("Unknown format: $type")
    }
}
