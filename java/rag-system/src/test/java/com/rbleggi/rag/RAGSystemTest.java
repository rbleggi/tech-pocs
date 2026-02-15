package com.rbleggi.rag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RAGSystemTest {
    private List<Document> testDocs;

    @BeforeEach
    void setUp() {
        testDocs = List.of(
            new Document("doc1", "Java e uma linguagem de programacao orientada a objetos", Map.of()),
            new Document("doc2", "Python e usado para ciencia de dados", Map.of()),
            new Document("doc3", "Sao Paulo e a maior cidade do Brasil", Map.of())
        );
    }

    @Test
    @DisplayName("KeywordRetrievalStrategy - encontra documentos por palavras-chave")
    void keywordStrategy_findsRelevantDocuments() {
        KeywordRetrievalStrategy strategy = new KeywordRetrievalStrategy();
        List<RetrievalResult> results = strategy.retrieve("Java programacao", testDocs, 2);

        assertFalse(results.isEmpty());
        assertEquals("doc1", results.get(0).documentId());
        assertTrue(results.get(0).score() > 0);
    }

    @Test
    @DisplayName("SemanticRetrievalStrategy - calcula similaridade")
    void semanticStrategy_calculatesSimilarity() {
        SemanticRetrievalStrategy strategy = new SemanticRetrievalStrategy();
        List<RetrievalResult> results = strategy.retrieve("linguagem programacao", testDocs, 2);

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).score() > 0);
    }

    @Test
    @DisplayName("HybridRetrievalStrategy - combina resultados")
    void hybridStrategy_combinesResults() {
        HybridRetrievalStrategy strategy = new HybridRetrievalStrategy(0.6, 0.4);
        List<RetrievalResult> results = strategy.retrieve("Java", testDocs, 3);

        assertFalse(results.isEmpty());
    }

    @Test
    @DisplayName("RAGSystem - processa query completa")
    void ragSystem_processesQuery() {
        RAGSystem rag = new RAGSystem(new KeywordRetrievalStrategy(), testDocs);
        QueryResult result = rag.query("Java", 2);

        assertEquals("Java", result.query());
        assertFalse(result.results().isEmpty());
        assertNotNull(result.response());
    }

    @Test
    @DisplayName("RAGSystem - retorna mensagem quando nao encontra documentos")
    void ragSystem_returnsNotFoundMessage() {
        RAGSystem rag = new RAGSystem(new KeywordRetrievalStrategy(), testDocs);
        QueryResult result = rag.query("xyzabc123", 2);

        assertTrue(result.results().isEmpty());
        assertTrue(result.response().contains("nao encontrei"));
    }

    @Test
    @DisplayName("RAGSystem - batch query processa multiplas queries")
    void ragSystem_processesBatchQuery() {
        RAGSystem rag = new RAGSystem(new KeywordRetrievalStrategy(), testDocs);
        List<QueryResult> results = rag.batchQuery(List.of("Java", "Python"), 2);

        assertEquals(2, results.size());
        assertEquals("Java", results.get(0).query());
        assertEquals("Python", results.get(1).query());
    }

    @Test
    @DisplayName("ResponseGenerator - gera resposta com documentos encontrados")
    void responseGenerator_generatesResponse() {
        ResponseGenerator generator = new ResponseGenerator();
        List<RetrievalResult> results = List.of(
            new RetrievalResult("doc1", 0.9, "Java e uma linguagem...")
        );

        String response = generator.generate("Java", results, testDocs);

        assertTrue(response.contains("Baseado nos documentos"));
        assertTrue(response.contains("0.9"));
    }
}
