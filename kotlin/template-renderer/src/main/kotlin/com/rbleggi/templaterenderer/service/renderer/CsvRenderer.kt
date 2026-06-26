package com.rbleggi.templaterenderer.service.renderer

class CsvRenderer : TemplateRenderer() {
    override fun renderTitle(title: String): String = "title,$title"

    override fun renderContent(content: String): String = "content,$content"
}
