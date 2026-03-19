# **Domain Fine-Tuned LLM**

## Overview

This project implements a domain-specific language model using the Strategy Pattern in Scala 3. It supports multiple specialized domains (Medical, Legal, Technology) with knowledge bases, providing contextually accurate responses based on domain expertise.

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

    class DominioStrategy {
        <<trait>>
        +gerar(contexto: Contexto): RespostaLLM
    }

    class DominioMedico {
        -conhecimento: Map[String, String]
        +gerar(contexto: Contexto): RespostaLLM
    }

    class DominioJuridico {
        -conhecimento: Map[String, String]
        +gerar(contexto: Contexto): RespostaLLM
    }

    class DominioTecnologia {
        -conhecimento: Map[String, String]
        +gerar(contexto: Contexto): RespostaLLM
    }

    class LLMDominio {
        -dominios: Map[String, DominioStrategy]
        +consultar(contexto: Contexto): RespostaLLM
        +adicionarDominio(nome: String, dominio: DominioStrategy): Unit
    }

    class Contexto {
        +dominio: String
        +pergunta: String
    }

    class RespostaLLM {
        +resposta: String
        +confianca: Double
        +termos: List[String]
    }

    DominioStrategy <|-- DominioMedico
    DominioStrategy <|-- DominioJuridico
    DominioStrategy <|-- DominioTecnologia
    LLMDominio --> DominioStrategy
    LLMDominio --> Contexto
    LLMDominio --> RespostaLLM
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/domain-fine-tuned-llm
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
