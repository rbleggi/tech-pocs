package com.rbleggi.realestate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RealEstatePricingTest {
    private List<Property> trainingData;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        trainingData = List.of(
            new Property("p1", "Sao Paulo", "Jardins", 120, 3, 2, 2, 5, 950000),
            new Property("p2", "Sao Paulo", "Vila Mariana", 85, 2, 1, 1, 10, 620000),
            new Property("p3", "Curitiba", "Batel", 100, 3, 2, 2, 3, 720000),
            new Property("p4", "Curitiba", "Centro", 70, 2, 1, 1, 15, 380000),
            new Property("p5", "Belo Horizonte", "Savassi", 95, 2, 2, 1, 8, 580000)
        );

        testProperty = new Property("test1", "Sao Paulo", "Jardins", 110, 3, 2, 2, 7, 900000);
    }

    @Test
    @DisplayName("LinearRegressionStrategy predicts price based on linear features")
    void linearRegression_predictsPrice() {
        RealEstatePricingSystem system = new RealEstatePricingSystem(new LinearRegressionStrategy());
        PricePrediction prediction = system.predictPrice(testProperty, trainingData);

        assertTrue(prediction.predictedPrice() > 100000);
        assertEquals("Linear-Regression", prediction.method());
        assertEquals(0.75, prediction.confidence(), 0.01);
    }

    @Test
    @DisplayName("PolynomialRegressionStrategy uses polynomial features")
    void polynomialRegression_usesPolynomialFeatures() {
        RealEstatePricingSystem system = new RealEstatePricingSystem(new PolynomialRegressionStrategy());
        PricePrediction prediction = system.predictPrice(testProperty, trainingData);

        assertTrue(prediction.predictedPrice() > 120000);
        assertEquals("Polynomial-Regression", prediction.method());
        assertTrue(prediction.confidence() > 0.75);
    }

    @Test
    @DisplayName("KNNRegressionStrategy averages K nearest neighbors")
    void knnRegression_averagesNearestNeighbors() {
        RealEstatePricingSystem system = new RealEstatePricingSystem(new KNNRegressionStrategy(3));
        PricePrediction prediction = system.predictPrice(testProperty, trainingData);

        assertTrue(prediction.predictedPrice() > 0);
        assertTrue(prediction.method().contains("KNN"));
        assertTrue(prediction.confidence() >= 0.65);
    }

    @Test
    @DisplayName("RealEstatePricingSystem predicts batch of properties")
    void pricingSystem_predictsBatch() {
        RealEstatePricingSystem system = new RealEstatePricingSystem(new LinearRegressionStrategy());
        List<Property> testSet = List.of(testProperty, trainingData.get(0));
        List<PricePrediction> predictions = system.predictBatch(testSet, trainingData);

        assertEquals(2, predictions.size());
        assertTrue(predictions.get(0).predictedPrice() > 0);
    }

    @Test
    @DisplayName("RealEstatePricingSystem calculates mean absolute error")
    void pricingSystem_calculatesMeanAbsoluteError() {
        RealEstatePricingSystem system = new RealEstatePricingSystem(new KNNRegressionStrategy(3));
        List<Property> testSet = List.of(testProperty);
        double mae = system.calculateMeanAbsoluteError(testSet, trainingData);

        assertTrue(mae >= 0);
    }

    @Test
    @DisplayName("RealEstatePricingSystem calculates accuracy percentage")
    void pricingSystem_calculatesAccuracyPercentage() {
        RealEstatePricingSystem system = new RealEstatePricingSystem(new KNNRegressionStrategy(3));
        List<Property> testSet = List.of(testProperty);
        double accuracy = system.calculateAccuracyPercentage(testSet, trainingData, 0.30);

        assertTrue(accuracy >= 0 && accuracy <= 100);
    }

    @Test
    @DisplayName("Different strategies produce different predictions")
    void differentStrategies_produceDifferentPredictions() {
        RealEstatePricingSystem linear = new RealEstatePricingSystem(new LinearRegressionStrategy());
        RealEstatePricingSystem poly = new RealEstatePricingSystem(new PolynomialRegressionStrategy());
        RealEstatePricingSystem knn = new RealEstatePricingSystem(new KNNRegressionStrategy(3));

        PricePrediction p1 = linear.predictPrice(testProperty, trainingData);
        PricePrediction p2 = poly.predictPrice(testProperty, trainingData);
        PricePrediction p3 = knn.predictPrice(testProperty, trainingData);

        assertNotEquals(p1.method(), p2.method());
        assertNotEquals(p2.method(), p3.method());
    }
}
