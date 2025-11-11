package com.rbleggi.yamlcodegenerator

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class YamlCodeGeneratorTest {
    @Test
    fun testBasicCodeGeneration() {
        val yaml = """
Person:
  name: String
  age: Int
        """.trimIndent()

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertTrue(result.contains("data class Person"))
        assertTrue(result.contains("val name: String"))
        assertTrue(result.contains("val age: Int"))
    }

    @Test
    fun testComplexTypesGeneration() {
        val yaml = """
Company:
  name: String
  cnpj: String
  employees: List<Person>
        """.trimIndent()

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertTrue(result.contains("data class Company"))
        assertTrue(result.contains("val name: String"))
        assertTrue(result.contains("val cnpj: String"))
        assertTrue(result.contains("val employees: List<Person>"))
    }

    @Test
    fun testMultipleFieldsFormatting() {
        val yaml = """
Address:
  street: String
  number: Int
  city: String
  state: String
        """.trimIndent()

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertTrue(result.contains("data class Address"))
        assertTrue(result.contains("val street: String"))
        assertTrue(result.contains("val number: Int"))
        assertTrue(result.contains("val city: String"))
        assertTrue(result.contains("val state: String"))
    }

    @Test
    fun testEmptyYaml() {
        val yaml = ""

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertEquals("// empty YAML", result)
    }

    @Test
    fun testYamlWithOnlyClassName() {
        val yaml = "Person:"

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertEquals("data class Person(\n\n)", result)
    }

    @Test
    fun testOutputFormatWithCommas() {
        val yaml = """
Person:
  name: String
  age: Int
  email: String
        """.trimIndent()

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        val lines = result.split("\n")
        assertTrue(lines[1].contains("val name: String,"))
        assertTrue(lines[2].contains("val age: Int,"))
        assertTrue(lines[3].contains("val email: String"))
    }

    @Test
    fun testDifferentPrimitiveTypes() {
        val yaml = """
DataTypes:
  text: String
  count: Int
  price: Double
  active: Boolean
  quantity: Long
        """.trimIndent()

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertTrue(result.contains("val text: String"))
        assertTrue(result.contains("val count: Int"))
        assertTrue(result.contains("val price: Double"))
        assertTrue(result.contains("val active: Boolean"))
        assertTrue(result.contains("val quantity: Long"))
    }

    @Test
    fun testNestedComplexTypes() {
        val yaml = """
Order:
  id: String
  customer: Person
  items: List<Item>
  address: Address
        """.trimIndent()

        val generator = DataClassGenerator()
        val result = generator.generate(yaml)

        assertTrue(result.contains("data class Order"))
        assertTrue(result.contains("val id: String"))
        assertTrue(result.contains("val customer: Person"))
        assertTrue(result.contains("val items: List<Item>"))
        assertTrue(result.contains("val address: Address"))
    }
}
