package com.rbleggi.aiagentsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    @Test
    @DisplayName("ResearcherAgent - pesquisa informacoes")
    void researcherAgent_performsResearch() {
        ResearcherAgent agent = new ResearcherAgent();
        Task task = new Task("t1", "Pesquisar sobre Java", "research");

        AgentResult result = agent.process(task, List.of());

        assertEquals("Researcher", result.agentName());
        assertTrue(result.result().contains("Java"));
        assertFalse(result.sentMessages().isEmpty());
    }

    @Test
    @DisplayName("WriterAgent - escreve artigo baseado em mensagens")
    void writerAgent_writesArticle() {
        WriterAgent agent = new WriterAgent();
        Task task = new Task("t1", "Escrever artigo", "write");
        Message researchMsg = new Message("Researcher", "Writer", "Dados de pesquisa", System.currentTimeMillis());

        AgentResult result = agent.process(task, List.of(researchMsg));

        assertEquals("Writer", result.agentName());
        assertTrue(result.result().contains("Article"));
    }

    @Test
    @DisplayName("ReviewerAgent - revisa artigo")
    void reviewerAgent_reviewsArticle() {
        ReviewerAgent agent = new ReviewerAgent();
        Task task = new Task("t1", "Revisar", "review");
        Message articleMsg = new Message("Writer", "Reviewer", "=== Article ===\nContent.\nConclusion: End", System.currentTimeMillis());

        AgentResult result = agent.process(task, List.of(articleMsg));

        assertEquals("Reviewer", result.agentName());
        assertTrue(result.result().contains("Review"));
    }

    @Test
    @DisplayName("AIAgentSystem - executa workflow completo")
    void agentSystem_executesWorkflow() {
        List<Agent> agents = List.of(
            new ResearcherAgent(),
            new WriterAgent(),
            new ReviewerAgent()
        );
        AIAgentSystem system = new AIAgentSystem(agents);
        Task task = new Task("t1", "Java", "article");
        List<String> workflow = List.of("Researcher", "Writer", "Reviewer");

        List<AgentResult> results = system.executeWorkflow(task, workflow);

        assertEquals(3, results.size());
        assertEquals("Researcher", results.get(0).agentName());
        assertEquals("Writer", results.get(1).agentName());
        assertEquals("Reviewer", results.get(2).agentName());
    }

    @Test
    @DisplayName("AIAgentSystem - rastreia mensagens")
    void agentSystem_tracksMessages() {
        List<Agent> agents = List.of(new ResearcherAgent(), new WriterAgent());
        AIAgentSystem system = new AIAgentSystem(agents);
        Task task = new Task("t1", "Python", "article");

        system.executeWorkflow(task, List.of("Researcher", "Writer"));
        List<Message> history = system.getMessageHistory();

        assertFalse(history.isEmpty());
    }
}
