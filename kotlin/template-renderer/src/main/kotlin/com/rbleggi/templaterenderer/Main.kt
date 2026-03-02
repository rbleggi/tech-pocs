package com.rbleggi.templaterenderer

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

abstract class TemplateRenderer {
    abstract fun render(data: Map<String, String>): ByteArray
}

class HTMLRenderer : TemplateRenderer() {
    override fun render(data: Map<String, String>): ByteArray {
        println("Generating HTML Template...")

        val htmlContent = """
            <html>
            <head>
                <title>${data["title"]}</title>
            </head>
            <body>
                <h1>${data["title"]}</h1>
                <p>${data["content"]}</p>
            </body>
            </html>
        """.trimIndent()

        println("HTML Template Generated Successfully!")

        return htmlContent.toByteArray()
    }
}

class CSVRenderer : TemplateRenderer() {
    override fun render(data: Map<String, String>): ByteArray {
        println("Generating CSV Template...")

        val csvContent = data.entries.joinToString("\n") { "${it.key},${it.value}" }

        println("CSV Template Generated Successfully!")
        return csvContent.toByteArray()
    }
}

class PDFRenderer : TemplateRenderer() {
    override fun render(data: Map<String, String>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val document = Document(PdfDocument(PdfWriter(outputStream)))

        println("Generating PDF Template...")

        document.add(Paragraph("Title: ${data["title"]}"))
        document.add(Paragraph("Content: ${data["content"]}"))

        document.close()

        println("PDF Template Generated Successfully!")

        return outputStream.toByteArray()
    }
}

object RendererFactory {
    fun getRenderer(type: String): TemplateRenderer {
        return when (type.lowercase()) {
            "html" -> HTMLRenderer()
            "csv" -> CSVRenderer()
            "pdf" -> PDFRenderer()
            else -> throw IllegalArgumentException("Unknown format: $type")
        }
    }
}

object FileUtil {
    fun saveToFile(filename: String, content: ByteArray) {
        val file = File(filename)
        FileOutputStream(file).use { outputStream -> outputStream.write(content) }
        println("File saved to: $filename\n")
    }
}

fun main() {
    println("Template Renderer")
}
