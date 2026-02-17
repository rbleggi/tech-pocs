package com.rbleggi.chatbot

import kotlin.test.*

class ChatbotTest {

    private val testMessage = Message("Joao", "Ola, bom dia!")

    @Test
    fun `KeywordMatchingStrategy responds to keywords`() {
        val strategy = KeywordMatchingStrategy()
        val response = strategy.respond(Message("User", "Qual o preco?"))

        assertTrue(response.response.contains("preco") || response.response.contains("R$"))
        assertTrue(response.confidence > 0.5)
    }

    @Test
    fun `KeywordMatchingStrategy handles unknown input`() {
        val strategy = KeywordMatchingStrategy()
        val response = strategy.respond(Message("User", "xyzabc"))

        assertTrue(response.response.contains("Desculpe") || response.response.contains("nao entendi"))
        assertTrue(response.confidence < 0.5)
    }

    @Test
    fun `PatternMatchingStrategy matches greeting pattern`() {
        val strategy = PatternMatchingStrategy()
        val response = strategy.respond(testMessage)

        assertTrue(response.response.contains("Ola") || response.response.contains("ajudar"))
        assertTrue(response.confidence > 0.8)
    }

    @Test
    fun `PatternMatchingStrategy matches pricing pattern`() {
        val strategy = PatternMatchingStrategy()
        val response = strategy.respond(Message("User", "Quanto custa o produto?"))

        assertTrue(response.response.contains("R$") || response.response.contains("valor"))
    }

    @Test
    fun `IntentBasedStrategy identifies greeting intent`() {
        val strategy = IntentBasedStrategy()
        val response = strategy.respond(testMessage)

        assertTrue(response.strategy.contains("Intent-Based"))
        assertTrue(response.confidence > 0.0)
    }

    @Test
    fun `IntentBasedStrategy identifies pricing intent`() {
        val strategy = IntentBasedStrategy()
        val response = strategy.respond(Message("User", "Qual o preco?"))

        assertTrue(response.response.contains("R$") || response.response.contains("produto"))
    }

    @Test
    fun `IntentBasedStrategy handles unknown intent`() {
        val strategy = IntentBasedStrategy()
        val response = strategy.respond(Message("User", "blablabla xyzabc"))

        assertTrue(response.strategy.contains("unknown"))
        assertTrue(response.confidence < 0.5)
    }

    @Test
    fun `Chatbot stores conversation history`() {
        val bot = Chatbot(KeywordMatchingStrategy())
        bot.chat(testMessage)
        bot.chat(testMessage)

        assertEquals(2, bot.getHistory().size)
    }

    @Test
    fun `Chatbot clears history`() {
        val bot = Chatbot(KeywordMatchingStrategy())
        bot.chat(testMessage)
        bot.clearHistory()

        assertEquals(0, bot.getHistory().size)
    }

    @Test
    fun `Chatbot returns response from strategy`() {
        val bot = Chatbot(KeywordMatchingStrategy())
        val response = bot.chat(Message("User", "ola"))

        assertNotNull(response.response)
        assertTrue(response.confidence >= 0.0)
    }

    @Test
    fun `Message data class stores fields correctly`() {
        val message = Message("User", "Text")

        assertEquals("User", message.user)
        assertEquals("Text", message.text)
    }

    @Test
    fun `ChatResponse contains all fields`() {
        val response = ChatResponse("response", 0.9, "strategy")

        assertEquals("response", response.response)
        assertEquals(0.9, response.confidence)
        assertEquals("strategy", response.strategy)
    }
}
