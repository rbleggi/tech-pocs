package com.rbleggi.contentmoderationsystem

data class Content(val id: String, val text: String, val author: String)

data class ModerationResult(
    val content: Content,
    val isAllowed: Boolean,
    val reason: String,
    val filterType: String
)

interface ModerationFilter {
    fun moderate(content: Content): ModerationResult
}

class KeywordFilter(private val blockedKeywords: Set<String>) : ModerationFilter {
    override fun moderate(content: Content): ModerationResult {
        val lowerText = content.text.lowercase()
        val foundKeywords = blockedKeywords.filter { lowerText.contains(it.lowercase()) }

        return if (foundKeywords.isNotEmpty()) {
            ModerationResult(
                content,
                false,
                "Conteudo bloqueado por palavras proibidas: ${foundKeywords.joinToString(", ")}",
                "keyword"
            )
        } else {
            ModerationResult(content, true, "Aprovado", "keyword")
        }
    }
}

class RegexFilter(private val patterns: List<Regex>) : ModerationFilter {
    override fun moderate(content: Content): ModerationResult {
        val matchedPatterns = patterns.filter { it.containsMatchIn(content.text) }

        return if (matchedPatterns.isNotEmpty()) {
            ModerationResult(
                content,
                false,
                "Conteudo bloqueado por padroes suspeitos (${matchedPatterns.size} padroes)",
                "regex"
            )
        } else {
            ModerationResult(content, true, "Aprovado", "regex")
        }
    }
}

class LengthFilter(private val minLength: Int, private val maxLength: Int) : ModerationFilter {
    override fun moderate(content: Content): ModerationResult {
        val length = content.text.length

        return when {
            length < minLength -> ModerationResult(
                content,
                false,
                "Conteudo muito curto ($length caracteres, minimo $minLength)",
                "length"
            )
            length > maxLength -> ModerationResult(
                content,
                false,
                "Conteudo muito longo ($length caracteres, maximo $maxLength)",
                "length"
            )
            else -> ModerationResult(content, true, "Aprovado", "length")
        }
    }
}

class ContentModerationSystem(private val filters: List<ModerationFilter>) {
    fun moderate(content: Content): List<ModerationResult> {
        return filters.map { it.moderate(content) }
    }

    fun isContentAllowed(content: Content): Boolean {
        return filters.all { it.moderate(content).isAllowed }
    }

    fun moderateBatch(contents: List<Content>): Map<String, List<ModerationResult>> {
        return contents.associateBy({ it.id }, { moderate(it) })
    }
}

fun main() {
    println("Content Moderation System")
}
