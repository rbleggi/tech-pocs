package com.rbleggi.personalaiassistant;

import java.util.*;
import java.util.stream.Collectors;

record Query(String id, String question, String type) {}

record Response(String queryId, String answer, Map<String, Object> metadata) {}

sealed interface QueryHandler permits MathHandler, TextAnalysisHandler, DataLookupHandler {
    Response handle(Query query);
    boolean canHandle(Query query);
}

final class MathHandler implements QueryHandler {
    @Override
    public boolean canHandle(Query query) {
        return query.type().equals("math");
    }

    @Override
    public Response handle(Query query) {
        String question = query.question().toLowerCase();
        Map<String, Object> metadata = new HashMap<>();

        if (question.contains("soma")) {
            List<Double> numbers = extractNumbers(question);
            double sum = numbers.stream().mapToDouble(Double::doubleValue).sum();
            metadata.put("operation", "soma");
            metadata.put("numbers", numbers);
            return new Response(query.id(), "O resultado da soma e: " + sum, metadata);
        }

        if (question.contains("media")) {
            List<Double> numbers = extractNumbers(question);
            double avg = numbers.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            metadata.put("operation", "media");
            metadata.put("numbers", numbers);
            return new Response(query.id(), String.format("A media e: %.2f", avg), metadata);
        }

        return new Response(query.id(), "Unknown math operation", metadata);
    }

    private List<Double> extractNumbers(String text) {
        List<Double> numbers = new ArrayList<>();
        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            try {
                numbers.add(Double.parseDouble(token));
            } catch (NumberFormatException ignored) {}
        }
        return numbers;
    }
}

final class TextAnalysisHandler implements QueryHandler {
    @Override
    public boolean canHandle(Query query) {
        return query.type().equals("text");
    }

    @Override
    public Response handle(Query query) {
        String question = query.question().toLowerCase();
        Map<String, Object> metadata = new HashMap<>();

        if (question.contains("contar palavras")) {
            String text = extractText(question);
            int wordCount = text.split("\\s+").length;
            metadata.put("operation", "contar_palavras");
            metadata.put("text", text);
            return new Response(query.id(), "O texto tem " + wordCount + " palavras", metadata);
        }

        if (question.contains("maiuscula")) {
            String text = extractText(question);
            metadata.put("operation", "maiuscula");
            return new Response(query.id(), text.toUpperCase(), metadata);
        }

        if (question.contains("reverter")) {
            String text = extractText(question);
            String reversed = new StringBuilder(text).reverse().toString();
            metadata.put("operation", "reverter");
            return new Response(query.id(), reversed, metadata);
        }

        return new Response(query.id(), "Unknown text operation", metadata);
    }

    private String extractText(String question) {
        if (question.contains(":")) {
            return question.substring(question.indexOf(":") + 1).trim();
        }
        return question;
    }
}

final class DataLookupHandler implements QueryHandler {
    private final Map<String, String> database;

    public DataLookupHandler() {
        this.database = Map.of(
            "joao", "Joao Silva, 30 anos, Engenheiro em Sao Paulo",
            "maria", "Maria Costa, 25 anos, Medica em Curitiba",
            "carlos", "Carlos Santos, 35 anos, Professor em Belo Horizonte"
        );
    }

    @Override
    public boolean canHandle(Query query) {
        return query.type().equals("lookup");
    }

    @Override
    public Response handle(Query query) {
        String question = query.question().toLowerCase();
        Map<String, Object> metadata = new HashMap<>();

        for (Map.Entry<String, String> entry : database.entrySet()) {
            if (question.contains(entry.getKey())) {
                metadata.put("key", entry.getKey());
                metadata.put("found", true);
                return new Response(query.id(), entry.getValue(), metadata);
            }
        }

        metadata.put("found", false);
        return new Response(query.id(), "Information not found", metadata);
    }
}

class PersonalAIAssistant {
    private final List<QueryHandler> handlers;

    public PersonalAIAssistant(List<QueryHandler> handlers) {
        this.handlers = handlers;
    }

    public Response processQuery(Query query) {
        return handlers.stream()
            .filter(handler -> handler.canHandle(query))
            .findFirst()
            .map(handler -> handler.handle(query))
            .orElse(new Response(query.id(), "Unsupported query type", Map.of()));
    }

    public List<Response> processBatch(List<Query> queries) {
        return queries.stream()
            .map(this::processQuery)
            .toList();
    }

    public Map<String, Long> getQueryTypeDistribution(List<Query> queries) {
        return queries.stream()
            .collect(Collectors.groupingBy(Query::type, Collectors.counting()));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Personal AI Assistant");
    }
}
