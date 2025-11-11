package com.rbleggi.yamlcodegenerator

interface GeneratorStrategy {
    fun generate(yaml: String): String
}

class DataClassGenerator : GeneratorStrategy {
    override fun generate(yaml: String): String {
        val lines = yaml.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        if (lines.isEmpty()) return "// empty YAML"

        val className = lines.first().replace(":", "")
        val fields = lines.drop(1).map { line ->
            val parts = line.split(":").map { it.trim() }
            "  val ${parts[0]}: ${parts[1]}"
        }
        return "data class $className(\n${fields.joinToString(",\n")}\n)"
    }
}

fun main() {
    val yamlContent = """
Person:
  name: String
  age: Int
  address: Address
  phones: List<String>

Address:
  street: String
  number: Int
  city: String
  state: String

Company:
  name: String
  cnpj: String
  employees: List<Person>
""".trimIndent()

    val generator: GeneratorStrategy = DataClassGenerator()
    val generatedCode = generator.generate(yamlContent)

    println("Generated code:\n$generatedCode")
}
