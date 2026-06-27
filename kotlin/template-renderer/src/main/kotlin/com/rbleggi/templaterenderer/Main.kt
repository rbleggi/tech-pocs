package com.rbleggi.templaterenderer

import com.rbleggi.templaterenderer.model.Document
import com.rbleggi.templaterenderer.service.RendererFactory

fun main() {
    println("Template Renderer")

    val document = Document(
        title = "Quarterly Report",
        content = "Summary of the results for the period."
    )

    listOf("html", "csv", "pdf").forEach { format ->
        val output = RendererFactory.getRenderer(format).render(document)
        println("\n== $format ==")
        println(if (format == "pdf") "${output.size} bytes" else String(output))
    }
}
