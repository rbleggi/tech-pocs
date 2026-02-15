# **Credit Risk Assessment**

## **Overview**

This project implements a credit risk assessment system for Brazilian clients using the Strategy Pattern. It includes three evaluation strategies: Score-Based, Debt-to-Income Ratio, and Composite, enabling flexible risk classification based on financial profiles including CPF, salary, credit score, and debt levels.

---

## **Tech Stack**

- **Scala 3.6.3** → Modern JVM language with advanced type safety and functional programming.
- **SBT 1.10.11** → Scala build tool.
- **JDK 25** → Java runtime environment.
- **ScalaTest 3.2.16** → Testing framework.

---

## **Architecture Diagram**

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

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/credit-risk-assessment
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
