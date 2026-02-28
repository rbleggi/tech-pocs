package com.rbleggi.codegenerationassistant;

import java.util.*;

record CodeRequest(String name, String type, List<String> parameters, Map<String, String> options) {}

record GeneratedCode(String code, String language, String type) {}

sealed interface CodeGenerationStrategy permits ClassGeneratorStrategy, FunctionGeneratorStrategy, TestGeneratorStrategy {
    GeneratedCode generate(CodeRequest request);
}

final class ClassGeneratorStrategy implements CodeGenerationStrategy {
    @Override
    public GeneratedCode generate(CodeRequest request) {
        StringBuilder code = new StringBuilder();
        String className = request.name();

        code.append("public class ").append(className).append(" {\n");

        for (String param : request.parameters()) {
            String[] parts = param.split(":");
            if (parts.length == 2) {
                code.append("    private ").append(parts[1].trim()).append(" ").append(parts[0].trim()).append(";\n");
            }
        }

        code.append("\n    public ").append(className).append("(");
        for (int i = 0; i < request.parameters().size(); i++) {
            String[] parts = request.parameters().get(i).split(":");
            if (parts.length == 2) {
                code.append(parts[1].trim()).append(" ").append(parts[0].trim());
                if (i < request.parameters().size() - 1) {
                    code.append(", ");
                }
            }
        }
        code.append(") {\n");

        for (String param : request.parameters()) {
            String[] parts = param.split(":");
            if (parts.length == 2) {
                String fieldName = parts[0].trim();
                code.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            }
        }

        code.append("    }\n");

        for (String param : request.parameters()) {
            String[] parts = param.split(":");
            if (parts.length == 2) {
                String fieldName = parts[0].trim();
                String fieldType = parts[1].trim();
                String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                code.append("\n    public ").append(fieldType).append(" get").append(capitalizedName).append("() {\n");
                code.append("        return ").append(fieldName).append(";\n");
                code.append("    }\n");
            }
        }

        code.append("}\n");

        return new GeneratedCode(code.toString(), "Java", "class");
    }
}

final class FunctionGeneratorStrategy implements CodeGenerationStrategy {
    @Override
    public GeneratedCode generate(CodeRequest request) {
        StringBuilder code = new StringBuilder();
        String returnType = request.options().getOrDefault("returnType", "void");
        String functionName = request.name();

        code.append("public ").append(returnType).append(" ").append(functionName).append("(");

        for (int i = 0; i < request.parameters().size(); i++) {
            String[] parts = request.parameters().get(i).split(":");
            if (parts.length == 2) {
                code.append(parts[1].trim()).append(" ").append(parts[0].trim());
                if (i < request.parameters().size() - 1) {
                    code.append(", ");
                }
            }
        }

        code.append(") {\n");

        if (!"void".equals(returnType)) {
            code.append("    // TODO: implement logic\n");
            code.append("    return ");
            switch (returnType) {
                case "int", "long", "double", "float" -> code.append("0");
                case "boolean" -> code.append("false");
                case "String" -> code.append("\"\"");
                default -> code.append("null");
            }
            code.append(";\n");
        } else {
            code.append("    // TODO: implement logic\n");
        }

        code.append("}\n");

        return new GeneratedCode(code.toString(), "Java", "function");
    }
}

final class TestGeneratorStrategy implements CodeGenerationStrategy {
    @Override
    public GeneratedCode generate(CodeRequest request) {
        StringBuilder code = new StringBuilder();
        String testClass = request.name();
        String targetClass = request.options().getOrDefault("targetClass", "Target");

        code.append("import org.junit.jupiter.api.Test;\n");
        code.append("import org.junit.jupiter.api.BeforeEach;\n");
        code.append("import static org.junit.jupiter.api.Assertions.*;\n\n");

        code.append("class ").append(testClass).append(" {\n");
        code.append("    private ").append(targetClass).append(" target;\n\n");

        code.append("    @BeforeEach\n");
        code.append("    void setUp() {\n");
        code.append("        target = new ").append(targetClass).append("();\n");
        code.append("    }\n\n");

        for (String method : request.parameters()) {
            code.append("    @Test\n");
            code.append("    void ").append(method).append("_deveRetornarResultadoEsperado() {\n");
            code.append("        // TODO: implement test\n");
            code.append("        assertNotNull(target);\n");
            code.append("    }\n\n");
        }

        code.append("}\n");

        return new GeneratedCode(code.toString(), "Java", "test");
    }
}

class CodeGenerationAssistant {
    private final CodeGenerationStrategy strategy;

    public CodeGenerationAssistant(CodeGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public GeneratedCode generateCode(CodeRequest request) {
        return strategy.generate(request);
    }

    public List<GeneratedCode> generateBatch(List<CodeRequest> requests) {
        return requests.stream()
            .map(this::generateCode)
            .toList();
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Code Generation Assistant");
    }
}
