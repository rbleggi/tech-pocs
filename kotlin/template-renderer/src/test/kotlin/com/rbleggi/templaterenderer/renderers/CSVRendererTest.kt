package com.rbleggi.templaterenderer.renderers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CSVRendererTest {

    @Test
    fun `should generate valid CSV output`() {
        val output = String(CSVRenderer().render(mapOf("Name" to "Roger", "Age" to "30")))
        assertEquals("Name,Roger\nAge,30", output)
    }

    @Test
    fun `should handle single field`() {
        val output = String(CSVRenderer().render(mapOf("Field" to "Value")))
        assertEquals("Field,Value", output)
    }

    @Test
    fun `should handle multiple fields`() {
        val output = String(CSVRenderer().render(mapOf(
            "Name" to "Alice",
            "Age" to "25",
            "City" to "New York",
            "Country" to "USA"
        )))
        assertTrue(output.contains("Name,Alice"))
        assertTrue(output.contains("Age,25"))
        assertTrue(output.contains("City,New York"))
        assertTrue(output.contains("Country,USA"))
    }

    @Test
    fun `should handle empty map`() {
        val output = String(CSVRenderer().render(emptyMap()))
        assertEquals("", output)
    }

    @Test
    fun `should handle values with commas`() {
        val output = String(CSVRenderer().render(mapOf("Address" to "123 Main St, Apt 4")))
        assertEquals("Address,123 Main St, Apt 4", output)
    }

    @Test
    fun `should separate entries with newlines`() {
        val output = String(CSVRenderer().render(mapOf("First" to "1", "Second" to "2")))
        assertTrue(output.contains("\n"))
        val lines = output.split("\n")
        assertEquals(2, lines.size)
    }
}
