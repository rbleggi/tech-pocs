package com.rbleggi.templaterenderer.core;

import java.util.Map;

public abstract class TemplateRenderer {
    public abstract byte[] render(Map<String, String> data);
}
