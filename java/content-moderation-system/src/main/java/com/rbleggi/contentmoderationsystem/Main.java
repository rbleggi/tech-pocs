package com.rbleggi.contentmoderationsystem;

import java.util.*;
import java.util.regex.Pattern;

record Content(String id, String text, String author, String type) {}

record ModerationResult(String contentId, boolean flagged, String reason, double severity, String filterType) {}

sealed interface ModerationFilter permits KeywordFilter, RegexFilter, LengthFilter {
    ModerationResult moderate(Content content);
}

final class KeywordFilter implements ModerationFilter {
    private final Set<String> bannedWords;
    private final double severityPerWord;

    public KeywordFilter(Set<String> bannedWords, double severityPerWord) {
        this.bannedWords = bannedWords;
        this.severityPerWord = severityPerWord;
    }

    @Override
    public ModerationResult moderate(Content content) {
        String lowerText = content.text().toLowerCase();
        List<String> foundWords = bannedWords.stream()
            .filter(lowerText::contains)
            .toList();

        if (foundWords.isEmpty()) {
            return new ModerationResult(content.id(), false, "Content approved", 0.0, "Keyword");
        }

        double severity = foundWords.size() * severityPerWord;
        String reason = "Banned words found: " + String.join(", ", foundWords);
        return new ModerationResult(content.id(), true, reason, severity, "Keyword");
    }
}

final class RegexFilter implements ModerationFilter {
    private final Map<String, Pattern> patterns;

    public RegexFilter(Map<String, Pattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public ModerationResult moderate(Content content) {
        String text = content.text();
        List<String> matches = new ArrayList<>();

        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            if (entry.getValue().matcher(text).find()) {
                matches.add(entry.getKey());
            }
        }

        if (matches.isEmpty()) {
            return new ModerationResult(content.id(), false, "No suspicious patterns", 0.0, "Regex");
        }

        double severity = Math.min(1.0, matches.size() * 0.3);
        String reason = "Patterns detected: " + String.join(", ", matches);
        return new ModerationResult(content.id(), true, reason, severity, "Regex");
    }
}

final class LengthFilter implements ModerationFilter {
    private final int minLength;
    private final int maxLength;

    public LengthFilter(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public ModerationResult moderate(Content content) {
        int length = content.text().length();

        if (length < minLength) {
            return new ModerationResult(
                content.id(),
                true,
                "Conteudo muito curto (" + length + " chars, minimo " + minLength + ")",
                0.3,
                "Length"
            );
        }

        if (length > maxLength) {
            return new ModerationResult(
                content.id(),
                true,
                "Conteudo muito longo (" + length + " chars, maximo " + maxLength + ")",
                0.5,
                "Length"
            );
        }

        return new ModerationResult(content.id(), false, "Adequate length", 0.0, "Length");
    }
}

class ContentModerationSystem {
    private final List<ModerationFilter> filters;

    public ContentModerationSystem(List<ModerationFilter> filters) {
        this.filters = filters;
    }

    public List<ModerationResult> moderateContent(Content content) {
        return filters.stream()
            .map(filter -> filter.moderate(content))
            .toList();
    }

    public boolean isContentApproved(Content content) {
        return moderateContent(content).stream()
            .noneMatch(ModerationResult::flagged);
    }

    public List<Content> filterApprovedContent(List<Content> contents) {
        return contents.stream()
            .filter(this::isContentApproved)
            .toList();
    }

    public Map<Boolean, Long> getModerationStats(List<Content> contents) {
        return contents.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                this::isContentApproved,
                java.util.stream.Collectors.counting()
            ));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Content Moderation System");
    }
}
