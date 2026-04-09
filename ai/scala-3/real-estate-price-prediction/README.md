# **Real Estate Price Prediction**

## Overview

This project implements a real estate price prediction system for Brazilian properties using the Strategy Pattern. It includes three prediction strategies: Linear Regression, K-Nearest Neighbors (KNN), and Ensemble, allowing flexible price estimation based on property features like area, location, and age.

---

## Tech Stack

- **Language** -> Scala 3
- **Build Tool** -> sbt
- **Testing** -> ScalaTest 3.2.16
- **JDK** -> 25

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class PredictionStrategy {
        <<trait>>
        +prever(imovel: Imovel): (Double, Double)
    }

    class LinearRegressionStrategy {
        -pesoArea: Double
        -pesoQuartos: Double
        -pesoIdade: Double
        -multiplicadorBairro: Map[String, Double]
        -multiplicadorCidade: Map[String, Double]
        +prever(imovel: Imovel): (Double, Double)
    }

    class KNNStrategy {
        -baseTreinamento: List[(Imovel, Double)]
        -distancia(i1: Imovel, i2: Imovel): Double
        +prever(imovel: Imovel): (Double, Double)
    }

    class EnsembleStrategy {
        -linear: LinearRegressionStrategy
        -knn: KNNStrategy
        +prever(imovel: Imovel): (Double, Double)
    }

    class PrevisaoPreco {
        -strategy: PredictionStrategy
        +prever(imovel: Imovel): Predicao
        +preverLote(imoveis: List[Imovel]): List[Predicao]
    }

    class Imovel {
        +area: Double
        +quartos: Int
        +bairro: String
        +cidade: String
        +idade: Int
    }

    class Predicao {
        +imovel: Imovel
        +precoEstimado: Double
        +confianca: Double
    }

    PredictionStrategy <|-- LinearRegressionStrategy
    PredictionStrategy <|-- KNNStrategy
    PredictionStrategy <|-- EnsembleStrategy
    EnsembleStrategy --> LinearRegressionStrategy
    EnsembleStrategy --> KNNStrategy
    PrevisaoPreco --> PredictionStrategy
    PrevisaoPreco --> Imovel
    PrevisaoPreco --> Predicao
    Predicao --> Imovel
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/real-estate-price-prediction
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
