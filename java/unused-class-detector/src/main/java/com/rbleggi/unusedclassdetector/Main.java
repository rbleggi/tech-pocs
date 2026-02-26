package com.rbleggi.unusedclassdetector;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Unused Class Detector");
    }
}

record AnalysisResult(List<String> unusedClasses) {}

interface ASTVisitor {
    AnalysisResult visit(String source);
}

class UnusedClassVisitor implements ASTVisitor {
    private final Pattern classDeclPattern = Pattern.compile("class\\s+(\\w+)");
    private final Pattern classUsagePattern = Pattern.compile("new\\s+(\\w+)");
    private final Pattern interfaceDeclPattern = Pattern.compile("interface\\s+(\\w+)");
    private final Pattern implementsPattern = Pattern.compile("implements\\s+(\\w+)");

    @Override
    public AnalysisResult visit(String source) {
        var decls = new java.util.HashSet<>(classDeclPattern.matcher(source)
            .results()
            .map(m -> m.group(1))
            .collect(Collectors.toSet()));

        var usages = new java.util.HashSet<>(classUsagePattern.matcher(source)
            .results()
            .map(m -> m.group(1))
            .collect(Collectors.toSet()));

        var interfaceDecls = interfaceDeclPattern.matcher(source)
            .results()
            .map(m -> m.group(1))
            .collect(Collectors.toSet());

        var interfaceUsages = implementsPattern.matcher(source)
            .results()
            .map(m -> m.group(1))
            .collect(Collectors.toSet());

        decls.addAll(interfaceDecls);
        usages.addAll(interfaceUsages);

        var excluded = Set.of("AnalysisResult", "UnusedClassVisitor",
                              "UnusedExampleClass", "Main");

        var unused = decls.stream()
            .filter(name -> !usages.contains(name))
            .filter(name -> !excluded.contains(name))
            .toList();

        return new AnalysisResult(unused);
    }
}

class UnusedExampleClass {
    String hello() {
        return "Hello, I am not used!";
    }
}
