package com.rbleggi.rag;

import java.util.*;
import java.util.stream.Collectors;

record Document(String id, String content, Map<String, String> metadata) {}

record RetrievalResult(String documentId, double score, String snippet) {}

record QueryResult(String query, List<RetrievalResult> results, String response) {}

sealed interface RetrievalStrategy permits KeywordRetrievalStrategy, SemanticRetrievalStrategy, HybridRetrievalStrategy {
    List<RetrievalResult> retrieve(String query, List<Document> documents, int topK);
}

final class KeywordRetrievalStrategy implements RetrievalStrategy {
    @Override
    public List<RetrievalResult> retrieve(String query, List<Document> documents, int topK) {
        String[] queryTerms = query.toLowerCase().split("\\s+");

        return documents.stream()
            .map(doc -> {
                String content = doc.content().toLowerCase();
                long matchCount = Arrays.stream(queryTerms)
                    .filter(content::contains)
                    .count();
                double score = (double) matchCount / queryTerms.length;
                String snippet = extractSnippet(doc.content(), queryTerms[0]);
                return new RetrievalResult(doc.id(), score, snippet);
            })
            .filter(r -> r.score() > 0)
            .sorted(Comparator.comparingDouble(RetrievalResult::score).reversed())
            .limit(topK)
            .toList();
    }

    private String extractSnippet(String content, String term) {
        int index = content.toLowerCase().indexOf(term.toLowerCase());
        if (index == -1) return content.substring(0, Math.min(100, content.length()));
        int start = Math.max(0, index - 50);
        int end = Math.min(content.length(), index + 100);
        return content.substring(start, end) + "...";
    }
}

final class SemanticRetrievalStrategy implements RetrievalStrategy {
    @Override
    public List<RetrievalResult> retrieve(String query, List<Document> documents, int topK) {
        Set<String> queryWords = new HashSet<>(Arrays.asList(query.toLowerCase().split("\\s+")));

        return documents.stream()
            .map(doc -> {
                Set<String> docWords = new HashSet<>(Arrays.asList(doc.content().toLowerCase().split("\\s+")));
                Set<String> intersection = new HashSet<>(queryWords);
                intersection.retainAll(docWords);

                Set<String> union = new HashSet<>(queryWords);
                union.addAll(docWords);

                double similarity = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
                String snippet = doc.content().substring(0, Math.min(100, doc.content().length()));
                return new RetrievalResult(doc.id(), similarity, snippet + "...");
            })
            .filter(r -> r.score() > 0)
            .sorted(Comparator.comparingDouble(RetrievalResult::score).reversed())
            .limit(topK)
            .toList();
    }
}

final class HybridRetrievalStrategy implements RetrievalStrategy {
    private final double keywordWeight;
    private final double semanticWeight;

    public HybridRetrievalStrategy(double keywordWeight, double semanticWeight) {
        this.keywordWeight = keywordWeight;
        this.semanticWeight = semanticWeight;
    }

    @Override
    public List<RetrievalResult> retrieve(String query, List<Document> documents, int topK) {
        KeywordRetrievalStrategy keywordStrategy = new KeywordRetrievalStrategy();
        SemanticRetrievalStrategy semanticStrategy = new SemanticRetrievalStrategy();

        List<RetrievalResult> keywordResults = keywordStrategy.retrieve(query, documents, documents.size());
        List<RetrievalResult> semanticResults = semanticStrategy.retrieve(query, documents, documents.size());

        Map<String, Double> combinedScores = new HashMap<>();

        for (RetrievalResult kr : keywordResults) {
            combinedScores.merge(kr.documentId(), kr.score() * keywordWeight, Double::sum);
        }

        for (RetrievalResult sr : semanticResults) {
            combinedScores.merge(sr.documentId(), sr.score() * semanticWeight, Double::sum);
        }

        return combinedScores.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(topK)
            .map(entry -> {
                Document doc = documents.stream()
                    .filter(d -> d.id().equals(entry.getKey()))
                    .findFirst()
                    .orElseThrow();
                String snippet = doc.content().substring(0, Math.min(100, doc.content().length()));
                return new RetrievalResult(entry.getKey(), entry.getValue(), snippet + "...");
            })
            .toList();
    }
}

class ResponseGenerator {
    public String generate(String query, List<RetrievalResult> retrievedDocs, List<Document> allDocs) {
        if (retrievedDocs.isEmpty()) {
            return "Desculpe, nao encontrei informacoes relevantes sobre: " + query;
        }

        StringBuilder response = new StringBuilder();
        response.append("Baseado nos documentos encontrados:\n\n");

        for (int i = 0; i < Math.min(3, retrievedDocs.size()); i++) {
            RetrievalResult result = retrievedDocs.get(i);
            Document doc = allDocs.stream()
                .filter(d -> d.id().equals(result.documentId()))
                .findFirst()
                .orElseThrow();

            response.append(String.format("%d. [Score: %.2f] %s\n",
                i + 1, result.score(), result.snippet()));
        }

        return response.toString();
    }
}

class RAGSystem {
    private final RetrievalStrategy retrievalStrategy;
    private final ResponseGenerator responseGenerator;
    private final List<Document> knowledgeBase;

    public RAGSystem(RetrievalStrategy retrievalStrategy, List<Document> knowledgeBase) {
        this.retrievalStrategy = retrievalStrategy;
        this.responseGenerator = new ResponseGenerator();
        this.knowledgeBase = knowledgeBase;
    }

    public QueryResult query(String query, int topK) {
        List<RetrievalResult> retrievedDocs = retrievalStrategy.retrieve(query, knowledgeBase, topK);
        String response = responseGenerator.generate(query, retrievedDocs, knowledgeBase);
        return new QueryResult(query, retrievedDocs, response);
    }

    public List<QueryResult> batchQuery(List<String> queries, int topK) {
        return queries.stream()
            .map(q -> query(q, topK))
            .toList();
    }
}

public class Main {
    public static void main(String[] args) {
        List<Document> knowledgeBase = List.of(
            new Document("doc1",
                "Joao Silva mora em Sao Paulo e trabalha como engenheiro de software. Ele gosta de programar em Java e Python.",
                Map.of("author", "Maria", "category", "perfil")),
            new Document("doc2",
                "O clima em Curitiba e geralmente frio no inverno. A temperatura pode chegar a 5 graus celsius.",
                Map.of("author", "Carlos", "category", "clima")),
            new Document("doc3",
                "A linguagem Java e amplamente utilizada para desenvolvimento backend. Muitas empresas em Sao Paulo contratam desenvolvedores Java.",
                Map.of("author", "Ana", "category", "tecnologia")),
            new Document("doc4",
                "Python e uma linguagem popular para ciencia de dados e machine learning. E facil de aprender para iniciantes.",
                Map.of("author", "Pedro", "category", "tecnologia")),
            new Document("doc5",
                "Belo Horizonte e conhecida por sua culinaria mineira. O pao de queijo e um prato tipico da regiao.",
                Map.of("author", "Julia", "category", "culinaria"))
        );

        System.out.println("=== Sistema RAG (Retrieval-Augmented Generation) ===\n");

        System.out.println("Estrategia: Keyword Retrieval");
        RAGSystem keywordRAG = new RAGSystem(new KeywordRetrievalStrategy(), knowledgeBase);
        QueryResult result1 = keywordRAG.query("Java em Sao Paulo", 3);
        System.out.println("Query: " + result1.query());
        System.out.println("Documentos encontrados: " + result1.results().size());
        System.out.println(result1.response());

        System.out.println("\nEstrategia: Semantic Retrieval");
        RAGSystem semanticRAG = new RAGSystem(new SemanticRetrievalStrategy(), knowledgeBase);
        QueryResult result2 = semanticRAG.query("programacao backend", 3);
        System.out.println("Query: " + result2.query());
        System.out.println("Documentos encontrados: " + result2.results().size());
        System.out.println(result2.response());

        System.out.println("\nEstrategia: Hybrid Retrieval (70% keyword, 30% semantic)");
        RAGSystem hybridRAG = new RAGSystem(new HybridRetrievalStrategy(0.7, 0.3), knowledgeBase);
        QueryResult result3 = hybridRAG.query("linguagens de programacao", 3);
        System.out.println("Query: " + result3.query());
        System.out.println("Documentos encontrados: " + result3.results().size());
        System.out.println(result3.response());

        List<String> queries = List.of("clima", "culinaria mineira");
        System.out.println("\nBatch Query:");
        List<QueryResult> batchResults = keywordRAG.batchQuery(queries, 2);
        batchResults.forEach(r -> {
            System.out.println("  Query: " + r.query() + " -> " + r.results().size() + " documentos");
        });
    }
}
