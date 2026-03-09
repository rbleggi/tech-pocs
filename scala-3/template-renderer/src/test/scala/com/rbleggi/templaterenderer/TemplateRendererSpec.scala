package com.rbleggi.templaterenderer

import com.rbleggi.templaterenderer.core.RendererFactory
import com.rbleggi.templaterenderer.renderers.{CSVRenderer, HTMLRenderer, PDFRenderer}
import org.scalatest.funsuite.AnyFunSuite

class TemplateRendererSpec extends AnyFunSuite {
  val testData = Map("title" -> "Test Title", "content" -> "Test Content")

  test("HTMLRenderer should generate HTML content") {
    val renderer = new HTMLRenderer
    val result = renderer.render(testData)
    val html = new String(result)

    assert(html.contains("<html>"))
    assert(html.contains("<title>Test Title</title>"))
    assert(html.contains("<h1>Test Title</h1>"))
    assert(html.contains("<p>Test Content</p>"))
    assert(html.contains("</html>"))
  }

  test("HTMLRenderer should handle empty values") {
    val renderer = new HTMLRenderer
    val emptyData = Map("title" -> "", "content" -> "")
    val result = renderer.render(emptyData)
    val html = new String(result)

    assert(html.contains("<html>"))
    assert(html.contains("</html>"))
  }

  test("CSVRenderer should generate CSV content") {
    val renderer = new CSVRenderer
    val result = renderer.render(testData)
    val csv = new String(result, "UTF-8")

    assert(csv.contains("title,Test Title"))
    assert(csv.contains("content,Test Content"))
  }

  test("CSVRenderer should handle single entry") {
    val renderer = new CSVRenderer
    val singleData = Map("name" -> "John")
    val result = renderer.render(singleData)
    val csv = new String(result, "UTF-8")

    assert(csv == "name,John")
  }

  test("CSVRenderer should handle multiple entries") {
    val renderer = new CSVRenderer
    val multiData = Map("col1" -> "val1", "col2" -> "val2", "col3" -> "val3")
    val result = renderer.render(multiData)
    val csv = new String(result, "UTF-8")

    assert(csv.contains("col1,val1"))
    assert(csv.contains("col2,val2"))
    assert(csv.contains("col3,val3"))
  }

  test("PDFRenderer should generate PDF content") {
    val renderer = new PDFRenderer
    val result = renderer.render(testData)

    assert(result.nonEmpty)
    assert(result(0) == '%'.toByte)
    assert(result(1) == 'P'.toByte)
    assert(result(2) == 'D'.toByte)
    assert(result(3) == 'F'.toByte)
  }

  test("PDFRenderer should handle empty content") {
    val renderer = new PDFRenderer
    val emptyData = Map("title" -> "", "content" -> "")
    val result = renderer.render(emptyData)

    assert(result.nonEmpty)
    assert(result(0) == '%'.toByte)
  }

  test("RendererFactory should return HTMLRenderer for html format") {
    val renderer = RendererFactory.getRenderer("html")
    assert(renderer.isInstanceOf[HTMLRenderer])
  }

  test("RendererFactory should return CSVRenderer for csv format") {
    val renderer = RendererFactory.getRenderer("csv")
    assert(renderer.isInstanceOf[CSVRenderer])
  }

  test("RendererFactory should return PDFRenderer for pdf format") {
    val renderer = RendererFactory.getRenderer("pdf")
    assert(renderer.isInstanceOf[PDFRenderer])
  }

  test("RendererFactory should throw exception for unknown format") {
    assertThrows[IllegalArgumentException] {
      RendererFactory.getRenderer("unknown")
    }
  }

  test("RendererFactory should handle case-insensitive formats") {
    val renderer = RendererFactory.getRenderer("HTML")
    assert(renderer.isInstanceOf[HTMLRenderer])
  }
}
