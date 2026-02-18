package com.rbleggi.domainfinetuned

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DomainFineTunedLLMTest {
    @Test
    fun `medical knowledge base answers medical questions`() {
        val kb = MedicalKnowledgeBase()
        val response = kb.query("O que e diabetes?")

        assertNotNull(response)
        assertTrue(response.answer.contains("Diabetes"))
        assertTrue(response.confidence > 0.5)
    }

    @Test
    fun `legal knowledge base answers legal questions`() {
        val kb = LegalKnowledgeBase()
        val response = kb.query("Como funciona a CLT?")

        assertNotNull(response)
        assertTrue(response.answer.contains("CLT"))
        assertEquals("legal", response.query.domain)
    }

    @Test
    fun `tech knowledge base answers tech questions`() {
        val kb = TechKnowledgeBase()
        val response = kb.query("O que e Kotlin?")

        assertNotNull(response)
        assertTrue(response.answer.contains("Kotlin"))
    }

    @Test
    fun `knowledge base returns null for unknown questions`() {
        val kb = MedicalKnowledgeBase()
        val response = kb.query("Pergunta desconhecida")

        assertNull(response)
    }

    @Test
    fun `LLM routes query to correct domain`() {
        val llm = DomainFineTunedLLM(
            mapOf("medical" to MedicalKnowledgeBase())
        )
        val query = DomainQuery("diabetes", "medical")
        val response = llm.ask(query)

        assertTrue(response.answer.contains("Diabetes"))
    }

    @Test
    fun `LLM handles unsupported domain`() {
        val llm = DomainFineTunedLLM(emptyMap())
        val query = DomainQuery("teste", "unsupported")
        val response = llm.ask(query)

        assertTrue(response.answer.contains("nao suportado"))
        assertEquals(0.0, response.confidence)
    }

    @Test
    fun `LLM handles unknown question in supported domain`() {
        val llm = DomainFineTunedLLM(
            mapOf("medical" to MedicalKnowledgeBase())
        )
        val query = DomainQuery("pergunta desconhecida", "medical")
        val response = llm.ask(query)

        assertTrue(response.answer.contains("Nao encontrei"))
        assertTrue(response.confidence < 0.5)
    }

    @Test
    fun `LLM searches across all domains`() {
        val llm = DomainFineTunedLLM(
            mapOf(
                "medical" to MedicalKnowledgeBase(),
                "tech" to TechKnowledgeBase()
            )
        )
        val results = llm.askMultipleDomains("kotlin")

        assertEquals(2, results.size)
        assertNull(results["medical"])
        assertNotNull(results["tech"])
    }

    @Test
    fun `medical knowledge base has high confidence for known topics`() {
        val kb = MedicalKnowledgeBase()
        val response = kb.query("febre")

        assertNotNull(response)
        assertTrue(response.confidence >= 0.8)
    }
}
