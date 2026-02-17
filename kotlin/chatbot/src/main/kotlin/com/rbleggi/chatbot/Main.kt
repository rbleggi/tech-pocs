package com.rbleggi.chatbot

data class Message(
    val user: String,
    val text: String
)

data class ChatResponse(
    val response: String,
    val confidence: Double,
    val strategy: String
)

sealed interface ChatStrategy {
    fun respond(message: Message): ChatResponse
}

class KeywordMatchingStrategy : ChatStrategy {
    private val keywords = mapOf(
        "ola" to "Ola! Como posso ajudar voce hoje?",
        "preco" to "Nossos precos variam de R$ 50 a R$ 500. Qual produto voce procura?",
        "entrega" to "Entregamos em todo o Brasil. O prazo e de 5 a 10 dias uteis.",
        "pagamento" to "Aceitamos cartao, pix e boleto bancario.",
        "devolucao" to "Voce tem 30 dias para devolucao. Consulte nossa politica.",
        "horario" to "Atendemos de segunda a sexta, das 8h as 18h.",
        "obrigado" to "Por nada! Estou aqui para ajudar!",
        "tchau" to "Ate logo! Tenha um otimo dia!"
    )

    override fun respond(message: Message): ChatResponse {
        val text = message.text.lowercase()

        for ((keyword, response) in keywords) {
            if (text.contains(keyword)) {
                return ChatResponse(
                    response = response,
                    confidence = 0.9,
                    strategy = "Keyword Matching"
                )
            }
        }

        return ChatResponse(
            response = "Desculpe, nao entendi. Pode reformular sua pergunta?",
            confidence = 0.3,
            strategy = "Keyword Matching"
        )
    }
}

class PatternMatchingStrategy : ChatStrategy {
    override fun respond(message: Message): ChatResponse {
        val text = message.text.lowercase()

        return when {
            text.matches(Regex(".*\\b(ola|oi|bom dia|boa tarde)\\b.*")) ->
                ChatResponse(
                    "Ola ${message.user}! Como posso te ajudar?",
                    0.95,
                    "Pattern Matching"
                )
            text.matches(Regex(".*quanto custa.*|.*qual o preco.*|.*valor.*")) ->
                ChatResponse(
                    "Os valores variam entre R$ 50 e R$ 500. Sobre qual produto?",
                    0.85,
                    "Pattern Matching"
                )
            text.matches(Regex(".*onde.*|.*quando chega.*|.*prazo.*entrega.*")) ->
                ChatResponse(
                    "Entregamos em todo Brasil. Prazo: 5-10 dias uteis.",
                    0.85,
                    "Pattern Matching"
                )
            text.matches(Regex(".*como.*pagar.*|.*forma.*pagamento.*|.*pix.*cartao.*")) ->
                ChatResponse(
                    "Formas de pagamento: Cartao, Pix ou Boleto.",
                    0.85,
                    "Pattern Matching"
                )
            text.matches(Regex(".*obrigad.*|.*valeu.*")) ->
                ChatResponse(
                    "Disponha! Precisando, estou aqui.",
                    0.9,
                    "Pattern Matching"
                )
            text.matches(Regex(".*tchau.*|.*ate.*|.*adeus.*")) ->
                ChatResponse(
                    "Ate mais! Volte sempre!",
                    0.9,
                    "Pattern Matching"
                )
            else ->
                ChatResponse(
                    "Nao compreendi. Pode ser mais especifico?",
                    0.4,
                    "Pattern Matching"
                )
        }
    }
}

class IntentBasedStrategy : ChatStrategy {
    private data class Intent(
        val name: String,
        val patterns: List<String>,
        val response: String
    )

    private val intents = listOf(
        Intent("greeting", listOf("ola", "oi", "bom dia", "boa tarde", "boa noite"),
            "Ola! Bem-vindo ao atendimento. Como posso ajudar?"),
        Intent("pricing", listOf("preco", "custa", "valor", "quanto"),
            "Nossos produtos custam entre R$ 50 e R$ 500. Qual produto lhe interessa?"),
        Intent("delivery", listOf("entrega", "entregar", "prazo", "demora", "quanto tempo"),
            "Prazo de entrega: 5 a 10 dias uteis para todo o Brasil."),
        Intent("payment", listOf("pagamento", "pagar", "pix", "cartao", "boleto"),
            "Aceitamos Pix, Cartao de Credito e Boleto Bancario."),
        Intent("return", listOf("devolucao", "devolver", "troca", "trocar"),
            "Politica de devolucao: 30 dias. Entre em contato com o suporte."),
        Intent("hours", listOf("horario", "atendimento", "funciona", "aberto"),
            "Horario de atendimento: Segunda a Sexta, 8h as 18h."),
        Intent("thanks", listOf("obrigado", "obrigada", "valeu", "agradeco"),
            "De nada! Foi um prazer ajudar!"),
        Intent("goodbye", listOf("tchau", "ate logo", "adeus", "ate mais", "flw"),
            "Ate breve! Tenha um excelente dia!")
    )

    override fun respond(message: Message): ChatResponse {
        val text = message.text.lowercase()
        val words = text.split(" ", ",", ".", "!", "?")

        var bestIntent: Intent? = null
        var bestScore = 0

        for (intent in intents) {
            val matches = words.count { word -> intent.patterns.any { pattern -> word.contains(pattern) } }
            if (matches > bestScore) {
                bestScore = matches
                bestIntent = intent
            }
        }

        return if (bestIntent != null && bestScore > 0) {
            val confidence = (bestScore.toDouble() / words.size).coerceAtMost(0.95)
            ChatResponse(
                response = bestIntent.response,
                confidence = confidence,
                strategy = "Intent-Based (${bestIntent.name})"
            )
        } else {
            ChatResponse(
                response = "Desculpe, nao entendi sua mensagem. Tente perguntar sobre precos, entrega ou pagamento.",
                confidence = 0.2,
                strategy = "Intent-Based (unknown)"
            )
        }
    }
}

class Chatbot(private val strategy: ChatStrategy) {
    private val conversationHistory = mutableListOf<Pair<Message, ChatResponse>>()

    fun chat(message: Message): ChatResponse {
        val response = strategy.respond(message)
        conversationHistory.add(message to response)
        return response
    }

    fun getHistory(): List<Pair<Message, ChatResponse>> = conversationHistory.toList()

    fun clearHistory() = conversationHistory.clear()
}

fun main() {
    val user = "Joao"

    val messages = listOf(
        Message(user, "Ola, bom dia!"),
        Message(user, "Quanto custa o notebook?"),
        Message(user, "Qual o prazo de entrega para Sao Paulo?"),
        Message(user, "Posso pagar com Pix?"),
        Message(user, "Obrigado pela ajuda!")
    )

    println("=== Sistema de Chatbot ===\n")

    println("--- Estrategia: Keyword Matching ---")
    val keywordBot = Chatbot(KeywordMatchingStrategy())
    messages.take(3).forEach { msg ->
        val response = keywordBot.chat(msg)
        println("Usuario: ${msg.text}")
        println("Bot: ${response.response}")
        println("Confianca: %.2f\n".format(response.confidence))
    }

    println("--- Estrategia: Pattern Matching ---")
    val patternBot = Chatbot(PatternMatchingStrategy())
    messages.take(3).forEach { msg ->
        val response = patternBot.chat(msg)
        println("Usuario: ${msg.text}")
        println("Bot: ${response.response}")
        println("Confianca: %.2f\n".format(response.confidence))
    }

    println("--- Estrategia: Intent-Based ---")
    val intentBot = Chatbot(IntentBasedStrategy())
    messages.forEach { msg ->
        val response = intentBot.chat(msg)
        println("Usuario: ${msg.text}")
        println("Bot: ${response.response}")
        println("Confianca: %.2f | Estrategia: ${response.strategy}\n".format(response.confidence))
    }

    println("--- Historico de Conversa ---")
    println("Total de mensagens: ${intentBot.getHistory().size}")
}
