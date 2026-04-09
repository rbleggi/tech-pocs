# **Code Generation Assistant**

## Overview

This project implements a code generation assistant using the Strategy Pattern in Scala 3. It supports multiple code generation strategies (Class, Function, Test) with template-based generation, enabling automated code scaffolding from reusable templates.

---

## Tech Stack

- **Language** -> Scala 3.6.3
- **Build Tool** -> sbt 1.10.11
- **Runtime** -> JDK 25
- **Testing** -> ScalaTest 3.2.16

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class GeradorStrategy {
        <<trait>>
        +gerar(template: Template, parametros: Map[String, String]): CodigoGerado
    }

    class GeradorClasse {
        +gerar(template: Template, parametros: Map[String, String]): CodigoGerado
    }

    class GeradorFuncao {
        +gerar(template: Template, parametros: Map[String, String]): CodigoGerado
    }

    class GeradorTeste {
        +gerar(template: Template, parametros: Map[String, String]): CodigoGerado
    }

    class AssistenteGeracao {
        -gerador: GeradorStrategy
        +gerarCodigo(template: Template, parametros: Map[String, String]): CodigoGerado
    }

    class Template {
        +nome: String
        +conteudo: String
    }

    class CodigoGerado {
        +arquivo: String
        +conteudo: String
    }

    GeradorStrategy <|-- GeradorClasse
    GeradorStrategy <|-- GeradorFuncao
    GeradorStrategy <|-- GeradorTeste
    AssistenteGeracao --> GeradorStrategy
    AssistenteGeracao --> Template
    AssistenteGeracao --> CodigoGerado
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/code-generation-assistant
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
