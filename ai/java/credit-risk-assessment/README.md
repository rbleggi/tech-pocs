# **Credit Risk Assessment**

## Overview

Credit risk assessment system demonstrating the **Strategy Pattern** with multiple risk evaluation algorithms including credit score analysis, debt-to-income ratio, composite scoring, and ensemble methods.

---

## Tech Stack

- **Java 25** → Latest JDK with modern language features including records and sealed interfaces.
- **Gradle** → Build automation and dependency management.
- **JUnit 5** → Testing framework for unit tests.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Applicant {
        +creditScore: int
        +annualIncome: double
        +totalDebt: double
        +employmentYears: int
        +latePayments: int
        +existingLoans: int
        +age: int
    }

    class RiskLevel {
        <<enumeration>>
        LOW
        MEDIUM
        HIGH
        VERY_HIGH
    }

    class RiskAssessment {
        +riskLevel: RiskLevel
        +score: double
        +confidence: double
        +factors: Map~String, Double~
    }

    class RiskStrategy {
        <<sealed interface>>
        +assess(applicant: Applicant): RiskAssessment
    }

    class CreditScoreStrategy {
        +assess(applicant: Applicant): RiskAssessment
    }

    class DebtToIncomeStrategy {
        +assess(applicant: Applicant): RiskAssessment
    }

    class CompositeRiskStrategy {
        +assess(applicant: Applicant): RiskAssessment
    }

    class EnsembleRiskStrategy {
        -strategies: List~RiskStrategy~
        +assess(applicant: Applicant): RiskAssessment
    }

    class CreditRiskSystem {
        -strategy: RiskStrategy
        +assess(applicant: Applicant): RiskAssessment
        +assessBatch(applicants: List~Applicant~): List~RiskAssessment~
        +approvalRecommendation(applicant: Applicant, threshold: double): ApprovalRecommendation
        +riskDistribution(applicants: List~Applicant~): Map~RiskLevel, Integer~
    }

    RiskStrategy <|.. CreditScoreStrategy
    RiskStrategy <|.. DebtToIncomeStrategy
    RiskStrategy <|.. CompositeRiskStrategy
    RiskStrategy <|.. EnsembleRiskStrategy
    CreditRiskSystem --> RiskStrategy
    RiskStrategy --> RiskAssessment
    RiskAssessment --> RiskLevel
    EnsembleRiskStrategy --> RiskStrategy
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/credit-risk-assessment
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
