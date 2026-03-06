package com.rbleggi.multimodalaisystem;

import java.util.*;

sealed interface InputData {}
record TextInput(String text) implements InputData {}
record NumericInput(double value) implements InputData {}
record CategoricalInput(String category, List<String> validCategories) implements InputData {}

record ProcessingResult(String inputType, Object processedValue, Map<String, Object> metadata) {}

sealed interface ProcessingStrategy permits TextProcessingStrategy, NumericProcessingStrategy, CategoricalProcessingStrategy {
    ProcessingResult process(InputData input);
}

final class TextProcessingStrategy implements ProcessingStrategy {
    @Override
    public ProcessingResult process(InputData input) {
        if (input instanceof TextInput textInput) {
            String text = textInput.text();
            int wordCount = text.split("\\s+").length;
            int charCount = text.length();
            boolean hasNumbers = text.matches(".*\\d.*");

            Map<String, Object> metadata = Map.of(
                "wordCount", wordCount,
                "charCount", charCount,
                "hasNumbers", hasNumbers,
                "uppercase", text.toUpperCase()
            );

            return new ProcessingResult("text", text.toLowerCase(), metadata);
        }
        throw new IllegalArgumentException("Invalid input for text processing");
    }
}

final class NumericProcessingStrategy implements ProcessingStrategy {
    @Override
    public ProcessingResult process(InputData input) {
        if (input instanceof NumericInput numericInput) {
            double value = numericInput.value();
            boolean isPositive = value > 0;
            boolean isInteger = value == Math.floor(value);
            double squared = value * value;
            double sqrt = Math.sqrt(Math.abs(value));

            Map<String, Object> metadata = Map.of(
                "isPositive", isPositive,
                "isInteger", isInteger,
                "squared", squared,
                "squareRoot", sqrt
            );

            return new ProcessingResult("numeric", value, metadata);
        }
        throw new IllegalArgumentException("Invalid input for numeric processing");
    }
}

final class CategoricalProcessingStrategy implements ProcessingStrategy {
    @Override
    public ProcessingResult process(InputData input) {
        if (input instanceof CategoricalInput catInput) {
            String category = catInput.category();
            List<String> validCategories = catInput.validCategories();
            boolean isValid = validCategories.contains(category);
            int categoryIndex = validCategories.indexOf(category);

            Map<String, Object> metadata = Map.of(
                "isValid", isValid,
                "categoryIndex", categoryIndex,
                "totalCategories", validCategories.size(),
                "oneHotEncoding", createOneHotEncoding(category, validCategories)
            );

            return new ProcessingResult("categorical", category, metadata);
        }
        throw new IllegalArgumentException("Invalid input for categorical processing");
    }

    private List<Integer> createOneHotEncoding(String category, List<String> validCategories) {
        List<Integer> encoding = new ArrayList<>();
        for (String validCategory : validCategories) {
            encoding.add(validCategory.equals(category) ? 1 : 0);
        }
        return encoding;
    }
}

class MultiModalAISystem {
    private final Map<Class<?>, ProcessingStrategy> strategies;

    public MultiModalAISystem() {
        this.strategies = new HashMap<>();
        strategies.put(TextInput.class, new TextProcessingStrategy());
        strategies.put(NumericInput.class, new NumericProcessingStrategy());
        strategies.put(CategoricalInput.class, new CategoricalProcessingStrategy());
    }

    public ProcessingResult process(InputData input) {
        ProcessingStrategy strategy = strategies.get(input.getClass());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported input type: " + input.getClass());
        }
        return strategy.process(input);
    }

    public List<ProcessingResult> processBatch(List<InputData> inputs) {
        return inputs.stream()
            .map(this::process)
            .toList();
    }

    public Map<String, Long> getInputTypeDistribution(List<InputData> inputs) {
        return inputs.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                input -> input.getClass().getSimpleName(),
                java.util.stream.Collectors.counting()
            ));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Multi-Modal AI System");
    }
}
