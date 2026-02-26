package com.rbleggi.unusedclassdetector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnusedClassDetectorTest {

    @Test
    @DisplayName("UnusedClassVisitor should detect unused class")
    void unusedClassVisitor_unusedClass_detectsUnused() {
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("class UsedClass {} class UnusedClass {} new UsedClass();");
        assertTrue(result.unusedClasses().contains("UnusedClass"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should not report used classes")
    void unusedClassVisitor_usedClass_notReported() {
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("class MyClass {} new MyClass();");
        assertFalse(result.unusedClasses().contains("MyClass"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should handle multiple classes")
    void unusedClassVisitor_multipleClasses_detectsCorrectly() {
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("class Used1 {} class Used2 {} class Unused {} new Used1(); new Used2();");
        assertTrue(result.unusedClasses().contains("Unused"));
        assertFalse(result.unusedClasses().contains("Used1"));
        assertFalse(result.unusedClasses().contains("Used2"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should exclude framework classes")
    void unusedClassVisitor_frameworkClasses_excluded() {
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("class AnalysisResult {} class Main {}");
        assertFalse(result.unusedClasses().contains("AnalysisResult"));
        assertFalse(result.unusedClasses().contains("Main"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should handle empty source")
    void unusedClassVisitor_emptySource_returnsEmpty() {
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("");
        assertTrue(result.unusedClasses().isEmpty());
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
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("interface Printable {} interface Unused {} class Printer implements Printable {}");
        assertTrue(result.unusedClasses().contains("Unused"));
        assertFalse(result.unusedClasses().contains("Printable"));
    }

    @Test
    @DisplayName("UnusedClassVisitor should not report implemented interfaces")
    void unusedClassVisitor_implementedInterface_notReported() {
        var visitor = new UnusedClassVisitor();
        var result = visitor.visit("interface Serializable {} class Data implements Serializable {} new Data();");
        assertFalse(result.unusedClasses().contains("Serializable"));
        assertFalse(result.unusedClasses().contains("Data"));
    }
}
