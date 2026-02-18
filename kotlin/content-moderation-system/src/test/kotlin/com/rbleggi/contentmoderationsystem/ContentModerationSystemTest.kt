package com.rbleggi.contentmoderationsystem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContentModerationSystemTest {
    @Test
    fun `keyword filter blocks content with banned words`() {
        val filter = KeywordFilter(setOf("spam", "golpe"))
        val content = Content("1", "Isso e spam!", "User")
        val result = filter.moderate(content)

        assertFalse(result.isAllowed)
        assertTrue(result.reason.contains("spam"))
        assertEquals("keyword", result.filterType)
    }

    @Test
    fun `keyword filter allows clean content`() {
        val filter = KeywordFilter(setOf("spam", "golpe"))
        val content = Content("1", "Conteudo legitimo", "User")
        val result = filter.moderate(content)

        assertTrue(result.isAllowed)
        assertEquals("Aprovado", result.reason)
    }

    @Test
    fun `regex filter blocks content matching patterns`() {
        val filter = RegexFilter(listOf(Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")))
        val content = Content("1", "Meu CPF e 123.456.789-00", "User")
        val result = filter.moderate(content)

        assertFalse(result.isAllowed)
        assertTrue(result.reason.contains("padroes"))
        assertEquals("regex", result.filterType)
    }

    @Test
    fun `length filter blocks short content`() {
        val filter = LengthFilter(10, 100)
        val content = Content("1", "Oi", "User")
        val result = filter.moderate(content)

        assertFalse(result.isAllowed)
        assertTrue(result.reason.contains("curto"))
        assertEquals("length", result.filterType)
    }

    @Test
    fun `length filter blocks long content`() {
        val filter = LengthFilter(10, 50)
        val content = Content("1", "a".repeat(100), "User")
        val result = filter.moderate(content)

        assertFalse(result.isAllowed)
        assertTrue(result.reason.contains("longo"))
    }

    @Test
    fun `system runs all filters on content`() {
        val filters = listOf(
            KeywordFilter(setOf("spam")),
            LengthFilter(5, 100)
        )
        val system = ContentModerationSystem(filters)
        val content = Content("1", "Conteudo valido", "User")
        val results = system.moderate(content)

        assertEquals(2, results.size)
    }

    @Test
    fun `system allows content passing all filters`() {
        val filters = listOf(
            KeywordFilter(setOf("spam")),
            LengthFilter(5, 100)
        )
        val system = ContentModerationSystem(filters)
        val content = Content("1", "Conteudo valido", "User")

        assertTrue(system.isContentAllowed(content))
    }

    @Test
    fun `system blocks content failing any filter`() {
        val filters = listOf(
            KeywordFilter(setOf("spam")),
            LengthFilter(5, 100)
        )
        val system = ContentModerationSystem(filters)
        val content = Content("1", "spam", "User")

        assertFalse(system.isContentAllowed(content))
    }

    @Test
    fun `system moderates batch of contents`() {
        val system = ContentModerationSystem(listOf(LengthFilter(5, 100)))
        val contents = listOf(
            Content("1", "Valido", "User1"),
            Content("2", "OK", "User2")
        )
        val results = system.moderateBatch(contents)

        assertEquals(2, results.size)
        assertTrue(results.containsKey("1"))
        assertTrue(results.containsKey("2"))
    }
}
