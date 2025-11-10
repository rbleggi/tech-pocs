package com.rbleggi.unusedclassdetector

import java.io.File

interface ASTNode
data class ClassDecl(val name: String) : ASTNode
data class ClassUsage(val name: String) : ASTNode
data class SourceFile(val content: String) : ASTNode

interface ASTVisitor {
    fun visit(source: SourceFile): AnalysisResult
}

data class AnalysisResult(val unusedClasses: List<String>)

class UnusedClassVisitor : ASTVisitor {
    private val classDeclPattern = Regex("""class\s+(\w+)""")
    private val classUsagePattern = Regex("""new\s+(\w+)""")

    override fun visit(source: SourceFile): AnalysisResult {
        val decls = classDeclPattern.findAll(source.content).map { it.groupValues[1] }.toSet()
        val usages = classUsagePattern.findAll(source.content).map { it.groupValues[1] }.toSet()
        val excluded = setOf("ClassDecl", "ClassUsage", "SourceFile", "AnalysisResult")
        val unused = decls.minus(usages).filter { name ->
            name !in excluded && !name.endsWith("Decl") && !name.endsWith("Usage") && !name.endsWith("Result") && !name.endsWith("File")
        }
        return AnalysisResult(unused)
    }
}

fun main() {
    val path = "kotlin/unused-class-detector/src/main/kotlin/com/rbleggi/unusedclassdetector/Main.kt"
    val content = try {
        File(path).readText()
    } catch (e: Exception) {
        println("Could not read file: ${e.message}")
        return
    }
    val sourceFile = SourceFile(content)
    val visitor = UnusedClassVisitor()
    val result = visitor.visit(sourceFile)
    println("Unused classes:")
    result.unusedClasses.forEach(::println)
}

class UnusedExampleClass {
    fun hello(): String = "Hello, I am not used!"
}
