package com.rbleggi.codegenerationassistant

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CodeGenerationAssistantTest {
    @Test
    fun `class generation creates data class with properties`() {
        val strategy = ClassGenerationStrategy()
        val request = CodeRequest(
            "class",
            "Pessoa",
            mapOf("properties" to "nome:String, idade:Int")
        )
        val result = strategy.generate(request)

        assertTrue(result.code.contains("data class Pessoa"))
        assertTrue(result.code.contains("val nome: String"))
        assertTrue(result.code.contains("val idade: Int"))
        assertEquals("kotlin", result.language)
        assertEquals("class", result.type)
    }

    @Test
    fun `function generation creates function with parameters and return type`() {
        val strategy = FunctionGenerationStrategy()
        val request = CodeRequest(
            "function",
            "somar",
            mapOf("params" to "a:Int, b:Int", "returnType" to "Int")
        )
        val result = strategy.generate(request)

        assertTrue(result.code.contains("fun somar"))
        assertTrue(result.code.contains("a: Int, b: Int"))
        assertTrue(result.code.contains(": Int"))
        assertTrue(result.code.contains("TODO"))
        assertEquals("kotlin", result.language)
        assertEquals("function", result.type)
    }

    @Test
    fun `test generation creates test with assertion`() {
        val strategy = TestGenerationStrategy()
        val request = CodeRequest(
            "test",
            "deve somar dois numeros",
            mapOf("target" to "somar(2, 3)")
        )
        val result = strategy.generate(request)

        assertTrue(result.code.contains("@Test"))
        assertTrue(result.code.contains("deve somar dois numeros"))
        assertTrue(result.code.contains("somar(2, 3)"))
        assertTrue(result.code.contains("assertEquals"))
        assertEquals("kotlin", result.language)
        assertEquals("test", result.type)
    }

    @Test
    fun `assistant generates code using selected strategy`() {
        val assistant = CodeGenerationAssistant(ClassGenerationStrategy())
        val request = CodeRequest("class", "Produto", mapOf("properties" to "nome:String"))
        val result = assistant.generate(request)

        assertTrue(result.code.contains("data class Produto"))
    }

    @Test
    fun `assistant generates multiple code pieces`() {
        val assistant = CodeGenerationAssistant(FunctionGenerationStrategy())
        val requests = listOf(
            CodeRequest("function", "funcao1", mapOf("params" to "x:Int", "returnType" to "Int")),
            CodeRequest("function", "funcao2", mapOf("params" to "y:String", "returnType" to "String"))
        )
        val results = assistant.generateMultiple(requests)

        assertEquals(2, results.size)
        assertTrue(results[0].code.contains("funcao1"))
        assertTrue(results[1].code.contains("funcao2"))
    }
}
