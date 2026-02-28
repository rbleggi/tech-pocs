package com.rbleggi.creditriskassessment;

import java.util.*;
import java.util.stream.Collectors;

record Applicant(
    int creditScore,
    double annualIncome,
    double totalDebt,
    int employmentYears,
    int latePayments,
    int existingLoans,
    int age
) {}

enum RiskLevel {
    LOW, MEDIUM, HIGH, VERY_HIGH
}

record RiskAssessment(
    RiskLevel riskLevel,
    double score,
    double confidence,
    Map<String, Double> factors
) {}

sealed interface RiskStrategy permits CreditScoreStrategy, DebtToIncomeStrategy, CompositeRiskStrategy, EnsembleRiskStrategy {
    RiskAssessment assess(Applicant applicant);
}

final class CreditScoreStrategy implements RiskStrategy {
    @Override
    public RiskAssessment assess(Applicant applicant) {
        double creditScoreNormalized = applicant.creditScore() / 850.0;
        double latePaymentPenalty = applicant.latePayments() * 0.05;
        double loansPenalty = Math.min(applicant.existingLoans() * 0.03, 0.15);

        double score = Math.max(0.0, Math.min(1.0,
            creditScoreNormalized - latePaymentPenalty - loansPenalty
        ));

        RiskLevel riskLevel = switch (Double.compare(score, 0.80)) {
            case 0, 1 -> RiskLevel.LOW;
            default -> switch (Double.compare(score, 0.60)) {
                case 0, 1 -> RiskLevel.MEDIUM;
                default -> switch (Double.compare(score, 0.40)) {
                    case 0, 1 -> RiskLevel.HIGH;
                    default -> RiskLevel.VERY_HIGH;
                };
            };
        };

        double confidence = 0.85;

        Map<String, Double> factors = Map.of(
            "credit_score", creditScoreNormalized,
            "late_payments", (double) applicant.latePayments(),
            "existing_loans", (double) applicant.existingLoans(),
            "score", score
        );

        return new RiskAssessment(riskLevel, score, confidence, factors);
    }
}

final class DebtToIncomeStrategy implements RiskStrategy {
    @Override
    public RiskAssessment assess(Applicant applicant) {
        double dti = applicant.totalDebt() / applicant.annualIncome();
        double employmentBonus = Math.min(applicant.employmentYears() * 0.02, 0.10);

        double baseScore;
        if (dti <= 0.20) {
            baseScore = 0.95;
        } else if (dti <= 0.35) {
            baseScore = 0.75;
        } else if (dti <= 0.50) {
            baseScore = 0.50;
        } else if (dti <= 0.70) {
            baseScore = 0.30;
        } else {
            baseScore = 0.10;
        }

        double score = Math.min(1.0, baseScore + employmentBonus);

        RiskLevel riskLevel = switch (Double.compare(score, 0.75)) {
            case 0, 1 -> RiskLevel.LOW;
            default -> switch (Double.compare(score, 0.50)) {
                case 0, 1 -> RiskLevel.MEDIUM;
                default -> switch (Double.compare(score, 0.30)) {
                    case 0, 1 -> RiskLevel.HIGH;
                    default -> RiskLevel.VERY_HIGH;
                };
            };
        };

        double confidence = 0.78;

        Map<String, Double> factors = Map.of(
            "debt_to_income", dti,
            "annual_income", applicant.annualIncome(),
            "total_debt", applicant.totalDebt(),
            "employment_years", (double) applicant.employmentYears(),
            "score", score
        );

        return new RiskAssessment(riskLevel, score, confidence, factors);
    }
}

final class CompositeRiskStrategy implements RiskStrategy {
    @Override
    public RiskAssessment assess(Applicant applicant) {
        double creditScoreWeight = 0.40;
        double incomeWeight = 0.25;
        double debtWeight = 0.20;
        double historyWeight = 0.15;

        double creditComponent = (applicant.creditScore() / 850.0) * creditScoreWeight;
        double incomeComponent = Math.min(applicant.annualIncome() / 200000.0, 1.0) * incomeWeight;
        double debtComponent = (1.0 - Math.min(applicant.totalDebt() / applicant.annualIncome(), 1.0)) * debtWeight;
        double historyComponent = (Math.min(applicant.employmentYears() / 10.0, 1.0) -
                                   Math.min(applicant.latePayments() * 0.1, 0.5)) * historyWeight;

        double score = Math.max(0.0, Math.min(1.0,
            creditComponent + incomeComponent + debtComponent + historyComponent
        ));

        RiskLevel riskLevel = switch (Double.compare(score, 0.70)) {
            case 0, 1 -> RiskLevel.LOW;
            default -> switch (Double.compare(score, 0.50)) {
                case 0, 1 -> RiskLevel.MEDIUM;
                default -> switch (Double.compare(score, 0.30)) {
                    case 0, 1 -> RiskLevel.HIGH;
                    default -> RiskLevel.VERY_HIGH;
                };
            };
        };

        double confidence = 0.90;

        Map<String, Double> factors = Map.of(
            "credit_component", creditComponent,
            "income_component", incomeComponent,
            "debt_component", debtComponent,
            "history_component", historyComponent,
            "score", score
        );

        return new RiskAssessment(riskLevel, score, confidence, factors);
    }
}

final class EnsembleRiskStrategy implements RiskStrategy {
    private final List<RiskStrategy> strategies;

    public EnsembleRiskStrategy(List<RiskStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public RiskAssessment assess(Applicant applicant) {
        List<RiskAssessment> assessments = strategies.stream()
            .map(strategy -> strategy.assess(applicant))
            .toList();

        double avgScore = assessments.stream()
            .mapToDouble(RiskAssessment::score)
            .average()
            .orElse(0.0);

        double avgConfidence = assessments.stream()
            .mapToDouble(RiskAssessment::confidence)
            .average()
            .orElse(0.0);

        RiskLevel riskLevel = switch (Double.compare(avgScore, 0.70)) {
            case 0, 1 -> RiskLevel.LOW;
            default -> switch (Double.compare(avgScore, 0.50)) {
                case 0, 1 -> RiskLevel.MEDIUM;
                default -> switch (Double.compare(avgScore, 0.30)) {
                    case 0, 1 -> RiskLevel.HIGH;
                    default -> RiskLevel.VERY_HIGH;
                };
            };
        };

        Map<String, Double> factors = Map.of(
            "ensemble_size", (double) strategies.size(),
            "avg_score", avgScore,
            "score_variance", calculateVariance(assessments.stream()
                .mapToDouble(RiskAssessment::score)
                .boxed()
                .toList()),
            "low_count", (double) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.LOW).count(),
            "medium_count", (double) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.MEDIUM).count(),
            "high_count", (double) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.HIGH).count()
        );

        return new RiskAssessment(riskLevel, avgScore, avgConfidence, factors);
    }

    private double calculateVariance(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return values.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
    }
}

class CreditRiskSystem {
    private final RiskStrategy strategy;

    public CreditRiskSystem(RiskStrategy strategy) {
        this.strategy = strategy;
    }

    public RiskAssessment assess(Applicant applicant) {
        return strategy.assess(applicant);
    }

    public List<RiskAssessment> assessBatch(List<Applicant> applicants) {
        return applicants.stream()
            .map(this::assess)
            .toList();
    }

    public record ApprovalRecommendation(boolean approved, RiskAssessment assessment) {}

    public ApprovalRecommendation approvalRecommendation(Applicant applicant, double threshold) {
        RiskAssessment assessment = assess(applicant);
        boolean approved = assessment.score() >= threshold;
        return new ApprovalRecommendation(approved, assessment);
    }

    public Map<RiskLevel, Integer> riskDistribution(List<Applicant> applicants) {
        List<RiskAssessment> assessments = assessBatch(applicants);
        Map<RiskLevel, Integer> distribution = new EnumMap<>(RiskLevel.class);

        distribution.put(RiskLevel.LOW, (int) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.LOW).count());
        distribution.put(RiskLevel.MEDIUM, (int) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.MEDIUM).count());
        distribution.put(RiskLevel.HIGH, (int) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.HIGH).count());
        distribution.put(RiskLevel.VERY_HIGH, (int) assessments.stream().filter(a -> a.riskLevel() == RiskLevel.VERY_HIGH).count());

        return distribution;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Credit Risk Assessment");
    }
}
