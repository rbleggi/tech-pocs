package com.rbleggi.templaterenderer.core;

import com.rbleggi.templaterenderer.renderers.CSVRenderer;
import com.rbleggi.templaterenderer.renderers.HTMLRenderer;
import com.rbleggi.templaterenderer.renderers.PDFRenderer;

public class RendererFactory {
    public static TemplateRenderer getRenderer(String type) {
        return switch (type.toLowerCase()) {
            case "html" -> new HTMLRenderer();
            case "csv" -> new CSVRenderer();
            case "pdf" -> new PDFRenderer();
            default -> throw new IllegalArgumentException("Unknown format: " + type);
        };
    }
}
