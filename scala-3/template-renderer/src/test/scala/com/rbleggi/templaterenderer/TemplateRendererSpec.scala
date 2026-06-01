package com.rbleggi.templaterenderer

import org.scalatest.funsuite.AnyFunSuite

class TemplateRendererSpec extends AnyFunSuite:

  val sampleData: Map[String, String] = Map("title" -> "Relatorio Anual", "content" -> "Dados de Sao Paulo")

  test("HtmlRenderer renders title in head and body"):
    val bytes = HtmlRenderer().render(sampleData)
    val html  = String(bytes, "UTF-8")
    assert(html.contains("<title>Relatorio Anual</title>"))
    assert(html.contains("<h1>Relatorio Anual</h1>"))
    assert(html.contains("<p>Dados de Sao Paulo</p>"))

  test("HtmlRenderer wraps output in html tags"):
    val html = String(HtmlRenderer().render(sampleData), "UTF-8")
    assert(html.startsWith("<html>"))
    assert(html.endsWith("</html>"))

  test("HtmlRenderer handles missing keys with empty strings"):
    val html = String(HtmlRenderer().render(Map.empty), "UTF-8")
    assert(html.contains("<title></title>"))
    assert(html.contains("<h1></h1>"))

  test("CsvRenderer produces key comma value lines"):
    val csv = String(CsvRenderer().render(Map("nome" -> "Joao", "cidade" -> "Recife")), "UTF-8")
    assert(csv.contains("nome,Joao"))
    assert(csv.contains("cidade,Recife"))

  test("CsvRenderer single entry produces no newline"):
    val csv = String(CsvRenderer().render(Map("cpf" -> "123")), "UTF-8")
    assert(csv == "cpf,123")

  test("CsvRenderer multiple entries are newline-separated"):
    val csv = String(CsvRenderer().render(Map("a" -> "1", "b" -> "2", "c" -> "3")), "UTF-8")
    val lines = csv.split("\n")
    assert(lines.length == 3)

  test("PdfRenderer returns non-empty bytes"):
    val bytes = PdfRenderer().render(sampleData)
    assert(bytes.nonEmpty)

  test("PdfRenderer output starts with PDF magic bytes"):
    val bytes = PdfRenderer().render(sampleData)
    assert(bytes(0) == '%'.toByte)
    assert(bytes(1) == 'P'.toByte)
    assert(bytes(2) == 'D'.toByte)
    assert(bytes(3) == 'F'.toByte)

  test("PdfRenderer handles empty data map"):
    val bytes = PdfRenderer().render(Map.empty)
    assert(bytes.nonEmpty)

  test("RendererFactory.forFormat returns HtmlRenderer for Html"):
    assert(RendererFactory.forFormat(OutputFormat.Html).isInstanceOf[HtmlRenderer])

  test("RendererFactory.forFormat returns CsvRenderer for Csv"):
    assert(RendererFactory.forFormat(OutputFormat.Csv).isInstanceOf[CsvRenderer])

  test("RendererFactory.forFormat returns PdfRenderer for Pdf"):
    assert(RendererFactory.forFormat(OutputFormat.Pdf).isInstanceOf[PdfRenderer])

  test("RendererFactory.forName resolves html case-insensitively"):
    assert(RendererFactory.forName("HTML").isInstanceOf[HtmlRenderer])
    assert(RendererFactory.forName("html").isInstanceOf[HtmlRenderer])

  test("RendererFactory.forName resolves csv"):
    assert(RendererFactory.forName("csv").isInstanceOf[CsvRenderer])

  test("RendererFactory.forName resolves pdf"):
    assert(RendererFactory.forName("pdf").isInstanceOf[PdfRenderer])

  test("RendererFactory.forName throws IllegalArgumentException for unknown format"):
    val ex = intercept[IllegalArgumentException]:
      RendererFactory.forName("xml")
    assert(ex.getMessage == "Unknown format: xml")

  test("FileStore saves bytes to a temp file"):
    val tmp = java.io.File.createTempFile("renderer", ".bin")
    val content = "conteudo de teste".getBytes("UTF-8")
    FileStore.save(tmp.getAbsolutePath, content)
    val read = java.nio.file.Files.readAllBytes(tmp.toPath)
    assert(read.sameElements(content))
    tmp.delete()
