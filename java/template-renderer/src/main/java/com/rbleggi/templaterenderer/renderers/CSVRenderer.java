package com.rbleggi.templaterenderer.renderers;

import com.rbleggi.templaterenderer.core.TemplateRenderer;

import java.util.Map;
import java.util.stream.Collectors;

public class CSVRenderer extends TemplateRenderer {
    @Override
    public byte[] render(Map<String, String> data) {
        System.out.println("Generating CSV Template...");

        var csvContent = data.entrySet().stream()
            .map(e -> e.getKey() + "," + e.getValue())
            .collect(Collectors.joining("\n"));

        System.out.println("CSV Template Generated Successfully!");
        return csvContent.getBytes();
    }
}
