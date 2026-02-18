package com.rbleggi.creditriskassessment

import kotlin.test.*

class CreditRiskAssessmentTest {

    private val lowRiskApplicant = Applicant(750, 120000.0, 25000.0, 8, 0, 2, 35)
    private val mediumRiskApplicant = Applicant(650, 80000.0, 40000.0, 5, 2, 3, 28)
    private val highRiskApplicant = Applicant(580, 60000.0, 50000.0, 3, 5, 4, 25)
    private val veryHighRiskApplicant = Applicant(520, 45000.0, 35000.0, 2, 8, 5, 23)

    @Test
    fun `CreditScoreStrategy assesses low risk for high credit score`() {
        val strategy = CreditScoreStrategy()
        val result = strategy.assess(lowRiskApplicant)

        assertEquals(RiskLevel.Low, result.riskLevel)
        assertTrue(result.score > 0.70)
        assertEquals(0.85, result.confidence)
        assertTrue(result.factors.containsKey("credit_score"))
    }

    @Test
    fun `CreditScoreStrategy penalizes for late payments`() {
        val strategy = CreditScoreStrategy()
        val goodApplicant = Applicant(750, 100000.0, 30000.0, 5, 0, 2, 30)
        val lateApplicant = Applicant(750, 100000.0, 30000.0, 5, 5, 2, 30)

        val goodResult = strategy.assess(goodApplicant)
        val lateResult = strategy.assess(lateApplicant)

        assertTrue(goodResult.score > lateResult.score)
    }

    @Test
    fun `CreditScoreStrategy penalizes for existing loans`() {
        val strategy = CreditScoreStrategy()
        val fewLoans = Applicant(750, 100000.0, 30000.0, 5, 0, 1, 30)
        val manyLoans = Applicant(750, 100000.0, 30000.0, 5, 0, 6, 30)

        val fewResult = strategy.assess(fewLoans)
        val manyResult = strategy.assess(manyLoans)

        assertTrue(fewResult.score > manyResult.score)
    }

    @Test
    fun `DebtToIncomeStrategy assesses low risk for low DTI`() {
        val strategy = DebtToIncomeStrategy()
        val lowDTI = Applicant(700, 100000.0, 15000.0, 5, 1, 2, 30)
        val result = strategy.assess(lowDTI)

        assertEquals(RiskLevel.Low, result.riskLevel)
        assertTrue(result.score > 0.75)
        assertTrue(result.factors.containsKey("debt_to_income"))
    }

    @Test
    fun `DebtToIncomeStrategy assesses high risk for high DTI`() {
        val strategy = DebtToIncomeStrategy()
        val highDTI = Applicant(700, 50000.0, 40000.0, 2, 1, 2, 25)
        val result = strategy.assess(highDTI)

        assertTrue(result.score < 0.50)
        assertTrue(result.factors["debt_to_income"]!! > 0.70)
    }

    @Test
    fun `DebtToIncomeStrategy rewards employment years`() {
        val strategy = DebtToIncomeStrategy()
        val newEmployee = Applicant(700, 100000.0, 30000.0, 1, 0, 2, 25)
        val experienced = Applicant(700, 100000.0, 30000.0, 10, 0, 2, 35)

        val newResult = strategy.assess(newEmployee)
        val expResult = strategy.assess(experienced)

        assertTrue(expResult.score > newResult.score)
    }

    @Test
    fun `CompositeRiskStrategy combines multiple factors`() {
        val strategy = CompositeRiskStrategy()
        val result = strategy.assess(lowRiskApplicant)

        assertTrue(result.factors.containsKey("credit_component"))
        assertTrue(result.factors.containsKey("income_component"))
        assertTrue(result.factors.containsKey("debt_component"))
        assertTrue(result.factors.containsKey("history_component"))
        assertEquals(0.90, result.confidence)
    }

    @Test
    fun `CompositeRiskStrategy assesses low risk applicant correctly`() {
        val strategy = CompositeRiskStrategy()
        val result = strategy.assess(lowRiskApplicant)

        assertEquals(RiskLevel.Low, result.riskLevel)
        assertTrue(result.score > 0.60)
    }

    @Test
    fun `CompositeRiskStrategy assesses very high risk applicant correctly`() {
        val strategy = CompositeRiskStrategy()
        val result = strategy.assess(veryHighRiskApplicant)

        assertTrue(result.riskLevel == RiskLevel.High || result.riskLevel == RiskLevel.VeryHigh)
        assertTrue(result.score < 0.50)
    }

    @Test
    fun `EnsembleRiskStrategy combines multiple strategies`() {
        val strategies = listOf(
            CreditScoreStrategy(),
            DebtToIncomeStrategy(),
            CompositeRiskStrategy()
        )
        val ensemble = EnsembleRiskStrategy(strategies)
        val result = ensemble.assess(lowRiskApplicant)

        assertEquals(3.0, result.factors["ensemble_size"])
        assertTrue(result.factors.containsKey("avg_score"))
        assertTrue(result.factors.containsKey("score_variance"))
    }

    @Test
    fun `EnsembleRiskStrategy averages scores from multiple strategies`() {
        val strategies = listOf(
            CreditScoreStrategy(),
            DebtToIncomeStrategy()
        )
        val ensemble = EnsembleRiskStrategy(strategies)

        val creditResult = CreditScoreStrategy().assess(lowRiskApplicant)
        val dtiResult = DebtToIncomeStrategy().assess(lowRiskApplicant)
        val ensembleResult = ensemble.assess(lowRiskApplicant)

        val expectedAvg = (creditResult.score + dtiResult.score) / 2.0
        assertEquals(expectedAvg, ensembleResult.score, 0.01)
    }

    @Test
    fun `EnsembleRiskStrategy counts risk levels correctly`() {
        val ensemble = EnsembleRiskStrategy(listOf(
            CreditScoreStrategy(),
            DebtToIncomeStrategy(),
            CompositeRiskStrategy()
        ))
        val result = ensemble.assess(lowRiskApplicant)

        val totalCounts = result.factors["low_count"]!! +
                          result.factors["medium_count"]!! +
                          result.factors["high_count"]!!
        assertEquals(3.0, totalCounts)
    }

    @Test
    fun `CreditRiskSystem assesses single applicant`() {
        val system = CreditRiskSystem(CompositeRiskStrategy())
        val result = system.assess(lowRiskApplicant)

        assertEquals(RiskLevel.Low, result.riskLevel)
        assertTrue(result.score > 0.0)
    }

    @Test
    fun `CreditRiskSystem assesses batch of applicants`() {
        val system = CreditRiskSystem(CompositeRiskStrategy())
        val applicants = listOf(lowRiskApplicant, mediumRiskApplicant, highRiskApplicant)
        val results = system.assessBatch(applicants)

        assertEquals(3, results.size)
        assertEquals(RiskLevel.Low, results[0].riskLevel)
    }

    @Test
    fun `CreditRiskSystem provides approval recommendation`() {
        val system = CreditRiskSystem(CompositeRiskStrategy())
        val (approved, assessment) = system.approvalRecommendation(lowRiskApplicant, 0.60)

        assertTrue(approved)
        assertTrue(assessment.score >= 0.60)
    }

    @Test
    fun `CreditRiskSystem rejects high risk applicant`() {
        val system = CreditRiskSystem(CompositeRiskStrategy())
        val (approved, assessment) = system.approvalRecommendation(veryHighRiskApplicant, 0.60)

        assertFalse(approved)
        assertTrue(assessment.score < 0.60)
    }

    @Test
    fun `CreditRiskSystem calculates risk distribution`() {
        val system = CreditRiskSystem(CompositeRiskStrategy())
        val applicants = listOf(
            lowRiskApplicant,
            mediumRiskApplicant,
            highRiskApplicant,
            veryHighRiskApplicant
        )
        val distribution = system.riskDistribution(applicants)

        assertEquals(4, distribution.values.sum())
        assertTrue(distribution.containsKey(RiskLevel.Low))
        assertTrue(distribution.containsKey(RiskLevel.Medium))
        assertTrue(distribution.containsKey(RiskLevel.High))
        assertTrue(distribution.containsKey(RiskLevel.VeryHigh))
    }

    @Test
    fun `RiskLevel enum supports all levels`() {
        assertNotNull(RiskLevel.Low)
        assertNotNull(RiskLevel.Medium)
        assertNotNull(RiskLevel.High)
        assertNotNull(RiskLevel.VeryHigh)
    }
}
