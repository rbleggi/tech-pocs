package com.rbleggi.templaterenderer

import java.io.{File, FileOutputStream, ByteArrayOutputStream}

package core:
  abstract class TemplateRenderer:
    def render(data: Map[String, String]): Array[Byte]

  import com.rbleggi.templaterenderer.renderers.{CSVRenderer, HTMLRenderer, PDFRenderer}

  object RendererFactory:
    def getRenderer(format: String): TemplateRenderer = format.toLowerCase match
      case "html" => new HTMLRenderer()
      case "csv"  => new CSVRenderer()
      case "pdf"  => new PDFRenderer()
      case _      => throw new IllegalArgumentException(s"Unknown format: $format")

package renderers:
  import com.rbleggi.templaterenderer.core.TemplateRenderer
  import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
  import com.itextpdf.layout.Document
  import com.itextpdf.layout.element.Paragraph
  import java.io.ByteArrayOutputStream

  class HTMLRenderer extends TemplateRenderer:
    override def render(data: Map[String, String]): Array[Byte] =
      println("Generating HTML Template...")
      val htmlContent =
        s"""
           |<html>
           |<head><title>${data("title")}</title></head>
           |<body>
           |<h1>${data("title")}</h1>
           |<p>${data("content")}</p>
           |</body>
           |</html>
           |""".stripMargin
      println("HTML Template Generated Successfully!")
      htmlContent.getBytes()

  class CSVRenderer extends TemplateRenderer:
    def render(data: Map[String, String]): Array[Byte] =
      println("Generating CSV Template...")
      val csvContent = data.map { case (key, value) => s"$key,$value" }.mkString("\n")
      println("CSV Template Generated Successfully!")
      csvContent.getBytes("UTF-8")

  class PDFRenderer extends TemplateRenderer:
    def render(data: Map[String, String]): Array[Byte] =
      println("Generating PDF Template...")
      val outputStream = new ByteArrayOutputStream()
      val document = new Document(new PdfDocument(new PdfWriter(outputStream)))
      document.add(new Paragraph(s"Title: ${data("title")}"))
      document.add(new Paragraph(s"Content: ${data("content")}"))
      document.close()
      println("PDF Template Generated Successfully!")
      outputStream.toByteArray

package utils:
  object FileUtil:
    def saveToFile(filename: String, content: Array[Byte]): Unit =
      val fos = new FileOutputStream(new File(filename))
      fos.write(content)
      fos.close()
      println(s"File saved to: $filename\n")

@main def run(): Unit =
  println("Template Renderer")
