package com.rbleggi.personalaiassistant

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersonalAIAssistantTest {
    @Test
    fun `math handler performs addition`() {
        val handler = MathQueryHandler()
        val query = Query("somar 10 e 20", "math")
        val response = handler.handle(query)

        assertTrue(response.answer.contains("30"))
        assertEquals("math", response.handlerType)
    }

    @Test
    fun `math handler performs multiplication`() {
        val handler = MathQueryHandler()
        val query = Query("multiplicar 5 e 6", "math")
        val response = handler.handle(query)

        assertTrue(response.answer.contains("30"))
        assertEquals("math", response.handlerType)
    }

    @Test
    fun `text analysis handler counts words`() {
        val handler = TextAnalysisQueryHandler()
        val query = Query("contar palavras em \"ola mundo\"", "text-analysis")
        val response = handler.handle(query)

        assertTrue(response.answer.contains("2 palavras"))
        assertEquals("text-analysis", response.handlerType)
    }

    @Test
    fun `text analysis handler reverses text`() {
        val handler = TextAnalysisQueryHandler()
        val query = Query("inverter \"abc\"", "text-analysis")
        val response = handler.handle(query)

        assertTrue(response.answer.contains("cba"))
    }

    @Test
    fun `data lookup handler finds information`() {
        val handler = DataLookupQueryHandler()
        val query = Query("populacao de sao paulo", "data-lookup")
        val response = handler.handle(query)

        assertTrue(response.answer.contains("12 milhoes"))
        assertEquals("data-lookup", response.handlerType)
    }

    @Test
    fun `data lookup handler handles unknown queries`() {
        val handler = DataLookupQueryHandler()
        val query = Query("informacao desconhecida", "data-lookup")
        val response = handler.handle(query)

        assertTrue(response.answer.contains("Nao encontrei"))
    }

    @Test
    fun `assistant routes query to correct handler`() {
        val handlers = mapOf(
            "math" to MathQueryHandler(),
            "data-lookup" to DataLookupQueryHandler()
        )
        val assistant = PersonalAIAssistant(handlers)
        val query = Query("somar 1 e 2", "math")
        val response = assistant.ask(query)

        assertEquals("math", response.handlerType)
    }

    @Test
    fun `assistant handles unsupported query type`() {
        val assistant = PersonalAIAssistant(emptyMap())
        val query = Query("teste", "unsupported")
        val response = assistant.ask(query)

        assertTrue(response.answer.contains("nao suportado"))
        assertEquals("unknown", response.handlerType)
    }

    @Test
    fun `assistant processes multiple queries`() {
        val handlers = mapOf("math" to MathQueryHandler())
        val assistant = PersonalAIAssistant(handlers)
        val queries = listOf(
            Query("somar 1 e 2", "math"),
            Query("multiplicar 3 e 4", "math")
        )
        val responses = assistant.askMultiple(queries)

        assertEquals(2, responses.size)
        assertTrue(responses[0].answer.contains("3"))
        assertTrue(responses[1].answer.contains("12"))
    }
}
