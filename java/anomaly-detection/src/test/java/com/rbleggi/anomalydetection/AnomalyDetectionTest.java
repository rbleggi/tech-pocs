package com.rbleggi.anomalydetection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyDetectionTest {
    private List<Transaction> historicalData;
    private Transaction normalTransaction;
    private Transaction anomalousTransaction;

    @BeforeEach
    void setUp() {
        historicalData = List.of(
            new Transaction("t1", "acc1", 150.00, "compra", 1000, "Sao Paulo"),
            new Transaction("t2", "acc1", 200.00, "compra", 2000, "Sao Paulo"),
            new Transaction("t3", "acc1", 180.00, "compra", 3000, "Curitiba"),
            new Transaction("t4", "acc1", 160.00, "compra", 4000, "Sao Paulo"),
            new Transaction("t5", "acc1", 190.00, "compra", 5000, "Belo Horizonte")
        );

        normalTransaction = new Transaction("t6", "acc1", 175.00, "compra", 6000, "Sao Paulo");
        anomalousTransaction = new Transaction("t7", "acc1", 5000.00, "compra", 7000, "Miami");
    }

    @Test
    @DisplayName("ZScoreStrategy detects anomalies based on standard deviation")
    void zScoreStrategy_detectsAnomalies() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new ZScoreStrategy(3.0));
        AnomalyResult result = system.detectAnomaly(anomalousTransaction, historicalData);

        assertTrue(result.isAnomaly());
        assertTrue(result.score() > 3.0);
        assertEquals("Z-Score", result.method());
    }

    @Test
    @DisplayName("ZScoreStrategy does not flag normal transactions")
    void zScoreStrategy_doesNotFlagNormal() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new ZScoreStrategy(3.0));
        AnomalyResult result = system.detectAnomaly(normalTransaction, historicalData);

        assertFalse(result.isAnomaly());
        assertTrue(result.score() < 3.0);
    }

    @Test
    @DisplayName("IQRStrategy detects outliers using interquartile range")
    void iqrStrategy_detectsOutliers() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new IQRStrategy(1.5));
        AnomalyResult result = system.detectAnomaly(anomalousTransaction, historicalData);

        assertTrue(result.isAnomaly());
        assertTrue(result.score() > 0);
        assertEquals("IQR", result.method());
    }

    @Test
    @DisplayName("IQRStrategy handles insufficient data")
    void iqrStrategy_handlesInsufficientData() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new IQRStrategy(1.5));
        List<Transaction> smallData = List.of(historicalData.get(0), historicalData.get(1));
        AnomalyResult result = system.detectAnomaly(normalTransaction, smallData);

        assertFalse(result.isAnomaly());
        assertTrue(result.reason().contains("insuficientes"));
    }

    @Test
    @DisplayName("MovingAverageStrategy detects deviations from recent average")
    void movingAverageStrategy_detectsDeviations() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new MovingAverageStrategy(3, 50.0));
        AnomalyResult result = system.detectAnomaly(anomalousTransaction, historicalData);

        assertTrue(result.isAnomaly());
        assertTrue(result.score() > 50.0);
        assertEquals("Moving-Average", result.method());
    }

    @Test
    @DisplayName("MovingAverageStrategy uses correct window size")
    void movingAverageStrategy_usesWindowSize() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new MovingAverageStrategy(2, 100.0));
        AnomalyResult result = system.detectAnomaly(normalTransaction, historicalData);

        assertNotNull(result);
        assertEquals("Moving-Average", result.method());
    }

    @Test
    @DisplayName("AnomalyDetectionSystem processes batch of transactions")
    void detectionSystem_processesBatch() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new ZScoreStrategy(3.0));
        List<Transaction> testSet = List.of(normalTransaction, anomalousTransaction);
        List<AnomalyResult> results = system.detectBatch(testSet, historicalData);

        assertEquals(2, results.size());
        assertFalse(results.get(0).isAnomaly());
        assertTrue(results.get(1).isAnomaly());
    }

    @Test
    @DisplayName("AnomalyDetectionSystem calculates distribution")
    void detectionSystem_calculatesDistribution() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new ZScoreStrategy(3.0));
        List<Transaction> testSet = List.of(normalTransaction, anomalousTransaction);
        Map<Boolean, Long> distribution = system.getAnomalyDistribution(testSet, historicalData);

        assertEquals(1L, distribution.get(false));
        assertEquals(1L, distribution.get(true));
    }

    @Test
    @DisplayName("AnomalyDetectionSystem filters anomalous transactions")
    void detectionSystem_filtersAnomalousTransactions() {
        AnomalyDetectionSystem system = new AnomalyDetectionSystem(new ZScoreStrategy(3.0));
        List<Transaction> testSet = List.of(normalTransaction, anomalousTransaction);
        List<Transaction> anomalies = system.getAnomalousTransactions(testSet, historicalData);

        assertEquals(1, anomalies.size());
        assertEquals(anomalousTransaction.id(), anomalies.get(0).id());
    }
}
