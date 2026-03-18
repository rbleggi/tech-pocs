# **AI Agent System**

## Overview

This project implements a multi-agent AI system using the Strategy Pattern in Scala 3. It supports different agent types (Researcher, Writer, Reviewer) that collaborate on tasks, exchanging messages and results to complete complex workflows.

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

    class AgenteStrategy {
        <<trait>>
        +processar(tarefa: Tarefa): ResultadoAgente
        +receberMensagem(mensagem: Mensagem): String
    }

    class AgentePesquisador {
        -baseConhecimento: Map[String, String]
        +processar(tarefa: Tarefa): ResultadoAgente
        +receberMensagem(mensagem: Mensagem): String
    }

    class AgenteEscritor {
        +processar(tarefa: Tarefa): ResultadoAgente
        +receberMensagem(mensagem: Mensagem): String
    }

    class AgenteRevisor {
        +processar(tarefa: Tarefa): ResultadoAgente
        +receberMensagem(mensagem: Mensagem): String
    }

    class SistemaAgentes {
        -agentes: Map[String, AgenteStrategy]
        -mensagens: List[Mensagem]
        +executarTarefa(nomeAgente: String, tarefa: Tarefa): Option[ResultadoAgente]
        +enviarMensagem(mensagem: Mensagem): Unit
        +colaborar(tarefa: Tarefa): List[ResultadoAgente]
        +obterMensagens: List[Mensagem]
    }

    class Tarefa {
        +id: String
        +descricao: String
        +tipo: String
    }

    class Mensagem {
        +remetente: String
        +destinatario: String
        +conteudo: String
    }

    class ResultadoAgente {
        +agente: String
        +tarefa: String
        +resultado: String
    }

    AgenteStrategy <|-- AgentePesquisador
    AgenteStrategy <|-- AgenteEscritor
    AgenteStrategy <|-- AgenteRevisor
    SistemaAgentes --> AgenteStrategy
    SistemaAgentes --> Tarefa
    SistemaAgentes --> Mensagem
    SistemaAgentes --> ResultadoAgente
```

---

## Setup Instructions

### 1 - Clone

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/ai-agent-system
```

### 2 - Build

```bash
sbt compile
```

### 3 - Test

```bash
sbt test
```
