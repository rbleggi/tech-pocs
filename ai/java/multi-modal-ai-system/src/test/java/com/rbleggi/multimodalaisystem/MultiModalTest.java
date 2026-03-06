package com.rbleggi.multimodalaisystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MultiModalTest {

    @Test
    @DisplayName("TextProcessingStrategy - processa texto e extrai metadados")
    void textProcessing_extractsMetadata() {
        TextInput input = new TextInput("Ola mundo 123");
        TextProcessingStrategy strategy = new TextProcessingStrategy();
        ProcessingResult result = strategy.process(input);

        assertEquals("text", result.inputType());
        assertEquals("ola mundo 123", result.processedValue());
        assertEquals(3, result.metadata().get("wordCount"));
        assertTrue((Boolean) result.metadata().get("hasNumbers"));
    }

    @Test
    @DisplayName("NumericProcessingStrategy - processa numeros e calcula propriedades")
    void numericProcessing_calculatesProperties() {
        NumericInput input = new NumericInput(16.0);
        NumericProcessingStrategy strategy = new NumericProcessingStrategy();
        ProcessingResult result = strategy.process(input);

        assertEquals("numeric", result.inputType());
        assertEquals(16.0, result.processedValue());
        assertTrue((Boolean) result.metadata().get("isPositive"));
        assertEquals(256.0, result.metadata().get("squared"));
        assertEquals(4.0, result.metadata().get("squareRoot"));
    }

    @Test
    @DisplayName("CategoricalProcessingStrategy - valida categoria e cria encoding")
    void categoricalProcessing_createsOneHotEncoding() {
        List<String> categories = List.of("A", "B", "C");
        CategoricalInput input = new CategoricalInput("B", categories);
        CategoricalProcessingStrategy strategy = new CategoricalProcessingStrategy();
        ProcessingResult result = strategy.process(input);

        assertEquals("categorical", result.inputType());
        assertTrue((Boolean) result.metadata().get("isValid"));
        assertEquals(1, result.metadata().get("categoryIndex"));
        assertEquals(List.of(0, 1, 0), result.metadata().get("oneHotEncoding"));
    }

    @Test
    @DisplayName("MultiModalAISystem - processa diferentes tipos de input")
    void multiModalSystem_processesDifferentInputTypes() {
        MultiModalAISystem system = new MultiModalAISystem();

        TextInput textInput = new TextInput("teste");
        ProcessingResult textResult = system.process(textInput);
        assertEquals("text", textResult.inputType());

        NumericInput numericInput = new NumericInput(10.0);
        ProcessingResult numericResult = system.process(numericInput);
        assertEquals("numeric", numericResult.inputType());
    }

    @Test
    @DisplayName("MultiModalAISystem - processa lote de inputs")
    void multiModalSystem_processesBatch() {
        MultiModalAISystem system = new MultiModalAISystem();
        List<InputData> inputs = List.of(
            new TextInput("teste"),
            new NumericInput(5.0),
            new CategoricalInput("X", List.of("X", "Y"))
        );

        List<ProcessingResult> results = system.processBatch(inputs);

        assertEquals(3, results.size());
        assertEquals("text", results.get(0).inputType());
        assertEquals("numeric", results.get(1).inputType());
        assertEquals("categorical", results.get(2).inputType());
    }

    @Test
    @DisplayName("MultiModalAISystem - calcula distribuicao de tipos")
    void multiModalSystem_calculatesDistribution() {
        MultiModalAISystem system = new MultiModalAISystem();
        List<InputData> inputs = List.of(
            new TextInput("a"),
            new TextInput("b"),
            new NumericInput(1.0)
        );

        Map<String, Long> distribution = system.getInputTypeDistribution(inputs);

        assertEquals(2, distribution.get("TextInput"));
        assertEquals(1, distribution.get("NumericInput"));
    }
}
