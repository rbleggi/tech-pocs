# **Personal AI Assistant**

## **Overview**

This project implements a personal AI assistant using the Strategy Pattern in Scala 3. It handles different types of queries (math, text analysis, data lookup) with specialized handlers, providing intelligent responses for various question types.

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

    class ManipuladorConsulta {
        <<trait>>
        +processar(consulta: Consulta): Resposta
    }

    class ManipuladorMatematica {
        +processar(consulta: Consulta): Resposta
    }

    class ManipuladorTexto {
        +processar(consulta: Consulta): Resposta
    }

    class ManipuladorDados {
        -dados: Map[String, String]
        +processar(consulta: Consulta): Resposta
    }

    class AssistentePessoal {
        -manipuladores: Map[String, ManipuladorConsulta]
        +consultar(consulta: Consulta): Resposta
        +consultarAutomatico(pergunta: String): Resposta
    }

    class Consulta {
        +tipo: String
        +pergunta: String
    }

    class Resposta {
        +tipo: String
        +resposta: String
        +confianca: Double
    }

    ManipuladorConsulta <|-- ManipuladorMatematica
    ManipuladorConsulta <|-- ManipuladorTexto
    ManipuladorConsulta <|-- ManipuladorDados
    AssistentePessoal --> ManipuladorConsulta
    AssistentePessoal --> Consulta
    AssistentePessoal --> Resposta
```

---

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/personal-ai-assistant
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
