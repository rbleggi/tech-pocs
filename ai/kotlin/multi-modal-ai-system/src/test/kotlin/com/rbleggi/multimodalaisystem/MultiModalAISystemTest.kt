package com.rbleggi.multimodalaisystem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MultiModalAISystemTest {
    @Test
    fun `text processing counts words and characters`() {
        val strategy = TextProcessingStrategy()
        val input = Input.TextInput("Ola mundo")
        val result = strategy.process(input)

        assertTrue(result.output.contains("2 palavras"))
        assertTrue(result.output.contains("9 caracteres"))
        assertEquals("text", result.mode)
    }

    @Test
    fun `numeric processing calculates square and sqrt`() {
        val strategy = NumericProcessingStrategy()
        val input = Input.NumericInput(16.0)
        val result = strategy.process(input)

        assertTrue(result.output.contains("256"))
        assertTrue(result.output.contains("4"))
        assertEquals("numeric", result.mode)
    }

    @Test
    fun `categorical processing maps categories`() {
        val strategy = CategoricalProcessingStrategy()
        val input = Input.CategoricalInput("produto")
        val result = strategy.process(input)

        assertTrue(result.output.contains("Itens Fisicos"))
        assertEquals("categorical", result.mode)
    }

    @Test
    fun `text processing rejects non-text input`() {
        val strategy = TextProcessingStrategy()
        val input = Input.NumericInput(10.0)
        val result = strategy.process(input)

        assertTrue(result.output.contains("invalida"))
    }

    @Test
    fun `system processes input using selected strategy`() {
        val system = MultiModalAISystem(TextProcessingStrategy())
        val input = Input.TextInput("teste")
        val result = system.process(input)

        assertEquals("text", result.mode)
    }

    @Test
    fun `system processes batch of inputs`() {
        val system = MultiModalAISystem(NumericProcessingStrategy())
        val inputs = listOf(
            Input.NumericInput(4.0),
            Input.NumericInput(9.0)
        )
        val results = system.processBatch(inputs)

        assertEquals(2, results.size)
        assertEquals("numeric", results[0].mode)
        assertEquals("numeric", results[1].mode)
    }

    @Test
    fun `categorical processing handles unknown category`() {
        val strategy = CategoricalProcessingStrategy()
        val input = Input.CategoricalInput("desconhecida")
        val result = strategy.process(input)

        assertTrue(result.output.contains("desconhecida"))
    }
}
