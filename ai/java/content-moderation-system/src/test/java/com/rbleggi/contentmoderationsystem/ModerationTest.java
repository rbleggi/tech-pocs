package com.rbleggi.contentmoderationsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ModerationTest {

    @Test
    @DisplayName("KeywordFilter - detecta palavras proibidas")
    void keywordFilter_detectsBannedWords() {
        Set<String> banned = Set.of("spam", "golpe");
        KeywordFilter filter = new KeywordFilter(banned, 0.5);
        Content content = new Content("c1", "Isso e um spam!", "user", "msg");

        ModerationResult result = filter.moderate(content);

        assertTrue(result.flagged());
        assertTrue(result.reason().contains("spam"));
        assertEquals(0.5, result.severity());
    }

    @Test
    @DisplayName("KeywordFilter - aprova conteudo limpo")
    void keywordFilter_approvesCleanContent() {
        Set<String> banned = Set.of("spam");
        KeywordFilter filter = new KeywordFilter(banned, 0.5);
        Content content = new Content("c1", "Conteudo normal", "user", "msg");

        ModerationResult result = filter.moderate(content);

        assertFalse(result.flagged());
        assertEquals(0.0, result.severity());
    }

    @Test
    @DisplayName("RegexFilter - detecta padroes suspeitos")
    void regexFilter_detectsPatterns() {
        Map<String, Pattern> patterns = Map.of(
            "email", Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        );
        RegexFilter filter = new RegexFilter(patterns);
        Content content = new Content("c1", "Contact me at test@example.com", "user", "msg");

        ModerationResult result = filter.moderate(content);

        assertTrue(result.flagged());
        assertTrue(result.reason().contains("email"));
    }

    @Test
    @DisplayName("LengthFilter - rejeita conteudo muito curto")
    void lengthFilter_rejectsTooShort() {
        LengthFilter filter = new LengthFilter(10, 100);
        Content content = new Content("c1", "Hi", "user", "msg");

        ModerationResult result = filter.moderate(content);

        assertTrue(result.flagged());
        assertTrue(result.reason().contains("muito curto"));
    }

    @Test
    @DisplayName("LengthFilter - rejeita conteudo muito longo")
    void lengthFilter_rejectsTooLong() {
        LengthFilter filter = new LengthFilter(10, 20);
        Content content = new Content("c1", "A".repeat(50), "user", "msg");

        ModerationResult result = filter.moderate(content);

        assertTrue(result.flagged());
        assertTrue(result.reason().contains("muito longo"));
    }

    @Test
    @DisplayName("ContentModerationSystem - modera com multiplos filtros")
    void moderationSystem_appliesMultipleFilters() {
        List<ModerationFilter> filters = List.of(
            new KeywordFilter(Set.of("spam"), 0.5),
            new LengthFilter(5, 100)
        );
        ContentModerationSystem system = new ContentModerationSystem(filters);
        Content content = new Content("c1", "Mensagem normal", "user", "msg");

        List<ModerationResult> results = system.moderateContent(content);

        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("ContentModerationSystem - aprova conteudo valido")
    void moderationSystem_approvesValidContent() {
        List<ModerationFilter> filters = List.of(
            new KeywordFilter(Set.of("spam"), 0.5),
            new LengthFilter(5, 100)
        );
        ContentModerationSystem system = new ContentModerationSystem(filters);
        Content content = new Content("c1", "Mensagem valida", "user", "msg");

        assertTrue(system.isContentApproved(content));
    }

    @Test
    @DisplayName("ContentModerationSystem - calcula estatisticas")
    void moderationSystem_calculatesStats() {
        List<ModerationFilter> filters = List.of(
            new KeywordFilter(Set.of("spam"), 0.5),
            new LengthFilter(10, 100)
        );
        ContentModerationSystem system = new ContentModerationSystem(filters);
        List<Content> contents = List.of(
            new Content("c1", "Mensagem valida aqui", "user", "msg"),
            new Content("c2", "spam", "user", "msg")
        );

        Map<Boolean, Long> stats = system.getModerationStats(contents);

        assertTrue(stats.containsKey(true));
        assertTrue(stats.containsKey(false));
    }
}
