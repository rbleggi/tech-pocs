package com.rbleggi.creditriskassessment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class CreditRiskAssessmentTest {

    private final Applicant lowRiskApplicant = new Applicant(750, 120000, 25000, 8, 0, 2, 35);
    private final Applicant mediumRiskApplicant = new Applicant(650, 80000, 40000, 5, 2, 3, 28);
    private final Applicant highRiskApplicant = new Applicant(580, 60000, 50000, 3, 5, 4, 25);
    private final Applicant veryHighRiskApplicant = new Applicant(520, 45000, 35000, 2, 8, 5, 23);

    @Test
    @DisplayName("CreditScoreStrategy assesses low risk for high credit score")
    void creditScoreStrategy_highCreditScore_lowRisk() {
        CreditScoreStrategy strategy = new CreditScoreStrategy();
        RiskAssessment result = strategy.assess(lowRiskApplicant);

        assertEquals(RiskLevel.LOW, result.riskLevel());
        assertTrue(result.score() > 0.70);
        assertEquals(0.85, result.confidence());
        assertTrue(result.factors().containsKey("credit_score"));
    }

    @Test
    @DisplayName("CreditScoreStrategy penalizes late payments")
    void creditScoreStrategy_latePayments_lowerScore() {
        CreditScoreStrategy strategy = new CreditScoreStrategy();
        Applicant goodApplicant = new Applicant(750, 100000, 30000, 5, 0, 2, 30);
        Applicant lateApplicant = new Applicant(750, 100000, 30000, 5, 5, 2, 30);

        RiskAssessment goodResult = strategy.assess(goodApplicant);
        RiskAssessment lateResult = strategy.assess(lateApplicant);

        assertTrue(goodResult.score() > lateResult.score());
    }

    @Test
    @DisplayName("CreditScoreStrategy penalizes existing loans")
    void creditScoreStrategy_existingLoans_lowerScore() {
        CreditScoreStrategy strategy = new CreditScoreStrategy();
        Applicant fewLoans = new Applicant(750, 100000, 30000, 5, 0, 1, 30);
        Applicant manyLoans = new Applicant(750, 100000, 30000, 5, 0, 6, 30);

        RiskAssessment fewResult = strategy.assess(fewLoans);
        RiskAssessment manyResult = strategy.assess(manyLoans);

        assertTrue(fewResult.score() > manyResult.score());
    }

    @Test
    @DisplayName("DebtToIncomeStrategy assesses low risk for low DTI")
    void debtToIncomeStrategy_lowDTI_lowRisk() {
        DebtToIncomeStrategy strategy = new DebtToIncomeStrategy();
        Applicant lowDTI = new Applicant(700, 100000, 15000, 5, 1, 2, 30);
        RiskAssessment result = strategy.assess(lowDTI);

        assertEquals(RiskLevel.LOW, result.riskLevel());
        assertTrue(result.score() > 0.75);
        assertTrue(result.factors().containsKey("debt_to_income"));
    }

    @Test
    @DisplayName("DebtToIncomeStrategy assesses high risk for high DTI")
    void debtToIncomeStrategy_highDTI_highRisk() {
        DebtToIncomeStrategy strategy = new DebtToIncomeStrategy();
        Applicant highDTI = new Applicant(700, 50000, 40000, 2, 1, 2, 25);
        RiskAssessment result = strategy.assess(highDTI);

        assertTrue(result.score() < 0.50);
        assertTrue(result.factors().get("debt_to_income") > 0.70);
    }

    @Test
    @DisplayName("DebtToIncomeStrategy rewards employment years")
    void debtToIncomeStrategy_employmentYears_higherScore() {
        DebtToIncomeStrategy strategy = new DebtToIncomeStrategy();
        Applicant newEmployee = new Applicant(700, 100000, 30000, 1, 0, 2, 25);
        Applicant experienced = new Applicant(700, 100000, 30000, 10, 0, 2, 35);

        RiskAssessment newResult = strategy.assess(newEmployee);
        RiskAssessment expResult = strategy.assess(experienced);

        assertTrue(expResult.score() > newResult.score());
    }

    @Test
    @DisplayName("CompositeRiskStrategy combines multiple factors")
    void compositeRiskStrategy_combinedFactors_allFactorsPresent() {
        CompositeRiskStrategy strategy = new CompositeRiskStrategy();
        RiskAssessment result = strategy.assess(lowRiskApplicant);

        assertTrue(result.factors().containsKey("credit_component"));
        assertTrue(result.factors().containsKey("income_component"));
        assertTrue(result.factors().containsKey("debt_component"));
        assertTrue(result.factors().containsKey("history_component"));
        assertEquals(0.90, result.confidence());
    }

    @Test
    @DisplayName("CompositeRiskStrategy assesses low risk applicant correctly")
    void compositeRiskStrategy_lowRiskApplicant_lowRisk() {
        CompositeRiskStrategy strategy = new CompositeRiskStrategy();
        RiskAssessment result = strategy.assess(lowRiskApplicant);

        assertEquals(RiskLevel.LOW, result.riskLevel());
        assertTrue(result.score() > 0.60);
    }

    @Test
    @DisplayName("CompositeRiskStrategy assesses very high risk applicant correctly")
    void compositeRiskStrategy_veryHighRiskApplicant_highOrVeryHighRisk() {
        CompositeRiskStrategy strategy = new CompositeRiskStrategy();
        RiskAssessment result = strategy.assess(veryHighRiskApplicant);

        assertTrue(result.riskLevel() == RiskLevel.HIGH || result.riskLevel() == RiskLevel.VERY_HIGH);
        assertTrue(result.score() < 0.50);
    }

    @Test
    @DisplayName("EnsembleRiskStrategy combines multiple strategies")
    void ensembleRiskStrategy_multipleStrategies_ensembleFactorsPresent() {
        List<RiskStrategy> strategies = List.of(
            new CreditScoreStrategy(),
            new DebtToIncomeStrategy(),
            new CompositeRiskStrategy()
        );
        EnsembleRiskStrategy ensemble = new EnsembleRiskStrategy(strategies);
        RiskAssessment result = ensemble.assess(lowRiskApplicant);

        assertEquals(3.0, result.factors().get("ensemble_size"));
        assertTrue(result.factors().containsKey("avg_score"));
        assertTrue(result.factors().containsKey("score_variance"));
    }

    @Test
    @DisplayName("EnsembleRiskStrategy averages scores from multiple strategies")
    void ensembleRiskStrategy_twoStrategies_averageScore() {
        List<RiskStrategy> strategies = List.of(
            new CreditScoreStrategy(),
            new DebtToIncomeStrategy()
        );
        EnsembleRiskStrategy ensemble = new EnsembleRiskStrategy(strategies);

        RiskAssessment creditResult = new CreditScoreStrategy().assess(lowRiskApplicant);
        RiskAssessment dtiResult = new DebtToIncomeStrategy().assess(lowRiskApplicant);
        RiskAssessment ensembleResult = ensemble.assess(lowRiskApplicant);

        double expectedAvg = (creditResult.score() + dtiResult.score()) / 2.0;
        assertEquals(expectedAvg, ensembleResult.score(), 0.01);
    }

    @Test
    @DisplayName("EnsembleRiskStrategy counts risk levels correctly")
    void ensembleRiskStrategy_threeStrategies_correctCounts() {
        EnsembleRiskStrategy ensemble = new EnsembleRiskStrategy(List.of(
            new CreditScoreStrategy(),
            new DebtToIncomeStrategy(),
            new CompositeRiskStrategy()
        ));
        RiskAssessment result = ensemble.assess(lowRiskApplicant);

        double totalCounts = result.factors().get("low_count") +
                             result.factors().get("medium_count") +
                             result.factors().get("high_count");
        assertEquals(3.0, totalCounts);
    }

    @Test
    @DisplayName("CreditRiskSystem assesses single applicant")
    void creditRiskSystem_singleApplicant_assessmentReturned() {
        CreditRiskSystem system = new CreditRiskSystem(new CompositeRiskStrategy());
        RiskAssessment result = system.assess(lowRiskApplicant);

        assertEquals(RiskLevel.LOW, result.riskLevel());
        assertTrue(result.score() > 0.0);
    }

    @Test
    @DisplayName("CreditRiskSystem assesses batch of applicants")
    void creditRiskSystem_batchApplicants_allAssessed() {
        CreditRiskSystem system = new CreditRiskSystem(new CompositeRiskStrategy());
        List<Applicant> applicants = List.of(lowRiskApplicant, mediumRiskApplicant, highRiskApplicant);
        List<RiskAssessment> results = system.assessBatch(applicants);

        assertEquals(3, results.size());
        assertEquals(RiskLevel.LOW, results.get(0).riskLevel());
    }

    @Test
    @DisplayName("CreditRiskSystem provides approval recommendation")
    void creditRiskSystem_lowRiskApplicant_approved() {
        CreditRiskSystem system = new CreditRiskSystem(new CompositeRiskStrategy());
        CreditRiskSystem.ApprovalRecommendation recommendation = system.approvalRecommendation(lowRiskApplicant, 0.60);

        assertTrue(recommendation.approved());
        assertTrue(recommendation.assessment().score() >= 0.60);
    }

    @Test
    @DisplayName("CreditRiskSystem rejects high risk applicant")
    void creditRiskSystem_veryHighRiskApplicant_rejected() {
        CreditRiskSystem system = new CreditRiskSystem(new CompositeRiskStrategy());
        CreditRiskSystem.ApprovalRecommendation recommendation = system.approvalRecommendation(veryHighRiskApplicant, 0.60);

        assertFalse(recommendation.approved());
        assertTrue(recommendation.assessment().score() < 0.60);
    }

    @Test
    @DisplayName("CreditRiskSystem calculates risk distribution")
    void creditRiskSystem_batchApplicants_distributionCalculated() {
        CreditRiskSystem system = new CreditRiskSystem(new CompositeRiskStrategy());
        List<Applicant> applicants = List.of(
            lowRiskApplicant,
            mediumRiskApplicant,
            highRiskApplicant,
            veryHighRiskApplicant
        );
        Map<RiskLevel, Integer> distribution = system.riskDistribution(applicants);

        assertEquals(4, distribution.values().stream().mapToInt(Integer::intValue).sum());
        assertTrue(distribution.containsKey(RiskLevel.LOW));
        assertTrue(distribution.containsKey(RiskLevel.MEDIUM));
        assertTrue(distribution.containsKey(RiskLevel.HIGH));
        assertTrue(distribution.containsKey(RiskLevel.VERY_HIGH));
    }

    @Test
    @DisplayName("RiskLevel enum supports all levels")
    void riskLevel_allLevels_defined() {
        assertNotNull(RiskLevel.LOW);
        assertNotNull(RiskLevel.MEDIUM);
        assertNotNull(RiskLevel.HIGH);
        assertNotNull(RiskLevel.VERY_HIGH);
    }
}
