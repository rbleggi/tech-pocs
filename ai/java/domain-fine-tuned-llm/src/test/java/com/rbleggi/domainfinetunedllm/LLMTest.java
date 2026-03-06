package com.rbleggi.domainfinetunedllm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LLMTest {
    private List<KnowledgeEntry> knowledgeBase;

    @BeforeEach
    void setUp() {
        knowledgeBase = List.of(
            new KnowledgeEntry("diabetes", "Doenca metabolica", "medical"),
            new KnowledgeEntry("clt", "Lei trabalhista", "legal"),
            new KnowledgeEntry("java", "Linguagem de programacao", "tech")
        );
    }

    @Test
    @DisplayName("MedicalDomain - gera resposta medica")
    void medicalDomain_generatesResponse() {
        MedicalDomain domain = new MedicalDomain();
        Prompt prompt = new Prompt("p1", "O que e diabetes?", "medical");

        LLMResponse response = domain.generate(prompt, knowledgeBase);

        assertEquals("medical", response.domain());
        assertTrue(response.generatedText().contains("metabolica"));
        assertTrue(response.confidence() > 0.5);
    }

    @Test
    @DisplayName("LegalDomain - gera resposta legal")
    void legalDomain_generatesResponse() {
        LegalDomain domain = new LegalDomain();
        Prompt prompt = new Prompt("p1", "Explique CLT", "legal");

        LLMResponse response = domain.generate(prompt, knowledgeBase);

        assertEquals("legal", response.domain());
        assertTrue(response.generatedText().contains("legislacao"));
    }

    @Test
    @DisplayName("TechDomain - gera resposta tecnica")
    void techDomain_generatesResponse() {
        TechDomain domain = new TechDomain();
        Prompt prompt = new Prompt("p1", "O que e Java?", "tech");

        LLMResponse response = domain.generate(prompt, knowledgeBase);

        assertEquals("tech", response.domain());
        assertTrue(response.generatedText().contains("programacao"));
    }

    @Test
    @DisplayName("DomainFineTunedLLM - gera respostas por dominio")
    void llm_generatesResponseByDomain() {
        List<DomainStrategy> strategies = List.of(new MedicalDomain(), new LegalDomain(), new TechDomain());
        DomainFineTunedLLM llm = new DomainFineTunedLLM(strategies, knowledgeBase);
        Prompt prompt = new Prompt("p1", "diabetes", "medical");

        LLMResponse response = llm.generate(prompt);

        assertNotNull(response);
        assertEquals("medical", response.domain());
    }

    @Test
    @DisplayName("DomainFineTunedLLM - processa lote")
    void llm_processesBatch() {
        List<DomainStrategy> strategies = List.of(new MedicalDomain(), new TechDomain());
        DomainFineTunedLLM llm = new DomainFineTunedLLM(strategies, knowledgeBase);
        List<Prompt> prompts = List.of(
            new Prompt("p1", "diabetes", "medical"),
            new Prompt("p2", "java", "tech")
        );

        List<LLMResponse> responses = llm.generateBatch(prompts);

        assertEquals(2, responses.size());
    }
}
