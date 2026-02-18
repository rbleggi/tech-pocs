package com.rbleggi.domainfinetuned

data class DomainQuery(val question: String, val domain: String)

data class DomainResponse(val query: DomainQuery, val answer: String, val confidence: Double)

interface DomainKnowledgeBase {
    fun query(question: String): DomainResponse?
}

class MedicalKnowledgeBase : DomainKnowledgeBase {
    private val knowledge = mapOf(
        "diabetes" to "Diabetes e uma doenca cronica que afeta a forma como o corpo processa acucar no sangue.",
        "hipertensao" to "Hipertensao e a pressao arterial elevada, um fator de risco para doencas cardiovasculares.",
        "febre" to "Febre e o aumento da temperatura corporal, geralmente indicando uma infeccao.",
        "gripe" to "Gripe e uma infeccao viral respiratoria que causa febre, tosse e dores no corpo."
    )

    override fun query(question: String): DomainResponse? {
        val lowerQuestion = question.lowercase()
        val match = knowledge.entries.firstOrNull { (key, _) -> key in lowerQuestion }

        return match?.let {
            DomainResponse(
                DomainQuery(question, "medical"),
                "Resposta Medica: ${it.value}",
                0.85
            )
        }
    }
}

class LegalKnowledgeBase : DomainKnowledgeBase {
    private val knowledge = mapOf(
        "clt" to "CLT (Consolidacao das Leis do Trabalho) e a legislacao trabalhista brasileira que regula as relacoes de trabalho.",
        "demissao" to "Demissao pode ser sem justa causa (com direito a FGTS e aviso previo) ou com justa causa (sem direitos).",
        "ferias" to "Trabalhadores tem direito a 30 dias de ferias apos 12 meses de trabalho, com adicional de 1/3 do salario.",
        "13 salario" to "13 salario e uma gratificacao natalina equivalente a 1/12 da remuneracao por mes trabalhado."
    )

    override fun query(question: String): DomainResponse? {
        val lowerQuestion = question.lowercase()
        val match = knowledge.entries.firstOrNull { (key, _) -> key in lowerQuestion }

        return match?.let {
            DomainResponse(
                DomainQuery(question, "legal"),
                "Resposta Juridica: ${it.value}",
                0.90
            )
        }
    }
}

class TechKnowledgeBase : DomainKnowledgeBase {
    private val knowledge = mapOf(
        "kotlin" to "Kotlin e uma linguagem de programacao moderna para a JVM, Android e desenvolvimento multiplataforma.",
        "jvm" to "JVM (Java Virtual Machine) e a maquina virtual que executa bytecode Java e outras linguagens JVM.",
        "rest" to "REST e um estilo arquitetural para APIs web que usa HTTP para comunicacao cliente-servidor.",
        "docker" to "Docker e uma plataforma de containerizacao que empacota aplicacoes e suas dependencias."
    )

    override fun query(question: String): DomainResponse? {
        val lowerQuestion = question.lowercase()
        val match = knowledge.entries.firstOrNull { (key, _) -> key in lowerQuestion }

        return match?.let {
            DomainResponse(
                DomainQuery(question, "tech"),
                "Resposta Tecnica: ${it.value}",
                0.95
            )
        }
    }
}

class DomainFineTunedLLM(private val domainStrategies: Map<String, DomainKnowledgeBase>) {
    fun ask(query: DomainQuery): DomainResponse {
        val knowledgeBase = domainStrategies[query.domain]
            ?: return DomainResponse(query, "Dominio nao suportado: ${query.domain}", 0.0)

        return knowledgeBase.query(query.question)
            ?: DomainResponse(query, "Nao encontrei informacoes sobre: ${query.question}", 0.3)
    }

    fun askMultipleDomains(question: String): Map<String, DomainResponse?> {
        return domainStrategies.mapValues { (domain, kb) ->
            kb.query(question)?.copy(query = DomainQuery(question, domain))
        }
    }
}

fun main() {
    val llm = DomainFineTunedLLM(
        mapOf(
            "medical" to MedicalKnowledgeBase(),
            "legal" to LegalKnowledgeBase(),
            "tech" to TechKnowledgeBase()
        )
    )

    println("=== Domain Fine-Tuned LLM ===")

    val queries = listOf(
        DomainQuery("O que e diabetes?", "medical"),
        DomainQuery("Como funciona a CLT?", "legal"),
        DomainQuery("O que e Kotlin?", "tech"),
        DomainQuery("Explique sobre ferias", "legal")
    )

    queries.forEach { query ->
        println("\n[${query.domain.uppercase()}] Pergunta: ${query.question}")
        val response = llm.ask(query)
        println("Resposta (confianca: ${(response.confidence * 100).toInt()}%): ${response.answer}")
    }

    println("\n=== Busca Multi-Dominio ===")
    val multiDomainResults = llm.askMultipleDomains("docker")
    multiDomainResults.forEach { (domain, response) ->
        println("[$domain] ${response?.answer ?: "Sem resposta"}")
    }
}
