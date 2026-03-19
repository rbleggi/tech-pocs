# **Credit Risk Assessment**

## Overview

This project implements a credit risk assessment system for Brazilian clients using the Strategy Pattern. It includes three evaluation strategies: Score-Based, Debt-to-Income Ratio, and Composite, enabling flexible risk classification based on financial profiles including CPF, salary, credit score, and debt levels.

---

## Tech Stack

- **Language** → Scala 3.6.3
- **Build Tool** → sbt 1.10.11
- **Runtime** → JDK 25
- **Testing** → ScalaTest 3.2.16

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class RiskAssessmentStrategy {
        <<trait>>
        +avaliar(cliente: Cliente): (RiskLevel, Double, String)
    }

    class ScoreBasedStrategy {
        +avaliar(cliente: Cliente): (RiskLevel, Double, String)
    }

    class DebtToIncomeStrategy {
        +avaliar(cliente: Cliente): (RiskLevel, Double, String)
    }

    class CompositeStrategy {
        +avaliar(cliente: Cliente): (RiskLevel, Double, String)
    }

    class AvaliadorRisco {
        -strategy: RiskAssessmentStrategy
        +avaliar(cliente: Cliente): AvaliacaoRisco
        +avaliarLote(clientes: List[Cliente]): List[AvaliacaoRisco]
    }

    class RiskLevel {
        <<enum>>
        Baixo
        Medio
        Alto
        MuitoAlto
    }

    class Cliente {
        +nome: String
        +cpf: String
        +idade: Int
        +salario: Double
        +scoreCredito: Int
        +dividas: Double
        +tempoEmprego: Int
    }

    class AvaliacaoRisco {
        +cliente: Cliente
        +nivel: RiskLevel
        +score: Double
        +motivo: String
    }

    RiskAssessmentStrategy <|-- ScoreBasedStrategy
    RiskAssessmentStrategy <|-- DebtToIncomeStrategy
    RiskAssessmentStrategy <|-- CompositeStrategy
    AvaliadorRisco --> RiskAssessmentStrategy
    AvaliadorRisco --> Cliente
    AvaliadorRisco --> AvaliacaoRisco
    AvaliacaoRisco --> RiskLevel
    AvaliacaoRisco --> Cliente
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/credit-risk-assessment
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
