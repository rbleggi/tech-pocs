# **Type Classes Pattern**

## **Overview**

This project demonstrates Scala 3's type classes using the given/using mechanism. Type classes enable ad-hoc polymorphism by defining behavior for types without modifying their definitions. The example shows formatters for Brazilian data types including currency, dates, CPF, and products.

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

    class Formatador~T~ {
        <<trait>>
        +formatar(value: T): String
    }

    class FormatadorObject {
        +formatar[T](value: T)(using Formatador[T]): String
        +given Formatador[Double]
        +given Formatador[LocalDate]
        +given Formatador[String]
    }

    class Cpf {
        +numero: String
        +given Formatador[Cpf]
    }

    class Produto {
        +nome: String
        +preco: Double
        +categoria: String
        +given Formatador[Produto]
    }

    class Extension {
        +formatar[T](value: T)(using Formatador[T]): String
    }

    Formatador~T~ <.. FormatadorObject
    Formatador~T~ <.. Cpf
    Formatador~T~ <.. Produto
    Extension ..> Formatador~T~
```

---

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/type-classes
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
