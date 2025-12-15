package com.rbleggi.templaterenderer.renderers;

import com.rbleggi.templaterenderer.core.TemplateRenderer;

import java.util.Map;

public class HTMLRenderer extends TemplateRenderer {
    @Override
    public byte[] render(Map<String, String> data) {
        System.out.println("Generating HTML Template...");

        var htmlContent = """
            <html>
            <head>
                <title>%s</title>
            </head>
            <body>
                <h1>%s</h1>
                <p>%s</p>
            </body>
            </html>
            """.formatted(data.get("title"), data.get("title"), data.get("content"));

        System.out.println("HTML Template Generated Successfully!");

        return htmlContent.getBytes();
    }
}
