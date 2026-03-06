package com.rbleggi.personalaiassistant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssistantTest {

    @Test
    @DisplayName("MathHandler - calcula soma corretamente")
    void mathHandler_calculatesSum() {
        MathHandler handler = new MathHandler();
        Query query = new Query("q1", "soma de 5 10 15", "math");

        assertTrue(handler.canHandle(query));
        Response response = handler.handle(query);
        assertTrue(response.answer().contains("30"));
    }

    @Test
    @DisplayName("TextAnalysisHandler - conta palavras")
    void textHandler_countsWords() {
        TextAnalysisHandler handler = new TextAnalysisHandler();
        Query query = new Query("q1", "contar palavras: ola mundo teste", "text");

        assertTrue(handler.canHandle(query));
        Response response = handler.handle(query);
        assertTrue(response.answer().contains("3"));
    }

    @Test
    @DisplayName("DataLookupHandler - encontra dados")
    void lookupHandler_findsData() {
        DataLookupHandler handler = new DataLookupHandler();
        Query query = new Query("q1", "quem e joao", "lookup");

        assertTrue(handler.canHandle(query));
        Response response = handler.handle(query);
        assertTrue(response.answer().contains("Joao Silva"));
    }

    @Test
    @DisplayName("PersonalAIAssistant - processa query corretamente")
    void assistant_processesQuery() {
        List<QueryHandler> handlers = List.of(new MathHandler(), new TextAnalysisHandler(), new DataLookupHandler());
        PersonalAIAssistant assistant = new PersonalAIAssistant(handlers);
        Query query = new Query("q1", "soma de 1 2 3", "math");

        Response response = assistant.processQuery(query);
        assertNotNull(response);
        assertTrue(response.answer().contains("6"));
    }

    @Test
    @DisplayName("PersonalAIAssistant - processa lote de queries")
    void assistant_processesBatch() {
        List<QueryHandler> handlers = List.of(new MathHandler(), new TextAnalysisHandler());
        PersonalAIAssistant assistant = new PersonalAIAssistant(handlers);
        List<Query> queries = List.of(
            new Query("q1", "soma de 1 2", "math"),
            new Query("q2", "contar palavras: abc def", "text")
        );

        List<Response> responses = assistant.processBatch(queries);
        assertEquals(2, responses.size());
    }
}
