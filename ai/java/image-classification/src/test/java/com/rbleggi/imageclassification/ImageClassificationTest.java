package com.rbleggi.imageclassification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ImageClassificationTest {
    private List<Image> trainingSet;
    private Image araraImage;
    private Image oncaImage;

    @BeforeEach
    void setUp() {
        trainingSet = List.of(
            new Image("t1", "arara_azul.jpg", new ImageFeatures(0.2, 0.6, 0.8, 0.7, 0.5, 0.8), "Arara"),
            new Image("t2", "arara_vermelha.jpg", new ImageFeatures(0.8, 0.5, 0.3, 0.6, 0.6, 0.7), "Arara"),
            new Image("t3", "onca_pintada.jpg", new ImageFeatures(0.6, 0.5, 0.3, 0.4, 0.8, 0.6), "Onca"),
            new Image("t4", "onca_preta.jpg", new ImageFeatures(0.3, 0.3, 0.2, 0.3, 0.7, 0.4), "Onca"),
            new Image("t5", "capivara.jpg", new ImageFeatures(0.5, 0.4, 0.3, 0.5, 0.4, 0.3), "Capivara")
        );

        araraImage = new Image("test1", "arara_nova.jpg", new ImageFeatures(0.3, 0.7, 0.9, 0.75, 0.6, 0.85), "Arara");
        oncaImage = new Image("test2", "onca_nova.jpg", new ImageFeatures(0.4, 0.4, 0.25, 0.35, 0.75, 0.5), "Onca");
    }

    @Test
    @DisplayName("ThresholdStrategy classifies based on color thresholds")
    void thresholdStrategy_classifiesBasedOnThresholds() {
        ImageClassificationSystem system = new ImageClassificationSystem(new ThresholdStrategy());
        Classification result = system.classify(araraImage, trainingSet);

        assertNotNull(result.predictedCategory());
        assertTrue(result.confidence() >= 0.0 && result.confidence() <= 1.0);
        assertEquals("Threshold-based", result.method());
    }

    @Test
    @DisplayName("KNNStrategy classifies using K nearest neighbors")
    void knnStrategy_usesNearestNeighbors() {
        ImageClassificationSystem system = new ImageClassificationSystem(new KNNStrategy(3));
        Classification result = system.classify(araraImage, trainingSet);

        assertEquals("Arara", result.predictedCategory());
        assertTrue(result.confidence() > 0.0);
        assertTrue(result.method().contains("KNN"));
    }

    @Test
    @DisplayName("KNNStrategy confidence reflects vote proportion")
    void knnStrategy_confidenceReflectsVotes() {
        ImageClassificationSystem system = new ImageClassificationSystem(new KNNStrategy(3));
        Classification result = system.classify(araraImage, trainingSet);

        assertTrue(result.confidence() <= 1.0);
        assertTrue(result.confidence() >= 0.33);
    }

    @Test
    @DisplayName("NeuralNetworkStrategy uses weighted scoring")
    void neuralNetworkStrategy_usesWeightedScoring() {
        ImageClassificationSystem system = new ImageClassificationSystem(new NeuralNetworkStrategy());
        Classification result = system.classify(araraImage, trainingSet);

        assertNotNull(result.predictedCategory());
        assertTrue(result.confidence() > 0.0 && result.confidence() <= 1.0);
        assertEquals("Neural-Network", result.method());
    }

    @Test
    @DisplayName("ImageClassificationSystem processes batch of images")
    void classificationSystem_processesBatch() {
        ImageClassificationSystem system = new ImageClassificationSystem(new KNNStrategy(3));
        List<Image> testSet = List.of(araraImage, oncaImage);
        List<Classification> results = system.classifyBatch(testSet, trainingSet);

        assertEquals(2, results.size());
        assertEquals("Arara", results.get(0).predictedCategory());
    }

    @Test
    @DisplayName("ImageClassificationSystem calculates accuracy")
    void classificationSystem_calculatesAccuracy() {
        ImageClassificationSystem system = new ImageClassificationSystem(new KNNStrategy(3));
        List<Image> testSet = List.of(araraImage, oncaImage);
        double accuracy = system.calculateAccuracy(testSet, trainingSet);

        assertTrue(accuracy >= 0.0 && accuracy <= 1.0);
    }

    @Test
    @DisplayName("Different strategies produce different results")
    void differentStrategies_produceDifferentResults() {
        ImageClassificationSystem threshold = new ImageClassificationSystem(new ThresholdStrategy());
        ImageClassificationSystem knn = new ImageClassificationSystem(new KNNStrategy(3));
        ImageClassificationSystem nn = new ImageClassificationSystem(new NeuralNetworkStrategy());

        Classification r1 = threshold.classify(araraImage, trainingSet);
        Classification r2 = knn.classify(araraImage, trainingSet);
        Classification r3 = nn.classify(araraImage, trainingSet);

        assertNotEquals(r1.method(), r2.method());
        assertNotEquals(r2.method(), r3.method());
    }
}
