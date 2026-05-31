package com.rbleggi.templaterenderer

import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.{ByteArrayOutputStream, File, FileOutputStream}

enum OutputFormat:
  case Html, Csv, Pdf

trait Renderer:
  def render(data: Map[String, String]): Array[Byte]

class HtmlRenderer extends Renderer:
  def render(data: Map[String, String]): Array[Byte] =
    val title   = data.getOrElse("title", "")
    val content = data.getOrElse("content", "")
    s"""|<html>
        |<head><title>$title</title></head>
        |<body>
        |<h1>$title</h1>
        |<p>$content</p>
        |</body>
        |</html>""".stripMargin.getBytes("UTF-8")

class CsvRenderer extends Renderer:
  def render(data: Map[String, String]): Array[Byte] =
    data.map { case (k, v) => s"$k,$v" }.mkString("\n").getBytes("UTF-8")

class PdfRenderer extends Renderer:
  def render(data: Map[String, String]): Array[Byte] =
    val out = ByteArrayOutputStream()
    val doc = Document(PdfDocument(PdfWriter(out)))
    data.foreach { case (k, v) => doc.add(Paragraph(s"$k: $v")) }
    doc.close()
    out.toByteArray

object RendererFactory:
  def forFormat(format: OutputFormat): Renderer = format match
    case OutputFormat.Html => HtmlRenderer()
    case OutputFormat.Csv  => CsvRenderer()
    case OutputFormat.Pdf  => PdfRenderer()

  def forName(name: String): Renderer = name.toLowerCase match
    case "html" => HtmlRenderer()
    case "csv"  => CsvRenderer()
    case "pdf"  => PdfRenderer()
    case other  => throw IllegalArgumentException(s"Unknown format: $other")

object FileStore:
  def save(path: String, bytes: Array[Byte]): Unit =
    val fos = FileOutputStream(File(path))
    fos.write(bytes)
    fos.close()

@main def run(): Unit =
  println("Template Renderer")
