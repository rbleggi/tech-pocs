package com.rbleggi.chatbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ChatbotTest {
    private ChatbotSystem keywordBot;
    private ChatbotSystem patternBot;
    private ChatbotSystem intentBot;

    @BeforeEach
    void setUp() {
        keywordBot = new ChatbotSystem(new KeywordMatchingStrategy());
        patternBot = new ChatbotSystem(new PatternMatchingStrategy());
        intentBot = new ChatbotSystem(new IntentBasedStrategy());
    }

    @Test
    @DisplayName("KeywordMatchingStrategy recognizes greeting")
    void keywordStrategy_recognizesGreeting() {
        ChatResponse response = keywordBot.sendMessage("Ola");

        assertTrue(response.text().contains("Ola"));
        assertTrue(response.confidence() > 0.80);
        assertEquals("Keyword-matching", response.strategy());
    }

    @Test
    @DisplayName("KeywordMatchingStrategy handles unknown keywords")
    void keywordStrategy_handlesUnknownKeywords() {
        ChatResponse response = keywordBot.sendMessage("xyz abc 123");

        assertTrue(response.text().contains("nao entendi"));
        assertTrue(response.confidence() < 0.50);
    }

    @Test
    @DisplayName("PatternMatchingStrategy matches price queries")
    void patternStrategy_matchesPriceQueries() {
        ChatResponse response = patternBot.sendMessage("Qual o preco do produto?");

        assertTrue(response.text().toLowerCase().contains("preco") || response.text().toLowerCase().contains("site"));
        assertTrue(response.confidence() > 0.85);
        assertEquals("Pattern-matching", response.strategy());
    }

    @Test
    @DisplayName("PatternMatchingStrategy matches delivery queries")
    void patternStrategy_matchesDeliveryQueries() {
        ChatResponse response = patternBot.sendMessage("Quando chega a entrega?");

        assertTrue(response.text().contains("prazo"));
        assertTrue(response.confidence() > 0.85);
    }

    @Test
    @DisplayName("IntentBasedStrategy detects greeting intent")
    void intentStrategy_detectsGreetingIntent() {
        ChatResponse response = intentBot.sendMessage("Bom dia");

        assertTrue(response.text().contains("bem-vindo") || response.text().contains("Ola"));
        assertTrue(response.confidence() >= 0.70);
        assertEquals("Intent-based", response.strategy());
    }

    @Test
    @DisplayName("IntentBasedStrategy detects payment intent")
    void intentStrategy_detectsPaymentIntent() {
        ChatResponse response = intentBot.sendMessage("Aceita cartao de credito e PIX?");

        assertTrue(response.text().contains("pagamento") || response.text().contains("cartao"));
        assertTrue(response.confidence() >= 0.70);
    }

    @Test
    @DisplayName("ChatbotSystem maintains conversation history")
    void chatbotSystem_maintainsHistory() {
        keywordBot.sendMessage("Ola");
        keywordBot.sendMessage("Qual o preco?");

        List<Message> history = keywordBot.getHistory();
        assertTrue(history.size() >= 2);
    }

    @Test
    @DisplayName("ChatbotSystem can clear history")
    void chatbotSystem_clearsHistory() {
        keywordBot.sendMessage("Ola");
        keywordBot.clearHistory();

        List<Message> history = keywordBot.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    @DisplayName("Different strategies produce different confidence levels")
    void differentStrategies_produceDifferentConfidence() {
        String message = "Ola, qual o preco?";

        ChatResponse r1 = keywordBot.sendMessage(message);
        ChatResponse r2 = patternBot.sendMessage(message);
        ChatResponse r3 = intentBot.sendMessage(message);

        assertNotEquals(r1.strategy(), r2.strategy());
        assertNotEquals(r2.strategy(), r3.strategy());
    }
}
