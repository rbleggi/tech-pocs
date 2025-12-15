package com.rbleggi.templaterenderer;

import com.rbleggi.templaterenderer.core.RendererFactory;
import com.rbleggi.templaterenderer.utils.FileUtil;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List.of("html", "csv", "pdf").forEach(type -> {
            var data = Map.of("title", "Example Title", "content", "Example content");
            FileUtil.saveToFile("src/main/resources/output." + type, RendererFactory.getRenderer(type).render(data));
        });
    }
}
