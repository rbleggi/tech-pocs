# **Content Moderation System**

## Overview

This project implements a content moderation system using the Strategy Pattern in Scala 3. It supports multiple moderation strategies (Keyword Filter, Regex Filter, Length Filter) to detect prohibited content, sensitive data, and enforce content policies.

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

    class ModeradorStrategy {
        <<trait>>
        +moderar(conteudo: Conteudo): ResultadoModeracao
    }

    class FiltroKeywords {
        -palavrasProibidas: Set[String]
        +moderar(conteudo: Conteudo): ResultadoModeracao
    }

    class FiltroRegex {
        -padroes: List[Regex]
        +moderar(conteudo: Conteudo): ResultadoModeracao
    }

    class FiltroTamanho {
        -tamanhoMinimo: Int
        -tamanhoMaximo: Int
        +moderar(conteudo: Conteudo): ResultadoModeracao
    }

    class SistemaModeracao {
        -moderadores: List[ModeradorStrategy]
        +moderarConteudo(conteudo: Conteudo): List[ResultadoModeracao]
        +aprovar(conteudo: Conteudo): Boolean
        +adicionarModerador(moderador: ModeradorStrategy): Unit
    }

    class Conteudo {
        +id: String
        +texto: String
        +autor: String
    }

    class ResultadoModeracao {
        +aprovado: Boolean
        +motivo: String
        +severidade: String
    }

    ModeradorStrategy <|-- FiltroKeywords
    ModeradorStrategy <|-- FiltroRegex
    ModeradorStrategy <|-- FiltroTamanho
    SistemaModeracao --> ModeradorStrategy
    SistemaModeracao --> Conteudo
    SistemaModeracao --> ResultadoModeracao
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/content-moderation-system
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
