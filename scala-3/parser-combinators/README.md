# **Parser Combinators**

## **Overview**

This project demonstrates a simple parser combinator library in Scala 3. Parser combinators allow building complex parsers from small, reusable components using functional composition. The example parses Brazilian addresses with street, number, city, state, and postal code.

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

    class Parser~T~ {
        <<trait>>
        +parse(input: String): Option[ParseResult[T]]
        +map[U](f: T => U): Parser[U]
        +flatMap[U](f: T => Parser[U]): Parser[U]
        +~[U](that: Parser[U]): Parser[(T, U)]
        +|[U](that: Parser[U]): Parser[U]
    }

    class ParseResult~T~ {
        +value: T
        +remaining: String
    }

    class ParserObject {
        +char(c: Char): Parser[Char]
        +string(s: String): Parser[String]
        +regex(pattern: String): Parser[String]
        +digit: Parser[Int]
        +digits: Parser[Int]
    }

    class Endereco {
        +logradouro: String
        +numero: Int
        +cidade: String
        +estado: String
        +cep: String
    }

    class EnderecoParser {
        +logradouro: Parser[String]
        +numero: Parser[Int]
        +cidade: Parser[String]
        +estado: Parser[String]
        +cep: Parser[String]
        +endereco: Parser[Endereco]
    }

    Parser~T~ --> ParseResult~T~
    ParserObject ..> Parser~T~
    EnderecoParser ..> Parser~T~
    EnderecoParser --> Endereco
```

---

## **Setup Instructions**

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/parser-combinators
```

### 2 - Compile & Run the Application

```bash
sbt compile run
```

### 3 - Run Tests

```bash
sbt test
```
