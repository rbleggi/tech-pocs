package com.rbleggi.ragsystem

data class Document(val id: String, val content: String, val keywords: Set<String>)

data class Query(val text: String, val keywords: Set<String>)

data class RetrievalResult(val document: Document, val score: Double, val method: String)

interface RetrievalStrategy {
    fun retrieve(query: Query, documents: List<Document>, topK: Int = 3): List<RetrievalResult>
}

class KeywordRetrievalStrategy : RetrievalStrategy {
    override fun retrieve(query: Query, documents: List<Document>, topK: Int): List<RetrievalResult> {
        return documents.map { doc ->
            val matchingKeywords = query.keywords.intersect(doc.keywords)
            val score = matchingKeywords.size.toDouble() / query.keywords.size.coerceAtLeast(1)
            RetrievalResult(doc, score, "keyword")
        }
        .filter { it.score > 0 }
        .sortedByDescending { it.score }
        .take(topK)
    }
}

class SemanticRetrievalStrategy : RetrievalStrategy {
    override fun retrieve(query: Query, documents: List<Document>, topK: Int): List<RetrievalResult> {
        return documents.map { doc ->
            val queryWords = query.text.lowercase().split(" ").toSet()
            val docWords = doc.content.lowercase().split(" ").toSet()
            val overlap = queryWords.intersect(docWords)
            val score = overlap.size.toDouble() / queryWords.size.coerceAtLeast(1)
            RetrievalResult(doc, score, "semantic")
        }
        .filter { it.score > 0 }
        .sortedByDescending { it.score }
        .take(topK)
    }
}

class HybridRetrievalStrategy(
    private val keywordWeight: Double = 0.5,
    private val semanticWeight: Double = 0.5
) : RetrievalStrategy {
    private val keywordStrategy = KeywordRetrievalStrategy()
    private val semanticStrategy = SemanticRetrievalStrategy()

    override fun retrieve(query: Query, documents: List<Document>, topK: Int): List<RetrievalResult> {
        val keywordResults = keywordStrategy.retrieve(query, documents, documents.size)
            .associateBy { it.document.id }
        val semanticResults = semanticStrategy.retrieve(query, documents, documents.size)
            .associateBy { it.document.id }

        val allDocIds = (keywordResults.keys + semanticResults.keys).toSet()

        return allDocIds.map { docId ->
            val keywordScore = keywordResults[docId]?.score ?: 0.0
            val semanticScore = semanticResults[docId]?.score ?: 0.0
            val hybridScore = keywordWeight * keywordScore + semanticWeight * semanticScore
            val doc = keywordResults[docId]?.document ?: semanticResults[docId]!!.document
            RetrievalResult(doc, hybridScore, "hybrid")
        }
        .filter { it.score > 0 }
        .sortedByDescending { it.score }
        .take(topK)
    }
}

class RAGSystem(private val strategy: RetrievalStrategy) {
    fun generateResponse(query: Query, documents: List<Document>, topK: Int = 3): String {
        val results = strategy.retrieve(query, documents, topK)

        if (results.isEmpty()) {
            return "Nao encontrei informacoes relevantes sobre: ${query.text}"
        }

        val context = results.joinToString("\n\n") {
            "Documento ${it.document.id} (score: %.2f):\n${it.document.content}".format(it.score)
        }

        return """
            |Baseado nos documentos encontrados:
            |
            |$context
            |
            |Resposta: Com base nas informacoes acima sobre '${query.text}', os documentos mais relevantes foram recuperados usando o metodo ${results.first().method}.
        """.trimMargin()
    }

    fun search(query: Query, documents: List<Document>, topK: Int = 3): List<RetrievalResult> {
        return strategy.retrieve(query, documents, topK)
    }
}

fun main() {
    val documents = listOf(
        Document(
            "doc1",
            "Joao Silva mora em Sao Paulo e trabalha como desenvolvedor Java na empresa Tech Brasil.",
            setOf("joao", "sao paulo", "java", "desenvolvedor")
        ),
        Document(
            "doc2",
            "Maria Santos e gerente de projetos em Curitiba, especializada em metodologias ageis.",
            setOf("maria", "curitiba", "gerente", "agil")
        ),
        Document(
            "doc3",
            "Carlos Oliveira e desenvolvedor Python em Belo Horizonte, trabalha com machine learning.",
            setOf("carlos", "belo horizonte", "python", "machine learning")
        ),
        Document(
            "doc4",
            "Ana Costa e analista de dados em Sao Paulo, usa Python e SQL para analise de negocios.",
            setOf("ana", "sao paulo", "python", "dados", "sql")
        )
    )

    val query = Query("desenvolvedores em Sao Paulo", setOf("sao paulo", "desenvolvedor"))

    println("=== RAG System - Keyword Retrieval ===")
    val keywordRAG = RAGSystem(KeywordRetrievalStrategy())
    println(keywordRAG.generateResponse(query, documents))

    println("\n=== RAG System - Semantic Retrieval ===")
    val semanticRAG = RAGSystem(SemanticRetrievalStrategy())
    println(semanticRAG.generateResponse(query, documents))

    println("\n=== RAG System - Hybrid Retrieval ===")
    val hybridRAG = RAGSystem(HybridRetrievalStrategy())
    println(hybridRAG.generateResponse(query, documents))
}
