package com.rbleggi.personalaiassistant

data class Query(val text: String, val type: String)

data class AssistantResponse(val query: Query, val answer: String, val handlerType: String)

interface QueryHandler {
    fun handle(query: Query): AssistantResponse
}

class MathQueryHandler : QueryHandler {
    override fun handle(query: Query): AssistantResponse {
        val text = query.text.lowercase()

        val result = when {
            "somar" in text || "+" in text -> {
                val numbers = extractNumbers(text)
                if (numbers.size >= 2) {
                    val sum = numbers.sum()
                    "A soma de ${numbers.joinToString(" + ")} e $sum"
                } else {
                    "Nao consegui identificar os numeros para somar"
                }
            }
            "multiplicar" in text || "*" in text -> {
                val numbers = extractNumbers(text)
                if (numbers.size >= 2) {
                    val product = numbers.reduce { acc, n -> acc * n }
                    "O produto de ${numbers.joinToString(" x ")} e $product"
                } else {
                    "Nao consegui identificar os numeros para multiplicar"
                }
            }
            else -> "Operacao matematica nao reconhecida"
        }

        return AssistantResponse(query, result, "math")
    }

    private fun extractNumbers(text: String): List<Int> {
        return Regex("\\d+").findAll(text).map { it.value.toInt() }.toList()
    }
}

class TextAnalysisQueryHandler : QueryHandler {
    override fun handle(query: Query): AssistantResponse {
        val text = query.text.lowercase()

        val result = when {
            "contar palavras" in text -> {
                val target = extractQuotedText(query.text)
                if (target.isNotEmpty()) {
                    val count = target.split(" ").size
                    "O texto '$target' tem $count palavras"
                } else {
                    "Forneca um texto entre aspas para contar palavras"
                }
            }
            "inverter" in text -> {
                val target = extractQuotedText(query.text)
                if (target.isNotEmpty()) {
                    val reversed = target.reversed()
                    "O texto invertido e: $reversed"
                } else {
                    "Forneca um texto entre aspas para inverter"
                }
            }
            else -> "Operacao de analise de texto nao reconhecida"
        }

        return AssistantResponse(query, result, "text-analysis")
    }

    private fun extractQuotedText(text: String): String {
        val match = Regex("\"([^\"]+)\"").find(text)
        return match?.groupValues?.get(1) ?: ""
    }
}

class DataLookupQueryHandler : QueryHandler {
    private val database = mapOf(
        "populacao sao paulo" to "Sao Paulo tem aproximadamente 12 milhoes de habitantes",
        "populacao curitiba" to "Curitiba tem aproximadamente 1.9 milhoes de habitantes",
        "capital brasil" to "A capital do Brasil e Brasilia",
        "moeda brasil" to "A moeda do Brasil e o Real (BRL)"
    )

    override fun handle(query: Query): AssistantResponse {
        val text = query.text.lowercase()

        val result = database.entries.firstOrNull { (key, _) ->
            key.split(" ").all { it in text }
        }?.value ?: "Nao encontrei informacoes sobre: ${query.text}"

        return AssistantResponse(query, result, "data-lookup")
    }
}

class PersonalAIAssistant(private val handlers: Map<String, QueryHandler>) {
    fun ask(query: Query): AssistantResponse {
        val handler = handlers[query.type]
            ?: return AssistantResponse(query, "Tipo de consulta nao suportado: ${query.type}", "unknown")
        return handler.handle(query)
    }

    fun askMultiple(queries: List<Query>): List<AssistantResponse> {
        return queries.map { ask(it) }
    }
}

fun main() {
    println("Personal AI Assistant")
}
