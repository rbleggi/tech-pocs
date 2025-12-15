package com.rbleggi.templaterenderer.renderers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.rbleggi.templaterenderer.core.TemplateRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class PDFRenderer extends TemplateRenderer {
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
