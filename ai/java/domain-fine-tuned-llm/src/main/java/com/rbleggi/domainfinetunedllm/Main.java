package com.rbleggi.domainfinetunedllm;

import java.util.*;

record Prompt(String id, String text, String domain) {}

record LLMResponse(String promptId, String generatedText, double confidence, String domain) {}

record KnowledgeEntry(String topic, String content, String domain) {}

sealed interface DomainStrategy permits MedicalDomain, LegalDomain, TechDomain {
    LLMResponse generate(Prompt prompt, List<KnowledgeEntry> knowledgeBase);
    String getDomainName();
}

final class MedicalDomain implements DomainStrategy {
    @Override
    public String getDomainName() {
        return "medical";
    }

    @Override
    public LLMResponse generate(Prompt prompt, List<KnowledgeEntry> knowledgeBase) {
        String promptText = prompt.text().toLowerCase();
        List<KnowledgeEntry> relevantEntries = knowledgeBase.stream()
            .filter(entry -> entry.domain().equals("medical"))
            .filter(entry -> promptText.contains(entry.topic().toLowerCase()))
            .toList();

        if (relevantEntries.isEmpty()) {
            return new LLMResponse(
                prompt.id(),
                "Medical information not available in the knowledge base",
                0.3,
                "medical"
            );
        }

        String response = "Based on medical knowledge: " + relevantEntries.get(0).content();
        double confidence = relevantEntries.size() > 1 ? 0.9 : 0.7;
        return new LLMResponse(prompt.id(), response, confidence, "medical");
    }
}

final class LegalDomain implements DomainStrategy {
    @Override
    public String getDomainName() {
        return "legal";
    }

    @Override
    public LLMResponse generate(Prompt prompt, List<KnowledgeEntry> knowledgeBase) {
        String promptText = prompt.text().toLowerCase();
        List<KnowledgeEntry> relevantEntries = knowledgeBase.stream()
            .filter(entry -> entry.domain().equals("legal"))
            .filter(entry -> promptText.contains(entry.topic().toLowerCase()))
            .toList();

        if (relevantEntries.isEmpty()) {
            return new LLMResponse(
                prompt.id(),
                "Legal question not covered in the knowledge base",
                0.3,
                "legal"
            );
        }

        String response = "De acordo com a legislacao: " + relevantEntries.get(0).content();
        double confidence = 0.85;
        return new LLMResponse(prompt.id(), response, confidence, "legal");
    }
}

final class TechDomain implements DomainStrategy {
    @Override
    public String getDomainName() {
        return "tech";
    }

    @Override
    public LLMResponse generate(Prompt prompt, List<KnowledgeEntry> knowledgeBase) {
        String promptText = prompt.text().toLowerCase();
        List<KnowledgeEntry> relevantEntries = knowledgeBase.stream()
            .filter(entry -> entry.domain().equals("tech"))
            .filter(entry -> promptText.contains(entry.topic().toLowerCase()))
            .toList();

        if (relevantEntries.isEmpty()) {
            return new LLMResponse(
                prompt.id(),
                "Technical topic not found in the knowledge base",
                0.4,
                "tech"
            );
        }

        String response = "Technical information: " + relevantEntries.get(0).content();
        double confidence = 0.95;
        return new LLMResponse(prompt.id(), response, confidence, "tech");
    }
}

class DomainFineTunedLLM {
    private final Map<String, DomainStrategy> domainStrategies;
    private final List<KnowledgeEntry> knowledgeBase;

    public DomainFineTunedLLM(List<DomainStrategy> strategies, List<KnowledgeEntry> knowledgeBase) {
        this.domainStrategies = new HashMap<>();
        strategies.forEach(strategy -> domainStrategies.put(strategy.getDomainName(), strategy));
        this.knowledgeBase = knowledgeBase;
    }

    public LLMResponse generate(Prompt prompt) {
        DomainStrategy strategy = domainStrategies.get(prompt.domain());
        if (strategy == null) {
            return new LLMResponse(prompt.id(), "Domain not supported", 0.0, prompt.domain());
        }
        return strategy.generate(prompt, knowledgeBase);
    }

    public List<LLMResponse> generateBatch(List<Prompt> prompts) {
        return prompts.stream()
            .map(this::generate)
            .toList();
    }

    public Map<String, Double> getAverageConfidenceByDomain(List<Prompt> prompts) {
        List<LLMResponse> responses = generateBatch(prompts);
        return responses.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                LLMResponse::domain,
                java.util.stream.Collectors.averagingDouble(LLMResponse::confidence)
            ));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Domain Fine-Tuned LLM");
    }
}
