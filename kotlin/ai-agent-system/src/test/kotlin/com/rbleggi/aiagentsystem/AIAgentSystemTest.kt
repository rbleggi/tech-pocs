package com.rbleggi.aiagentsystem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AIAgentSystemTest {
    @Test
    fun `researcher agent finds relevant information`() {
        val agent = ResearcherAgent()
        val output = agent.process("kotlin")

        assertTrue(output.contains("JetBrains"))
    }

    @Test
    fun `researcher agent handles unknown topics`() {
        val agent = ResearcherAgent()
        val output = agent.process("topico desconhecido")

        assertTrue(output.contains("Nao encontrei"))
    }

    @Test
    fun `writer agent formats content into article`() {
        val agent = WriterAgent()
        val input = "Ponto 1\nPonto 2\nPonto 3"
        val output = agent.process(input)

        assertTrue(output.contains("Artigo escrito"))
        assertTrue(output.contains("1. Ponto 1"))
        assertTrue(output.contains("3 pontos"))
    }

    @Test
    fun `reviewer agent counts words and lines`() {
        val agent = ReviewerAgent()
        val input = "Este e um texto de teste com varias palavras"
        val output = agent.process(input)

        assertTrue(output.contains("Revisao"))
        assertTrue(output.contains("Palavras:"))
    }

    @Test
    fun `reviewer agent suggests improvements for short text`() {
        val agent = ReviewerAgent()
        val input = "Texto curto"
        val output = agent.process(input)

        assertTrue(output.contains("muito curto"))
    }

    @Test
    fun `agent receives and stores messages`() {
        val agent = ResearcherAgent()
        val message = Message("sender", "pesquisador", "conteudo")
        agent.receiveMessage(message)

        val messages = agent.getMessages()
        assertEquals(1, messages.size)
        assertEquals("conteudo", messages[0].content)
    }

    @Test
    fun `system delivers messages to correct agent`() {
        val agents = mapOf(
            "pesquisador" to ResearcherAgent(),
            "escritor" to WriterAgent()
        )
        val system = AIAgentSystem(agents)
        val message = Message("pesquisador", "escritor", "teste")

        system.deliverMessage(message)

        val writerMessages = system.getAgentMessages("escritor")
        assertEquals(1, writerMessages.size)
        assertEquals("teste", writerMessages[0].content)
    }

    @Test
    fun `workflow runs all agents in sequence`() {
        val system = AIAgentSystem(
            mapOf(
                "pesquisador" to ResearcherAgent(),
                "escritor" to WriterAgent(),
                "revisor" to ReviewerAgent()
            )
        )

        val results = system.runWorkflow("kotlin")

        assertEquals(3, results.size)
        assertTrue(results.containsKey("pesquisador"))
        assertTrue(results.containsKey("escritor"))
        assertTrue(results.containsKey("revisor"))
    }

    @Test
    fun `workflow passes messages between agents`() {
        val system = AIAgentSystem(
            mapOf(
                "pesquisador" to ResearcherAgent(),
                "escritor" to WriterAgent()
            )
        )

        system.runWorkflow("kotlin")

        val writerMessages = system.getAgentMessages("escritor")
        assertTrue(writerMessages.isNotEmpty())
        assertEquals("pesquisador", writerMessages[0].from)
    }

    @Test
    fun `writer agent handles empty input`() {
        val agent = WriterAgent()
        val output = agent.process("")

        assertTrue(output.contains("Nenhum conteudo"))
    }
}
