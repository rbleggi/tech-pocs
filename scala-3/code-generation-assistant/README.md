# **Code Generation Assistant**

## **Overview**

This project implements a code generation assistant using the Strategy Pattern in Scala 3. It supports multiple code generation strategies (Class, Function, Test) with template-based generation, enabling automated code scaffolding from reusable templates.

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

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/code-generation-assistant
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
