# **Credit Risk Assessment**

## Overview

Credit risk assessment system demonstrating the **Strategy Pattern** with multiple risk evaluation algorithms including credit score analysis, debt-to-income ratio, composite scoring, and ensemble methods.

---

## Tech Stack

- **Kotlin 2.1.10** → Modern JVM language with concise syntax and null safety
- **Gradle** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Applicant {
        +creditScore: Int
        +annualIncome: Double
        +totalDebt: Double
        +employmentYears: Int
        +latePayments: Int
        +existingLoans: Int
        +age: Int
    }

    class RiskLevel {
        <<enumeration>>
        Low
        Medium
        High
        VeryHigh
    }

    class RiskAssessment {
        +riskLevel: RiskLevel
        +score: Double
        +confidence: Double
        +factors: Map~String, Double~
    }

    class RiskStrategy {
        <<interface>>
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
        +approvalRecommendation(applicant: Applicant, threshold: Double): Pair~Boolean, RiskAssessment~
        +riskDistribution(applicants: List~Applicant~): Map~RiskLevel, Int~
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
cd kotlin/credit-risk-assessment
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run the Application
```bash
./gradlew run
```

### 4 - Run Tests
```bash
./gradlew test
```
