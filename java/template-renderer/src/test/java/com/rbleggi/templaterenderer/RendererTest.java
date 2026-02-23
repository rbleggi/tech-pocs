package com.rbleggi.templaterenderer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RendererTest {

    @Test
    @DisplayName("RendererFactory should return HTMLRenderer for html type")
    void rendererFactory_html_returnsHTMLRenderer() {
        var renderer = RendererFactory.getRenderer("html");
        assertInstanceOf(HTMLRenderer.class, renderer);
    }

    @Test
    @DisplayName("RendererFactory should return CSVRenderer for csv type")
    void rendererFactory_csv_returnsCSVRenderer() {
        var renderer = RendererFactory.getRenderer("csv");
        assertInstanceOf(CSVRenderer.class, renderer);
    }

    @Test
    @DisplayName("RendererFactory should return PDFRenderer for pdf type")
    void rendererFactory_pdf_returnsPDFRenderer() {
        var renderer = RendererFactory.getRenderer("pdf");
        assertInstanceOf(PDFRenderer.class, renderer);
    }

    @Test
    @DisplayName("RendererFactory should be case insensitive")
    void rendererFactory_caseInsensitive_returnsCorrectRenderer() {
        var renderer1 = RendererFactory.getRenderer("HTML");
        var renderer2 = RendererFactory.getRenderer("Html");
        assertInstanceOf(HTMLRenderer.class, renderer1);
        assertInstanceOf(HTMLRenderer.class, renderer2);
    }

    @Test
    @DisplayName("RendererFactory should throw exception for unknown type")
    void rendererFactory_unknownType_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            RendererFactory.getRenderer("xml"));
    }

    @Test
    @DisplayName("HTMLRenderer should render data to HTML")
    void htmlRenderer_render_returnsHTMLContent() {
        var renderer = new HTMLRenderer();
        var data = Map.of("title", "Test", "content", "Content");
        var result = new String(renderer.render(data));
        assertTrue(result.contains("<html>"));
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("Content"));
    }

    @Test
    @DisplayName("CSVRenderer should render data to CSV")
    void csvRenderer_render_returnsCSVContent() {
        var renderer = new CSVRenderer();
        var data = Map.of("title", "Test", "content", "Content");
        var result = new String(renderer.render(data));
        assertTrue(result.contains("Test") || result.contains("Content"));
        assertTrue(result.contains(","));
    }

    @Test
    @DisplayName("PDFRenderer should render data to PDF format")
    void pdfRenderer_render_returnsPDFContent() {
        var renderer = new PDFRenderer();
        var data = Map.of("title", "Test", "content", "Content");
        var result = renderer.render(data);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    @DisplayName("HTMLRenderer should handle empty data")
    void htmlRenderer_emptyData_returnsHTML() {
        var renderer = new HTMLRenderer();
        var result = new String(renderer.render(Map.of()));
        assertTrue(result.contains("<html>"));
    }

    @Test
    @DisplayName("CSVRenderer should handle empty data")
    void csvRenderer_emptyData_returnsCSV() {
        var renderer = new CSVRenderer();
        var result = renderer.render(Map.of());
        assertNotNull(result);
    }

    @Test
    @DisplayName("PDFRenderer should handle empty data")
    void pdfRenderer_emptyData_returnsPDF() {
        var renderer = new PDFRenderer();
        var result = renderer.render(Map.of());
        assertNotNull(result);
    }
}
