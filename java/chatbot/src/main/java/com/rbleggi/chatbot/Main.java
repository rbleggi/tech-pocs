package com.rbleggi.chatbot;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

record Message(String text, long timestamp) {}

record ChatResponse(String text, double confidence, String strategy) {}

sealed interface ResponseStrategy permits KeywordMatchingStrategy, PatternMatchingStrategy, IntentBasedStrategy {
    ChatResponse respond(String userMessage, List<Message> history);
}

final class KeywordMatchingStrategy implements ResponseStrategy {
    private final Map<String, String> keywordResponses;

    public KeywordMatchingStrategy() {
        this.keywordResponses = Map.ofEntries(
            Map.entry("ola", "Ola! Como posso ajudar voce hoje?"),
            Map.entry("oi", "Oi! Em que posso ser util?"),
            Map.entry("preco", "Nossos precos variam de acordo com o produto. Consulte nosso catalogo."),
            Map.entry("entrega", "O prazo de entrega e de 3 a 7 dias uteis para todo o Brasil."),
            Map.entry("pagamento", "Aceitamos cartao de credito, debito, PIX e boleto bancario."),
            Map.entry("horario", "Nosso atendimento funciona de segunda a sexta, das 8h as 18h."),
            Map.entry("devolucao", "Voce pode devolver o produto em ate 30 dias apos a compra."),
            Map.entry("rastreio", "Para rastrear seu pedido, use o codigo enviado por email."),
            Map.entry("garantia", "Todos os produtos tem garantia de 12 meses."),
            Map.entry("tchau", "Ate logo! Tenha um otimo dia!")
        );
    }

    @Override
    public ChatResponse respond(String userMessage, List<Message> history) {
        String normalized = userMessage.toLowerCase();

        for (Map.Entry<String, String> entry : keywordResponses.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                return new ChatResponse(entry.getValue(), 0.85, "Keyword-matching");
            }
        }

        return new ChatResponse("Desculpe, nao entendi. Pode reformular?", 0.30, "Keyword-matching");
    }
}

final class PatternMatchingStrategy implements ResponseStrategy {
    private final List<PatternRule> rules;

    record PatternRule(Pattern pattern, String response) {}

    public PatternMatchingStrategy() {
        this.rules = List.of(
            new PatternRule(
                Pattern.compile("(qual|quais).*(preco|valor|custo).*", Pattern.CASE_INSENSITIVE),
                "Os precos estao disponiveis no nosso site. Posso ajudar com algum produto especifico?"
            ),
            new PatternRule(
                Pattern.compile("(como|onde).*(comprar|adquirir).*", Pattern.CASE_INSENSITIVE),
                "Voce pode comprar pelo nosso site ou em uma de nossas lojas fisicas."
            ),
            new PatternRule(
                Pattern.compile("(aceita|aceito).*(cartao|pix|boleto).*", Pattern.CASE_INSENSITIVE),
                "Sim! Aceitamos todas as formas de pagamento: cartao, PIX e boleto."
            ),
            new PatternRule(
                Pattern.compile("(quando|qual).*(entrega|chega|prazo).*", Pattern.CASE_INSENSITIVE),
                "O prazo de entrega e de 3 a 7 dias uteis, dependendo da sua regiao."
            ),
            new PatternRule(
                Pattern.compile(".*(problema|defeito|erro).*", Pattern.CASE_INSENSITIVE),
                "Sinto muito pelo problema. Por favor, entre em contato com nosso suporte tecnico."
            ),
            new PatternRule(
                Pattern.compile(".*(obrigad|agradec).*", Pattern.CASE_INSENSITIVE),
                "De nada! Estou aqui para ajudar sempre que precisar."
            )
        );
    }

    @Override
    public ChatResponse respond(String userMessage, List<Message> history) {
        for (PatternRule rule : rules) {
            if (rule.pattern().matcher(userMessage).find()) {
                return new ChatResponse(rule.response(), 0.90, "Pattern-matching");
            }
        }

        return new ChatResponse("Nao consegui processar sua mensagem. Tente ser mais especifico.", 0.40, "Pattern-matching");
    }
}

final class IntentBasedStrategy implements ResponseStrategy {
    private final Map<Intent, List<String>> intentKeywords;
    private final Map<Intent, String> intentResponses;

    enum Intent {
        GREETING, PRICING, PAYMENT, DELIVERY, SUPPORT, FAREWELL, UNKNOWN
    }

    public IntentBasedStrategy() {
        this.intentKeywords = Map.of(
            Intent.GREETING, List.of("ola", "oi", "bom dia", "boa tarde", "boa noite"),
            Intent.PRICING, List.of("preco", "valor", "custo", "quanto custa"),
            Intent.PAYMENT, List.of("pagamento", "pagar", "cartao", "pix", "boleto"),
            Intent.DELIVERY, List.of("entrega", "prazo", "quando chega", "envio"),
            Intent.SUPPORT, List.of("problema", "defeito", "erro", "suporte", "ajuda"),
            Intent.FAREWELL, List.of("tchau", "ate logo", "adeus", "obrigado")
        );

        this.intentResponses = Map.of(
            Intent.GREETING, "Ola! Seja bem-vindo ao nosso atendimento. Como posso ajudar?",
            Intent.PRICING, "Nossos precos sao competitivos! Visite nosso catalogo para ver os valores.",
            Intent.PAYMENT, "Aceitamos diversas formas de pagamento: cartao, PIX, boleto e parcelamento.",
            Intent.DELIVERY, "Entregas em todo o Brasil em 3 a 7 dias uteis. Frete gratis acima de R$ 200.",
            Intent.SUPPORT, "Vou encaminhar voce para nosso suporte especializado. Aguarde um momento.",
            Intent.FAREWELL, "Foi um prazer atender voce! Ate a proxima!"
        );
    }

    @Override
    public ChatResponse respond(String userMessage, List<Message> history) {
        String normalized = userMessage.toLowerCase();
        Map<Intent, Integer> intentScores = new HashMap<>();

        for (Map.Entry<Intent, List<String>> entry : intentKeywords.entrySet()) {
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (normalized.contains(keyword)) {
                    score++;
                }
            }
            if (score > 0) {
                intentScores.put(entry.getKey(), score);
            }
        }

        Intent detectedIntent = intentScores.entrySet().stream()
            .max(Comparator.comparingInt(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse(Intent.UNKNOWN);

        if (detectedIntent == Intent.UNKNOWN) {
            return new ChatResponse("Desculpe, nao compreendi. Pode explicar melhor?", 0.50, "Intent-based");
        }

        double confidence = 0.70 + (intentScores.get(detectedIntent) * 0.1);
        return new ChatResponse(intentResponses.get(detectedIntent), Math.min(confidence, 1.0), "Intent-based");
    }
}

class ChatbotSystem {
    private final ResponseStrategy strategy;
    private final List<Message> conversationHistory;

    public ChatbotSystem(ResponseStrategy strategy) {
        this.strategy = strategy;
        this.conversationHistory = new ArrayList<>();
    }

    public ChatResponse sendMessage(String userMessage) {
        conversationHistory.add(new Message(userMessage, System.currentTimeMillis()));
        ChatResponse response = strategy.respond(userMessage, conversationHistory);
        conversationHistory.add(new Message(response.text(), System.currentTimeMillis()));
        return response;
    }

    public List<Message> getHistory() {
        return List.copyOf(conversationHistory);
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Chatbot");
    }
}
