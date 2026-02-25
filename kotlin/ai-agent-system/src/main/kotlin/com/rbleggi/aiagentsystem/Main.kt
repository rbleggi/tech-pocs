package com.rbleggi.aiagentsystem

data class Message(val from: String, val to: String, val content: String, val timestamp: Long = System.currentTimeMillis())

data class AgentResult(val agentName: String, val output: String, val messagesReceived: Int)

interface Agent {
    val name: String
    fun process(input: String): String
    fun receiveMessage(message: Message)
    fun getMessages(): List<Message>
}

abstract class BaseAgent(override val name: String) : Agent {
    private val messages = mutableListOf<Message>()

    override fun receiveMessage(message: Message) {
        messages.add(message)
    }

    override fun getMessages(): List<Message> = messages.toList()

    protected fun sendMessage(to: String, content: String, system: AIAgentSystem) {
        val message = Message(name, to, content)
        system.deliverMessage(message)
    }
}

class ResearcherAgent : BaseAgent("pesquisador") {
    private val knowledge = mapOf(
        "kotlin" to "Kotlin e uma linguagem moderna para JVM criada pela JetBrains.",
        "brasil" to "Brasil e o maior pais da America do Sul, capital Brasilia.",
        "tecnologia" to "Tecnologia envolve aplicacao de conhecimento cientifico para fins praticos."
    )

    override fun process(input: String): String {
        val lowerInput = input.lowercase()
        val findings = knowledge.entries.filter { (key, _) -> key in lowerInput }

        return if (findings.isNotEmpty()) {
            "Pesquisa sobre '$input':\n" + findings.joinToString("\n") { "- ${it.value}" }
        } else {
            "Nao encontrei informacoes relevantes sobre '$input'"
        }
    }
}

class WriterAgent : BaseAgent("escritor") {
    override fun process(input: String): String {
        val sentences = input.split("\n").filter { it.isNotBlank() }

        return if (sentences.isEmpty()) {
            "Nenhum conteudo para escrever"
        } else {
            buildString {
                appendLine("Artigo escrito pelo agente escritor:")
                appendLine()
                sentences.forEachIndexed { index, sentence ->
                    appendLine("${index + 1}. ${sentence.trim()}")
                }
                appendLine()
                appendLine("Total de ${sentences.size} pontos abordados.")
            }
        }
    }
}

class ReviewerAgent : BaseAgent("revisor") {
    override fun process(input: String): String {
        val wordCount = input.split(" ").size
        val lineCount = input.split("\n").size
        val hasNumbers = Regex("\\d+").containsMatchIn(input)

        val issues = mutableListOf<String>()
        if (wordCount < 10) issues.add("Texto muito curto (minimo 10 palavras)")
        if (!hasNumbers) issues.add("Recomendo adicionar dados numericos para mais credibilidade")

        return buildString {
            appendLine("Revisao do conteudo:")
            appendLine("- Palavras: $wordCount")
            appendLine("- Linhas: $lineCount")
            appendLine()
            if (issues.isNotEmpty()) {
                appendLine("Sugestoes de melhoria:")
                issues.forEach { appendLine("- $it") }
            } else {
                appendLine("Conteudo aprovado sem restricoes!")
            }
        }
    }
}

class AIAgentSystem(private val agents: Map<String, Agent>) {
    fun deliverMessage(message: Message) {
        agents[message.to]?.receiveMessage(message)
    }

    fun runWorkflow(topic: String): Map<String, AgentResult> {
        val results = mutableMapOf<String, AgentResult>()

        val researcher = agents["pesquisador"] as? ResearcherAgent
        val writer = agents["escritor"] as? WriterAgent
        val reviewer = agents["revisor"] as? ReviewerAgent

        if (researcher != null) {
            val researchOutput = researcher.process(topic)
            results[researcher.name] = AgentResult(researcher.name, researchOutput, researcher.getMessages().size)

            if (writer != null) {
                val message = Message(researcher.name, writer.name, researchOutput)
                deliverMessage(message)

                val writerOutput = writer.process(researchOutput)
                results[writer.name] = AgentResult(writer.name, writerOutput, writer.getMessages().size)

                if (reviewer != null) {
                    val reviewMessage = Message(writer.name, reviewer.name, writerOutput)
                    deliverMessage(reviewMessage)

                    val reviewOutput = reviewer.process(writerOutput)
                    results[reviewer.name] = AgentResult(reviewer.name, reviewOutput, reviewer.getMessages().size)
                }
            }
        }

        return results
    }

    fun getAgentMessages(agentName: String): List<Message> {
        return agents[agentName]?.getMessages() ?: emptyList()
    }
}

fun main() {
    println("AI Agent System")
}
