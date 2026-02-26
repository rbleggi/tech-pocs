package com.rbleggi.unusedclassdetector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Unused Class Detector");
    }
}

interface ASTNode {}

record ClassDecl(String name) implements ASTNode {}

record ClassUsage(String name) implements ASTNode {}

record SourceFile(String content) implements ASTNode {}

record InterfaceDecl(String name) implements ASTNode {}

record InterfaceUsage(String name) implements ASTNode {}

interface ASTVisitor {
    AnalysisResult visit(SourceFile source);
}

record AnalysisResult(List<String> unusedClasses) {}

class UnusedClassVisitor implements ASTVisitor {
    private final Pattern classDeclPattern = Pattern.compile("class\\s+(\\w+)");
    private final Pattern classUsagePattern = Pattern.compile("new\\s+(\\w+)");
    private final Pattern interfaceDeclPattern = Pattern.compile("interface\\s+(\\w+)");
    private final Pattern implementsPattern = Pattern.compile("implements\\s+(\\w+)");

    @Override
    public AnalysisResult visit(SourceFile source) {
        var decls = classDeclPattern.matcher(source.content())
            .results()
            .map(m -> m.group(1))
            .collect(Collectors.toSet());

        var usages = classUsagePattern.matcher(source.content())
            .results()
            .map(m -> m.group(1))
            .collect(Collectors.toSet());

        var excluded = Set.of("ClassDecl", "ClassUsage", "SourceFile", "AnalysisResult");

        var unused = decls.stream()
            .filter(name -> !usages.contains(name))
            .filter(name -> !excluded.contains(name))
            .filter(name -> !name.endsWith("Decl") && !name.endsWith("Usage") &&
                           !name.endsWith("Result") && !name.endsWith("File"))
            .toList();

        return new AnalysisResult(unused);
    }
}

class UnusedExampleClass {
    String hello() {
        return "Hello, I am not used!";
    }
}
