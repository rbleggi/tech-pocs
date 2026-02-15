# **Anomaly Detection**

## **Overview**

This project implements an anomaly detection system for Brazilian financial transactions using the Strategy Pattern. It includes three detection strategies: Z-Score, Interquartile Range (IQR), and Isolation, enabling flexible identification of unusual patterns in transaction data.

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

    class AnomalyDetectionStrategy {
        <<trait>>
        +detectar(transacoes: List[Transacao]): List[ResultadoDeteccao]
    }

    class ZScoreStrategy {
        -limiar: Double
        -calcularMedia(valores: List[Double]): Double
        -calcularDesvioPadrao(valores: List[Double], media: Double): Double
        +detectar(transacoes: List[Transacao]): List[ResultadoDeteccao]
    }

    class IQRStrategy {
        -calcularQuartis(valores: List[Double]): (Double, Double, Double)
        +detectar(transacoes: List[Transacao]): List[ResultadoDeteccao]
    }

    class IsolationStrategy {
        -limiarAlto: Double
        -limiarMuitoAlto: Double
        +detectar(transacoes: List[Transacao]): List[ResultadoDeteccao]
    }

    class DetectorAnomalias {
        -strategy: AnomalyDetectionStrategy
        +detectar(transacoes: List[Transacao]): List[ResultadoDeteccao]
        +detectarAnomalias(transacoes: List[Transacao]): List[ResultadoDeteccao]
    }

    class Transacao {
        +id: String
        +valor: Double
        +timestamp: Long
        +cpf: String
    }

    class ResultadoDeteccao {
        +transacao: Transacao
        +anomalia: Boolean
        +score: Double
        +motivo: String
    }

    AnomalyDetectionStrategy <|-- ZScoreStrategy
    AnomalyDetectionStrategy <|-- IQRStrategy
    AnomalyDetectionStrategy <|-- IsolationStrategy
    DetectorAnomalias --> AnomalyDetectionStrategy
    DetectorAnomalias --> Transacao
    DetectorAnomalias --> ResultadoDeteccao
    ResultadoDeteccao --> Transacao
```

---

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/anomaly-detection
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
