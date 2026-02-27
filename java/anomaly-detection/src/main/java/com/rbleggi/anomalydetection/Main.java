package com.rbleggi.anomalydetection;

import java.util.*;
import java.util.stream.Collectors;

record Transaction(
    String id,
    String accountId,
    double amount,
    String type,
    long timestamp,
    String location
) {}

record AnomalyResult(String transactionId, boolean isAnomaly, double score, String reason, String method) {}

sealed interface AnomalyDetectionStrategy permits ZScoreStrategy, IQRStrategy, MovingAverageStrategy {
    AnomalyResult detect(Transaction transaction, List<Transaction> historicalData);
}

final class ZScoreStrategy implements AnomalyDetectionStrategy {
    private final double threshold;

    public ZScoreStrategy(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public AnomalyResult detect(Transaction transaction, List<Transaction> historicalData) {
        List<Double> amounts = historicalData.stream()
            .filter(t -> t.accountId().equals(transaction.accountId()))
            .map(Transaction::amount)
            .toList();

        if (amounts.isEmpty()) {
            return new AnomalyResult(transaction.id(), false, 0.0, "No historical data", "Z-Score");
        }

        double mean = amounts.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = amounts.stream()
            .mapToDouble(a -> Math.pow(a - mean, 2))
            .average()
            .orElse(0.0);
        double stdDev = Math.sqrt(variance);

        if (stdDev == 0) {
            return new AnomalyResult(transaction.id(), false, 0.0, "Zero standard deviation", "Z-Score");
        }

        double zScore = Math.abs((transaction.amount() - mean) / stdDev);
        boolean isAnomaly = zScore > threshold;

        String reason = isAnomaly
            ? String.format("Z-Score %.2f exceeds threshold %.2f (Mean: %.2f, StdDev: %.2f)", zScore, threshold, mean, stdDev)
            : "Within normal range";

        return new AnomalyResult(transaction.id(), isAnomaly, zScore, reason, "Z-Score");
    }
}

final class IQRStrategy implements AnomalyDetectionStrategy {
    private final double multiplier;

    public IQRStrategy(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public AnomalyResult detect(Transaction transaction, List<Transaction> historicalData) {
        List<Double> amounts = historicalData.stream()
            .filter(t -> t.accountId().equals(transaction.accountId()))
            .map(Transaction::amount)
            .sorted()
            .toList();

        if (amounts.size() < 4) {
            return new AnomalyResult(transaction.id(), false, 0.0, "Dados insuficientes", "IQR");
        }

        int n = amounts.size();
        double q1 = amounts.get(n / 4);
        double q3 = amounts.get(3 * n / 4);
        double iqr = q3 - q1;

        double lowerBound = q1 - (multiplier * iqr);
        double upperBound = q3 + (multiplier * iqr);

        boolean isAnomaly = transaction.amount() < lowerBound || transaction.amount() > upperBound;
        double score = isAnomaly
            ? Math.max(Math.abs(transaction.amount() - upperBound) / iqr, Math.abs(transaction.amount() - lowerBound) / iqr)
            : 0.0;

        String reason = isAnomaly
            ? String.format("Value %.2f outside interval [%.2f, %.2f]", transaction.amount(), lowerBound, upperBound)
            : "Within IQR interval";

        return new AnomalyResult(transaction.id(), isAnomaly, score, reason, "IQR");
    }
}

final class MovingAverageStrategy implements AnomalyDetectionStrategy {
    private final int windowSize;
    private final double threshold;

    public MovingAverageStrategy(int windowSize, double threshold) {
        this.windowSize = windowSize;
        this.threshold = threshold;
    }

    @Override
    public AnomalyResult detect(Transaction transaction, List<Transaction> historicalData) {
        List<Transaction> recentTransactions = historicalData.stream()
            .filter(t -> t.accountId().equals(transaction.accountId()))
            .filter(t -> t.timestamp() < transaction.timestamp())
            .sorted(Comparator.comparingLong(Transaction::timestamp).reversed())
            .limit(windowSize)
            .toList();

        if (recentTransactions.isEmpty()) {
            return new AnomalyResult(transaction.id(), false, 0.0, "No recent transactions", "Moving-Average");
        }

        double movingAvg = recentTransactions.stream()
            .mapToDouble(Transaction::amount)
            .average()
            .orElse(0.0);

        double deviation = Math.abs(transaction.amount() - movingAvg);
        double deviationPercent = movingAvg > 0 ? (deviation / movingAvg) * 100 : 0.0;

        boolean isAnomaly = deviationPercent > threshold;

        String reason = isAnomaly
            ? String.format("Deviation of %.1f%% from moving average %.2f", deviationPercent, movingAvg)
            : "Within expected variation";

        return new AnomalyResult(transaction.id(), isAnomaly, deviationPercent, reason, "Moving-Average");
    }
}

class AnomalyDetectionSystem {
    private final AnomalyDetectionStrategy strategy;

    public AnomalyDetectionSystem(AnomalyDetectionStrategy strategy) {
        this.strategy = strategy;
    }

    public AnomalyResult detectAnomaly(Transaction transaction, List<Transaction> historicalData) {
        return strategy.detect(transaction, historicalData);
    }

    public List<AnomalyResult> detectBatch(List<Transaction> transactions, List<Transaction> historicalData) {
        return transactions.stream()
            .map(t -> detectAnomaly(t, historicalData))
            .toList();
    }

    public Map<Boolean, Long> getAnomalyDistribution(List<Transaction> transactions, List<Transaction> historicalData) {
        return detectBatch(transactions, historicalData).stream()
            .collect(Collectors.groupingBy(AnomalyResult::isAnomaly, Collectors.counting()));
    }

    public List<Transaction> getAnomalousTransactions(List<Transaction> transactions, List<Transaction> historicalData) {
        List<AnomalyResult> results = detectBatch(transactions, historicalData);
        return transactions.stream()
            .filter(t -> results.stream()
                .anyMatch(r -> r.transactionId().equals(t.id()) && r.isAnomaly()))
            .toList();
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Anomaly Detection");
    }
}
