package com.rbleggi.templaterenderer.service.renderer

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document as PdfLayoutDocument
import com.itextpdf.layout.element.Paragraph as PdfParagraph
import java.io.ByteArrayOutputStream

class PdfRenderer : TemplateRenderer() {
    override fun renderTitle(title: String): String = title

    override fun renderContent(content: String): String = content

    override fun assemble(title: String, content: String): ByteArray {
        val output = ByteArrayOutputStream()
        val pdf = PdfLayoutDocument(PdfDocument(PdfWriter(output)))
        pdf.add(PdfParagraph(title))
        pdf.add(PdfParagraph(content))
        pdf.close()
        return output.toByteArray()
    }
}
