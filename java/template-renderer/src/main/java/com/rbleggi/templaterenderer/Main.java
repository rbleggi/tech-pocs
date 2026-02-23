package com.rbleggi.templaterenderer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Template Renderer");
    }
}

abstract class TemplateRenderer {
    public abstract byte[] render(Map<String, String> data);
}

class HTMLRenderer extends TemplateRenderer {
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

class CSVRenderer extends TemplateRenderer {
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

class PDFRenderer extends TemplateRenderer {
    @Override
    public byte[] render(Map<String, String> data) {
        var outputStream = new ByteArrayOutputStream();
        var document = new Document(new PdfDocument(new PdfWriter(outputStream)));

        System.out.println("Generating PDF Template...");

        document.add(new Paragraph("Title: " + data.get("title")));
        document.add(new Paragraph("Content: " + data.get("content")));

        document.close();

        System.out.println("PDF Template Generated Successfully!");

        return outputStream.toByteArray();
    }
}

class RendererFactory {
    static TemplateRenderer getRenderer(String type) {
        return switch (type.toLowerCase()) {
            case "html" -> new HTMLRenderer();
            case "csv" -> new CSVRenderer();
            case "pdf" -> new PDFRenderer();
            default -> throw new IllegalArgumentException("Unknown format: " + type);
        };
    }
}

class FileUtil {
    static void saveToFile(String filename, byte[] content) {
        var file = new File(filename);
        try (var outputStream = new FileOutputStream(file)) {
            outputStream.write(content);
            System.out.println("File saved to: " + filename + "\n");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}
