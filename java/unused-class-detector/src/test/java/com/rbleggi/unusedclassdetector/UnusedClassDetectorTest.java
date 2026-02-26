package com.rbleggi.unusedclassdetector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnusedClassDetectorTest {

    @Test
    @DisplayName("UnusedClassVisitor should detect unused class")
    void unusedClassVisitor_unusedClass_detectsUnused() {
        var content = "class UsedClass {} class UnusedClass {} new UsedClass();";
        var source = new SourceFile(content);
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertTrue(result.unusedClasses().contains("UnusedClass"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should not report used classes")
    void unusedClassVisitor_usedClass_notReported() {
        var content = "class MyClass {} new MyClass();";
        var source = new SourceFile(content);
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertFalse(result.unusedClasses().contains("MyClass"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should handle multiple classes")
    void unusedClassVisitor_multipleClasses_detectsCorrectly() {
        var content = "class Used1 {} class Used2 {} class Unused {} new Used1(); new Used2();";
        var source = new SourceFile(content);
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertTrue(result.unusedClasses().contains("Unused"));
        assertFalse(result.unusedClasses().contains("Used1"));
        assertFalse(result.unusedClasses().contains("Used2"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should exclude framework classes")
    void unusedClassVisitor_frameworkClasses_excluded() {
        var content = "class ClassDecl {} class ClassUsage {} class SourceFile {}";
        var source = new SourceFile(content);
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertFalse(result.unusedClasses().contains("ClassDecl"));
        assertFalse(result.unusedClasses().contains("ClassUsage"));
        assertFalse(result.unusedClasses().contains("SourceFile"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should handle empty source")
    void unusedClassVisitor_emptySource_returnsEmpty() {
        var source = new SourceFile("");
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertTrue(result.unusedClasses().isEmpty());
    }

    @Test
    @DisplayName("SourceFile should store content")
    void sourceFile_storesContent() {
        var content = "class Test {}";
        var source = new SourceFile(content);
        assertEquals(content, source.content());
    }

    @Test
    @DisplayName("ClassDecl should store class name")
    void classDecl_storesName() {
        var decl = new ClassDecl("TestClass");
        assertEquals("TestClass", decl.name());
    }

    @Test
    @DisplayName("ClassUsage should store class name")
    void classUsage_storesName() {
        var usage = new ClassUsage("TestClass");
        assertEquals("TestClass", usage.name());
    }

    @Test
    @DisplayName("AnalysisResult should store unused classes")
    void analysisResult_storesUnusedClasses() {
        var unused = java.util.List.of("Class1", "Class2");
        var result = new AnalysisResult(unused);
        assertEquals(2, result.unusedClasses().size());
        assertTrue(result.unusedClasses().contains("Class1"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should detect unused interface")
    void unusedClassVisitor_unusedInterface_detectsUnused() {
        var content = "interface Printable {} interface Unused {} class Printer implements Printable {}";
        var source = new SourceFile(content);
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertTrue(result.unusedClasses().contains("Unused"));
        assertFalse(result.unusedClasses().contains("Printable"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should not report implemented interfaces")
    void unusedClassVisitor_implementedInterface_notReported() {
        var content = "interface Serializable {} class Data implements Serializable {} new Data();";
        var source = new SourceFile(content);
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit(source);
        assertFalse(result.unusedClasses().contains("Serializable"));
        assertFalse(result.unusedClasses().contains("Data"));
    }
}
