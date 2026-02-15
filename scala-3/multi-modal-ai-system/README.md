# **Multi-Modal AI System**

## **Overview**

This project implements a multi-modal AI system using the Strategy Pattern in Scala 3. It processes different types of input (text, numbers, categories) with specialized analyzers, providing intelligent analysis for each modality.

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

    class TipoEntrada {
        <<sealed trait>>
    }

    class EntradaTexto {
        +texto: String
    }

    class EntradaNumero {
        +numero: Double
    }

    class EntradaCategoria {
        +categoria: String
    }

    class ProcessadorStrategy {
        <<trait>>
        +processar(entrada: TipoEntrada): Analise
    }

    class ProcessadorTexto {
        +processar(entrada: TipoEntrada): Analise
    }

    class ProcessadorNumero {
        +processar(entrada: TipoEntrada): Analise
    }

    class ProcessadorCategoria {
        -categorias: Map[String, String]
        +processar(entrada: TipoEntrada): Analise
    }

    class SistemaMultiModal {
        -processadores: Map[String, ProcessadorStrategy]
        +processar(entrada: TipoEntrada, modo: String): Analise
        +adicionarProcessador(modo: String, processador: ProcessadorStrategy): Unit
    }

    class Analise {
        +tipo: String
        +resultado: String
        +confianca: Double
    }

    TipoEntrada <|-- EntradaTexto
    TipoEntrada <|-- EntradaNumero
    TipoEntrada <|-- EntradaCategoria
    ProcessadorStrategy <|-- ProcessadorTexto
    ProcessadorStrategy <|-- ProcessadorNumero
    ProcessadorStrategy <|-- ProcessadorCategoria
    SistemaMultiModal --> ProcessadorStrategy
    SistemaMultiModal --> TipoEntrada
    SistemaMultiModal --> Analise
```

---

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/multi-modal-ai-system
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
