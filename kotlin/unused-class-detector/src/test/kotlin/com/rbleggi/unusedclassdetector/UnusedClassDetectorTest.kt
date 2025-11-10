package com.rbleggi.unusedclassdetector

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class UnusedClassDetectorTest {
    @Test
    fun testUnusedClassDetection() {
        val source = """
            class UsedClass
            class UnusedClass
            val instance = new UsedClass()
        """.trimIndent()

        val visitor = UnusedClassVisitor()
        val result = visitor.visit(SourceFile(source))

        assertTrue(result.unusedClasses.contains("UnusedClass"))
    }
}
