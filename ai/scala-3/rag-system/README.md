# **RAG System (Retrieval-Augmented Generation)**

## Overview

This project implements a Retrieval-Augmented Generation (RAG) system using the Strategy Pattern in Scala 3. It supports Portuguese document retrieval with multiple strategies (Keyword, Semantic, Hybrid) and response generation approaches (Template, Summary), enabling flexible question-answering over knowledge bases.

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

    class RetrievalStrategy {
        <<trait>>
        +recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento]
    }

    class KeywordStrategy {
        -extrairPalavrasChave(texto: String): Set[String]
        -calcularRelevancia(consulta: Set[String], doc: Documento): Int
        +recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento]
    }

    class SemanticStrategy {
        -sinonimos: Map[String, Set[String]]
        -expandirConsulta(texto: String): Set[String]
        -calcularRelevancia(consultaExpandida: Set[String], doc: Documento): Int
        +recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento]
    }

    class HybridStrategy {
        -keyword: KeywordStrategy
        -semantic: SemanticStrategy
        +recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento]
    }

    class GenerationStrategy {
        <<trait>>
        +gerar(consulta: Consulta, documentos: List[Documento]): String
    }

    class TemplateGeneration {
        +gerar(consulta: Consulta, documentos: List[Documento]): String
    }

    class SummaryGeneration {
        +gerar(consulta: Consulta, documentos: List[Documento]): String
    }

    class RAGSystem {
        -retrieval: RetrievalStrategy
        -generation: GenerationStrategy
        -baseConhecimento: List[Documento]
        +adicionarDocumento(doc: Documento): Unit
        +adicionarDocumentos(docs: List[Documento]): Unit
        +consultar(consulta: Consulta): Resposta
    }

    class Documento {
        +id: String
        +titulo: String
        +conteudo: String
        +categoria: String
    }

    class Consulta {
        +texto: String
    }

    class Resposta {
        +consulta: Consulta
        +documentos: List[Documento]
        +resposta: String
        +relevancia: Double
    }

    RetrievalStrategy <|-- KeywordStrategy
    RetrievalStrategy <|-- SemanticStrategy
    RetrievalStrategy <|-- HybridStrategy
    HybridStrategy --> KeywordStrategy
    HybridStrategy --> SemanticStrategy
    GenerationStrategy <|-- TemplateGeneration
    GenerationStrategy <|-- SummaryGeneration
    RAGSystem --> RetrievalStrategy
    RAGSystem --> GenerationStrategy
    RAGSystem --> Documento
    RAGSystem --> Consulta
    RAGSystem --> Resposta
    Resposta --> Consulta
    Resposta --> Documento
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/rag-system
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
