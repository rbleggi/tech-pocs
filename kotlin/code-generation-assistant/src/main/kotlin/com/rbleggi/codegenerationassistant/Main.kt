package com.rbleggi.codegenerationassistant

data class CodeRequest(val type: String, val name: String, val parameters: Map<String, String>)

data class GeneratedCode(val code: String, val language: String, val type: String)

interface CodeGenerationStrategy {
    fun generate(request: CodeRequest): GeneratedCode
}

class ClassGenerationStrategy : CodeGenerationStrategy {
    override fun generate(request: CodeRequest): GeneratedCode {
        val className = request.name
        val properties = request.parameters["properties"]?.split(",") ?: listOf()

        val propertiesCode = properties.joinToString("\n    ") { prop ->
            val (name, type) = prop.trim().split(":")
            "val $name: $type"
        }

        val code = """
            |data class $className(
            |    $propertiesCode
            |)
        """.trimMargin()

        return GeneratedCode(code, "kotlin", "class")
    }
}

class FunctionGenerationStrategy : CodeGenerationStrategy {
    override fun generate(request: CodeRequest): GeneratedCode {
        val functionName = request.name
        val params = request.parameters["params"]?.split(",") ?: listOf()
        val returnType = request.parameters["returnType"] ?: "Unit"

        val paramsCode = params.joinToString(", ") { param ->
            val (name, type) = param.trim().split(":")
            "$name: $type"
        }

        val code = """
            |fun $functionName($paramsCode): $returnType {
            |    TODO("Implementar logica")
            |}
        """.trimMargin()

        return GeneratedCode(code, "kotlin", "function")
    }
}

class TestGenerationStrategy : CodeGenerationStrategy {
    override fun generate(request: CodeRequest): GeneratedCode {
        val testName = request.name
        val targetFunction = request.parameters["target"] ?: "funcao"

        val code = """
            |@Test
            |fun `$testName`() {
            |    val resultado = $targetFunction()
            |    assertEquals(valorEsperado, resultado)
            |}
        """.trimMargin()

        return GeneratedCode(code, "kotlin", "test")
    }
}

class CodeGenerationAssistant(private val strategy: CodeGenerationStrategy) {
    fun generate(request: CodeRequest): GeneratedCode {
        return strategy.generate(request)
    }

    fun generateMultiple(requests: List<CodeRequest>): List<GeneratedCode> {
        return requests.map { generate(it) }
    }
}

fun main() {
    println("Code Generation Assistant")
}
