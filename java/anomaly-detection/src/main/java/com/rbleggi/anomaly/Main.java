package com.rbleggi.anomaly;

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
            return new AnomalyResult(transaction.id(), false, 0.0, "Sem dados historicos", "Z-Score");
        }

        double mean = amounts.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = amounts.stream()
            .mapToDouble(a -> Math.pow(a - mean, 2))
            .average()
            .orElse(0.0);
        double stdDev = Math.sqrt(variance);

        if (stdDev == 0) {
            return new AnomalyResult(transaction.id(), false, 0.0, "Desvio padrao zero", "Z-Score");
        }

        double zScore = Math.abs((transaction.amount() - mean) / stdDev);
        boolean isAnomaly = zScore > threshold;

        String reason = isAnomaly
            ? String.format("Z-Score %.2f excede limite %.2f (Media: R$ %.2f, DP: R$ %.2f)", zScore, threshold, mean, stdDev)
            : "Dentro da normalidade";

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
            ? String.format("Valor R$ %.2f fora do intervalo [R$ %.2f, R$ %.2f]", transaction.amount(), lowerBound, upperBound)
            : "Dentro do intervalo IQR";

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
            return new AnomalyResult(transaction.id(), false, 0.0, "Sem transacoes recentes", "Moving-Average");
        }

        double movingAvg = recentTransactions.stream()
            .mapToDouble(Transaction::amount)
            .average()
            .orElse(0.0);

        double deviation = Math.abs(transaction.amount() - movingAvg);
        double deviationPercent = movingAvg > 0 ? (deviation / movingAvg) * 100 : 0.0;

        boolean isAnomaly = deviationPercent > threshold;

        String reason = isAnomaly
            ? String.format("Desvio de %.1f%% da media movel R$ %.2f", deviationPercent, movingAvg)
            : "Dentro da variacao esperada";

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
        List<Transaction> historicalData = List.of(
            new Transaction("t1", "acc1", 150.00, "compra", 1000, "Sao Paulo"),
            new Transaction("t2", "acc1", 200.00, "compra", 2000, "Sao Paulo"),
            new Transaction("t3", "acc1", 180.00, "compra", 3000, "Curitiba"),
            new Transaction("t4", "acc1", 160.00, "compra", 4000, "Sao Paulo"),
            new Transaction("t5", "acc1", 190.00, "compra", 5000, "Belo Horizonte"),
            new Transaction("t6", "acc1", 175.00, "compra", 6000, "Sao Paulo")
        );

        Transaction normalTransaction = new Transaction("t7", "acc1", 185.00, "compra", 7000, "Sao Paulo");
        Transaction anomalousTransaction = new Transaction("t8", "acc1", 5000.00, "compra", 8000, "Miami");

        System.out.println("=== Sistema de Deteccao de Anomalias ===\n");

        System.out.println("Transacao Normal: R$ 185,00");
        System.out.println("Estrategia: Z-Score (threshold=3)");
        AnomalyDetectionSystem zScoreSystem = new AnomalyDetectionSystem(new ZScoreStrategy(3.0));
        AnomalyResult zScoreNormal = zScoreSystem.detectAnomaly(normalTransaction, historicalData);
        System.out.printf("  Anomalia: %s%n", zScoreNormal.isAnomaly() ? "SIM" : "NAO");
        System.out.printf("  Score: %.2f%n", zScoreNormal.score());
        System.out.printf("  Razao: %s%n", zScoreNormal.reason());

        System.out.println("\nTransacao Suspeita: R$ 5.000,00");
        AnomalyResult zScoreAnomaly = zScoreSystem.detectAnomaly(anomalousTransaction, historicalData);
        System.out.printf("  Anomalia: %s%n", zScoreAnomaly.isAnomaly() ? "SIM" : "NAO");
        System.out.printf("  Score: %.2f%n", zScoreAnomaly.score());
        System.out.printf("  Razao: %s%n", zScoreAnomaly.reason());

        System.out.println("\nEstrategia: IQR (multiplier=1.5)");
        AnomalyDetectionSystem iqrSystem = new AnomalyDetectionSystem(new IQRStrategy(1.5));
        AnomalyResult iqrResult = iqrSystem.detectAnomaly(anomalousTransaction, historicalData);
        System.out.printf("  Anomalia: %s%n", iqrResult.isAnomaly() ? "SIM" : "NAO");
        System.out.printf("  Score: %.2f%n", iqrResult.score());
        System.out.printf("  Razao: %s%n", iqrResult.reason());

        System.out.println("\nEstrategia: Moving Average (window=5, threshold=50%)");
        AnomalyDetectionSystem maSystem = new AnomalyDetectionSystem(new MovingAverageStrategy(5, 50.0));
        AnomalyResult maResult = maSystem.detectAnomaly(anomalousTransaction, historicalData);
        System.out.printf("  Anomalia: %s%n", maResult.isAnomaly() ? "SIM" : "NAO");
        System.out.printf("  Score: %.2f%n", maResult.score());
        System.out.printf("  Razao: %s%n", maResult.reason());

        List<Transaction> testSet = List.of(normalTransaction, anomalousTransaction);
        System.out.println("\nDistribuicao de Anomalias (Z-Score):");
        Map<Boolean, Long> distribution = zScoreSystem.getAnomalyDistribution(testSet, historicalData);
        System.out.printf("  Normais: %d%n", distribution.getOrDefault(false, 0L));
        System.out.printf("  Anomalias: %d%n", distribution.getOrDefault(true, 0L));
    }
}
