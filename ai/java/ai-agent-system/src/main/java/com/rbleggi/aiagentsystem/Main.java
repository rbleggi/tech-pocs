package com.rbleggi.aiagentsystem;

import java.util.*;

record Task(String id, String description, String type) {}

record Message(String from, String to, String content, long timestamp) {}

record AgentResult(String agentName, String taskId, String result, List<Message> sentMessages) {}

sealed interface Agent permits ResearcherAgent, WriterAgent, ReviewerAgent {
    AgentResult process(Task task, List<Message> inbox);
    String getName();
}

final class ResearcherAgent implements Agent {
    private final Map<String, String> knowledgeBase;

    public ResearcherAgent() {
        this.knowledgeBase = Map.of(
            "java", "Java is an object-oriented language used for backend",
            "python", "Python is an interpreted language popular in data science",
            "scala", "Scala combines functional and object-oriented programming"
        );
    }

    @Override
    public String getName() {
        return "Researcher";
    }

    @Override
    public AgentResult process(Task task, List<Message> inbox) {
        String description = task.description().toLowerCase();
        List<Message> sentMessages = new ArrayList<>();

        StringBuilder research = new StringBuilder("Research result:\n");

        for (Map.Entry<String, String> entry : knowledgeBase.entrySet()) {
            if (description.contains(entry.getKey())) {
                research.append("- ").append(entry.getValue()).append("\n");
            }
        }

        if (research.toString().equals("Research result:\n")) {
            research.append("No information found");
        }

        Message toWriter = new Message(
            "Researcher",
            "Writer",
            research.toString(),
            System.currentTimeMillis()
        );
        sentMessages.add(toWriter);

        return new AgentResult(getName(), task.id(), research.toString(), sentMessages);
    }
}

final class WriterAgent implements Agent {
    @Override
    public String getName() {
        return "Writer";
    }

    @Override
    public AgentResult process(Task task, List<Message> inbox) {
        List<Message> sentMessages = new ArrayList<>();

        String researchData = inbox.stream()
            .filter(m -> m.to().equals("Writer"))
            .findFirst()
            .map(Message::content)
            .orElse("No research data");

        StringBuilder article = new StringBuilder();
        article.append("=== Article ===\n");
        article.append("Topic: ").append(task.description()).append("\n\n");
        article.append("Content based on research:\n");
        article.append(researchData).append("\n");
        article.append("\nConclusion: This article presents relevant information on the topic.");

        Message toReviewer = new Message(
            "Writer",
            "Reviewer",
            article.toString(),
            System.currentTimeMillis()
        );
        sentMessages.add(toReviewer);

        return new AgentResult(getName(), task.id(), article.toString(), sentMessages);
    }
}

final class ReviewerAgent implements Agent {
    @Override
    public String getName() {
        return "Reviewer";
    }

    @Override
    public AgentResult process(Task task, List<Message> inbox) {
        List<Message> sentMessages = new ArrayList<>();

        String article = inbox.stream()
            .filter(m -> m.to().equals("Reviewer"))
            .findFirst()
            .map(Message::content)
            .orElse("No article received");

        int wordCount = article.split("\\s+").length;
        boolean hasTitle = article.contains("===");
        boolean hasConclusion = article.toLowerCase().contains("conclusion");

        StringBuilder review = new StringBuilder();
        review.append("=== Review ===\n");
        review.append("Words: ").append(wordCount).append("\n");
        review.append("Has title: ").append(hasTitle ? "Yes" : "No").append("\n");
        review.append("Has conclusion: ").append(hasConclusion ? "Yes" : "No").append("\n");

        if (wordCount > 20 && hasTitle && hasConclusion) {
            review.append("Status: APPROVED");
        } else {
            review.append("Status: NEEDS REVISION");
        }

        return new AgentResult(getName(), task.id(), review.toString(), sentMessages);
    }
}

class AIAgentSystem {
    private final Map<String, Agent> agents;
    private final List<Message> messageQueue;

    public AIAgentSystem(List<Agent> agents) {
        this.agents = new HashMap<>();
        agents.forEach(agent -> this.agents.put(agent.getName(), agent));
        this.messageQueue = new ArrayList<>();
    }

    public List<AgentResult> executeWorkflow(Task task, List<String> agentOrder) {
        List<AgentResult> results = new ArrayList<>();
        List<Message> currentInbox = new ArrayList<>();

        for (String agentName : agentOrder) {
            Agent agent = agents.get(agentName);
            if (agent == null) continue;

            AgentResult result = agent.process(task, currentInbox);
            results.add(result);

            messageQueue.addAll(result.sentMessages());
            currentInbox.addAll(result.sentMessages());
        }

        return results;
    }

    public Map<String, Long> getMessageStats() {
        return messageQueue.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Message::from,
                java.util.stream.Collectors.counting()
            ));
    }

    public List<Message> getMessageHistory() {
        return new ArrayList<>(messageQueue);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("AI Agent System");
    }
}
