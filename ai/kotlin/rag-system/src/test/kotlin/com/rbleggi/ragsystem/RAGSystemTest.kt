package com.rbleggi.ragsystem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RAGSystemTest {
    private val documents = listOf(
        Document(
            "doc1",
            "Joao Silva mora em Sao Paulo e trabalha como desenvolvedor Java.",
            setOf("joao", "sao paulo", "java", "desenvolvedor")
        ),
        Document(
            "doc2",
            "Maria Santos e gerente em Curitiba.",
            setOf("maria", "curitiba", "gerente")
        ),
        Document(
            "doc3",
            "Carlos Oliveira e desenvolvedor Python em Belo Horizonte.",
            setOf("carlos", "belo horizonte", "python", "desenvolvedor")
        )
    )

    @Test
    fun `keyword retrieval finds matching documents by keywords`() {
        val strategy = KeywordRetrievalStrategy()
        val query = Query("desenvolvedores", setOf("desenvolvedor", "sao paulo"))
        val results = strategy.retrieve(query, documents, topK = 2)

        assertEquals(2, results.size)
        assertEquals("doc1", results[0].document.id)
        assertEquals(1.0, results[0].score)
        assertEquals("keyword", results[0].method)
    }

    @Test
    fun `semantic retrieval finds documents by content overlap`() {
        val strategy = SemanticRetrievalStrategy()
        val query = Query("desenvolvedor Java", setOf())
        val results = strategy.retrieve(query, documents, topK = 2)

        assertTrue(results.isNotEmpty())
        assertEquals("doc1", results[0].document.id)
        assertEquals("semantic", results[0].method)
    }

    @Test
    fun `hybrid retrieval combines keyword and semantic scores`() {
        val strategy = HybridRetrievalStrategy(keywordWeight = 0.6, semanticWeight = 0.4)
        val query = Query("desenvolvedor em Sao Paulo", setOf("desenvolvedor", "sao paulo"))
        val results = strategy.retrieve(query, documents, topK = 2)

        assertTrue(results.isNotEmpty())
        assertEquals("hybrid", results[0].method)
    }

    @Test
    fun `RAG system generates response with retrieved documents`() {
        val system = RAGSystem(KeywordRetrievalStrategy())
        val query = Query("desenvolvedores", setOf("desenvolvedor"))
        val response = system.generateResponse(query, documents, topK = 2)

        assertTrue(response.contains("Baseado nos documentos encontrados"))
        assertTrue(response.contains("doc1") || response.contains("doc3"))
    }

    @Test
    fun `RAG system returns not found message when no results`() {
        val system = RAGSystem(KeywordRetrievalStrategy())
        val query = Query("teste", setOf("inexistente"))
        val response = system.generateResponse(query, documents)

        assertTrue(response.contains("Nao encontrei informacoes relevantes"))
    }

    @Test
    fun `search returns top K results`() {
        val system = RAGSystem(SemanticRetrievalStrategy())
        val query = Query("desenvolvedor", setOf())
        val results = system.search(query, documents, topK = 1)

        assertEquals(1, results.size)
    }
}
