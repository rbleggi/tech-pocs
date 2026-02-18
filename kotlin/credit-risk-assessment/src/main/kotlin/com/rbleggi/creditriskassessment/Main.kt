package com.rbleggi.creditriskassessment

import kotlin.math.min
import kotlin.math.max
import kotlin.math.pow

data class Applicant(
    val creditScore: Int,
    val annualIncome: Double,
    val totalDebt: Double,
    val employmentYears: Int,
    val latePayments: Int,
    val existingLoans: Int,
    val age: Int
)

enum class RiskLevel {
    Low, Medium, High, VeryHigh
}

data class RiskAssessment(
    val riskLevel: RiskLevel,
    val score: Double,
    val confidence: Double,
    val factors: Map<String, Double>
)

sealed interface RiskStrategy {
    fun assess(applicant: Applicant): RiskAssessment
}

class CreditScoreStrategy : RiskStrategy {
    override fun assess(applicant: Applicant): RiskAssessment {
        val creditScoreNormalized = applicant.creditScore / 850.0
        val latePaymentPenalty = applicant.latePayments * 0.05
        val loansPenalty = min(applicant.existingLoans * 0.03, 0.15)

        val score = max(0.0, min(1.0,
            creditScoreNormalized - latePaymentPenalty - loansPenalty
        ))

        val riskLevel = when {
            score >= 0.80 -> RiskLevel.Low
            score >= 0.60 -> RiskLevel.Medium
            score >= 0.40 -> RiskLevel.High
            else -> RiskLevel.VeryHigh
        }

        val confidence = 0.85

        val factors = mapOf(
            "credit_score" to creditScoreNormalized,
            "late_payments" to applicant.latePayments.toDouble(),
            "existing_loans" to applicant.existingLoans.toDouble(),
            "score" to score
        )

        return RiskAssessment(riskLevel, score, confidence, factors)
    }
}

class DebtToIncomeStrategy : RiskStrategy {
    override fun assess(applicant: Applicant): RiskAssessment {
        val dti = applicant.totalDebt / applicant.annualIncome
        val employmentBonus = min(applicant.employmentYears * 0.02, 0.10)

        val baseScore = when {
            dti <= 0.20 -> 0.95
            dti <= 0.35 -> 0.75
            dti <= 0.50 -> 0.50
            dti <= 0.70 -> 0.30
            else -> 0.10
        }

        val score = min(1.0, baseScore + employmentBonus)

        val riskLevel = when {
            score >= 0.75 -> RiskLevel.Low
            score >= 0.50 -> RiskLevel.Medium
            score >= 0.30 -> RiskLevel.High
            else -> RiskLevel.VeryHigh
        }

        val confidence = 0.78

        val factors = mapOf(
            "debt_to_income" to dti,
            "annual_income" to applicant.annualIncome,
            "total_debt" to applicant.totalDebt,
            "employment_years" to applicant.employmentYears.toDouble(),
            "score" to score
        )

        return RiskAssessment(riskLevel, score, confidence, factors)
    }
}

class CompositeRiskStrategy : RiskStrategy {
    override fun assess(applicant: Applicant): RiskAssessment {
        val creditScoreWeight = 0.40
        val incomeWeight = 0.25
        val debtWeight = 0.20
        val historyWeight = 0.15

        val creditComponent = (applicant.creditScore / 850.0) * creditScoreWeight
        val incomeComponent = min(applicant.annualIncome / 200000.0, 1.0) * incomeWeight
        val debtComponent = (1.0 - min(applicant.totalDebt / applicant.annualIncome, 1.0)) * debtWeight
        val historyComponent = (min(applicant.employmentYears / 10.0, 1.0) -
                               min(applicant.latePayments * 0.1, 0.5)) * historyWeight

        val score = max(0.0, min(1.0,
            creditComponent + incomeComponent + debtComponent + historyComponent
        ))

        val riskLevel = when {
            score >= 0.70 -> RiskLevel.Low
            score >= 0.50 -> RiskLevel.Medium
            score >= 0.30 -> RiskLevel.High
            else -> RiskLevel.VeryHigh
        }

        val confidence = 0.90

        val factors = mapOf(
            "credit_component" to creditComponent,
            "income_component" to incomeComponent,
            "debt_component" to debtComponent,
            "history_component" to historyComponent,
            "score" to score
        )

        return RiskAssessment(riskLevel, score, confidence, factors)
    }
}

class EnsembleRiskStrategy(private val strategies: List<RiskStrategy>) : RiskStrategy {
    override fun assess(applicant: Applicant): RiskAssessment {
        val assessments = strategies.map { it.assess(applicant) }
        val avgScore = assessments.map { it.score }.average()
        val avgConfidence = assessments.map { it.confidence }.average()

        val riskLevel = when {
            avgScore >= 0.70 -> RiskLevel.Low
            avgScore >= 0.50 -> RiskLevel.Medium
            avgScore >= 0.30 -> RiskLevel.High
            else -> RiskLevel.VeryHigh
        }

        val factors = mapOf(
            "ensemble_size" to strategies.size.toDouble(),
            "avg_score" to avgScore,
            "score_variance" to calculateVariance(assessments.map { it.score }),
            "low_count" to assessments.count { it.riskLevel == RiskLevel.Low }.toDouble(),
            "medium_count" to assessments.count { it.riskLevel == RiskLevel.Medium }.toDouble(),
            "high_count" to assessments.count { it.riskLevel == RiskLevel.High }.toDouble()
        )

        return RiskAssessment(riskLevel, avgScore, avgConfidence, factors)
    }

    private fun calculateVariance(values: List<Double>): Double {
        val mean = values.average()
        return values.map { (it - mean).pow(2) }.average()
    }
}

class CreditRiskSystem(private val strategy: RiskStrategy) {
    fun assess(applicant: Applicant): RiskAssessment =
        strategy.assess(applicant)

    fun assessBatch(applicants: List<Applicant>): List<RiskAssessment> =
        applicants.map { assess(it) }

    fun approvalRecommendation(applicant: Applicant, threshold: Double = 0.60): Pair<Boolean, RiskAssessment> {
        val assessment = assess(applicant)
        val approved = assessment.score >= threshold
        return approved to assessment
    }

    fun riskDistribution(applicants: List<Applicant>): Map<RiskLevel, Int> {
        val assessments = assessBatch(applicants)
        return mapOf(
            RiskLevel.Low to assessments.count { it.riskLevel == RiskLevel.Low },
            RiskLevel.Medium to assessments.count { it.riskLevel == RiskLevel.Medium },
            RiskLevel.High to assessments.count { it.riskLevel == RiskLevel.High },
            RiskLevel.VeryHigh to assessments.count { it.riskLevel == RiskLevel.VeryHigh }
        )
    }
}

fun main() {
    val applicants = listOf(
        Applicant(750, 120000.0, 25000.0, 8, 0, 2, 35),
        Applicant(650, 80000.0, 40000.0, 5, 2, 3, 28),
        Applicant(580, 60000.0, 50000.0, 3, 5, 4, 25),
        Applicant(800, 150000.0, 20000.0, 12, 0, 1, 42),
        Applicant(520, 45000.0, 35000.0, 2, 8, 5, 23)
    )

    println("=== Credit Risk Assessment System ===\n")

    println("Credit Score Strategy:")
    val creditScoreSystem = CreditRiskSystem(CreditScoreStrategy())
    val creditResult = creditScoreSystem.assess(applicants[0])
    println("  Risk Level: ${creditResult.riskLevel}")
    println("  Score: %.2f".format(creditResult.score))
    println("  Confidence: %.2f".format(creditResult.confidence))

    println("\nDebt-to-Income Strategy:")
    val dtiSystem = CreditRiskSystem(DebtToIncomeStrategy())
    val dtiResult = dtiSystem.assess(applicants[0])
    println("  Risk Level: ${dtiResult.riskLevel}")
    println("  Score: %.2f".format(dtiResult.score))
    println("  Confidence: %.2f".format(dtiResult.confidence))

    println("\nComposite Risk Strategy:")
    val compositeSystem = CreditRiskSystem(CompositeRiskStrategy())
    val compositeResult = compositeSystem.assess(applicants[0])
    println("  Risk Level: ${compositeResult.riskLevel}")
    println("  Score: %.2f".format(compositeResult.score))
    println("  Confidence: %.2f".format(compositeResult.confidence))

    println("\nEnsemble Strategy:")
    val ensemble = EnsembleRiskStrategy(listOf(
        CreditScoreStrategy(),
        DebtToIncomeStrategy(),
        CompositeRiskStrategy()
    ))
    val ensembleSystem = CreditRiskSystem(ensemble)
    val ensembleResult = ensembleSystem.assess(applicants[0])
    println("  Risk Level: ${ensembleResult.riskLevel}")
    println("  Score: %.2f".format(ensembleResult.score))
    println("  Confidence: %.2f".format(ensembleResult.confidence))

    println("\nApproval Recommendation:")
    val (approved, assessment) = ensembleSystem.approvalRecommendation(applicants[2])
    println("  Approved: $approved")
    println("  Risk Level: ${assessment.riskLevel}")
    println("  Score: %.2f".format(assessment.score))

    println("\nRisk Distribution (Batch Processing):")
    val distribution = ensembleSystem.riskDistribution(applicants)
    println("  Low Risk: ${distribution[RiskLevel.Low]}")
    println("  Medium Risk: ${distribution[RiskLevel.Medium]}")
    println("  High Risk: ${distribution[RiskLevel.High]}")
    println("  Very High Risk: ${distribution[RiskLevel.VeryHigh]}")
}
