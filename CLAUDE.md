# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

Multi-language Proof of Concept repository with 100+ self-contained projects demonstrating design patterns and architectures across Kotlin, Scala 3, Java, Python, OCaml, and DevOps tooling.

## Platform Environment

**This repository runs on a Windows machine.** All bash commands should be executed using **cmd** or **PowerShell**. When providing command examples, prefer PowerShell syntax for Windows compatibility.

## Build Commands by Language

### Kotlin (`kotlin/`)
```bash
cd kotlin/<project>
./gradlew build
./gradlew test
./gradlew run
```

### Scala 3 (`scala-3/`)
```bash
cd scala-3/<project>
sbt compile
sbt run
sbt test
```

**Note:** On Windows, use PowerShell commands:
```bash
powershell.exe -Command "cd 'C:\path\to\scala-3\<project>'; sbt test"
```

### Java (`java/`)
```bash
cd java/<project>
./gradlew build
./gradlew test
./gradlew run
```

### Python (`python/movie-recommender/`)
```bash
python3 -m venv venv
source venv/bin/activate  # Unix
venv\Scripts\activate     # Windows
pip install -r requirements.txt
python main.py
```

### OCaml (`ocaml/bitonic-sequence/`)
```bash
opam install . --deps-only --with-test
opam exec -- dune build
opam exec -- dune runtest
opam exec -- dune exec ./bin/main.exe
```

## Running Single Tests

**Kotlin/Java:**
```bash
./gradlew test --tests "ClassName.methodName"
```

**Scala 3:**
```bash
sbt "testOnly *ClassName"
```

## Project Conventions

- JVM projects target JDK 25
- Kotlin/Java use JUnit 5; Scala uses ScalaTest 3.2.16
- Each project has its own README with architecture diagrams (Mermaid)
- Projects are single-file POCs when possible
- Same features often implemented in Kotlin, Scala 3, and Java for comparison
- Design patterns from Gang of Four: Builder, Strategy, Command, Observer, Factory, Adapter, Decorator, Template Method
- Before creating a new project, review similar projects in the same language directory and follow their patterns

## Code Style

- Do not add comments unless explicitly requested
- Do not use emojis in code or README files
- Follow existing style and structure in the repository
- Edit existing files rather than deleting and rewriting
- Always verify code compiles and tests pass before completing tasks
- Single-file implementations preferred for POCs

## Key Directories

- `kotlin/`, `scala-3/`, `java/` - Main POC implementations
- `devops/` - Infrastructure projects (Kubernetes, Helm, OpenTofu, Jenkins)
- `archtecture/` - Architecture documentation (Flink, voting systems)
- `scala-study/` - Scala learning materials
- `python/` - Python-specific projects
- `ocaml/` - OCaml projects
- `prompt-ai/` - AI-powered tools and prompts
- `secondbrain/` - Personal knowledge base (Obsidian notes)

## Common POC Patterns

Most POCs follow this structure:
- Main implementation in `src/main/<language>/<package>/main.<ext>` (single-file when possible)
- Tests in `src/test/<language>/`
- README with problem description and Mermaid architecture diagram
- Build file (`build.gradle.kts`, `build.sbt`, etc.)

## Testing Philosophy

- Tests verify behavior, not implementation details
- Test names clearly describe what they test (e.g., "DefaultTaxSpecification - electronics in MG 2024")
- Each specification/strategy pattern gets its own test coverage
- Exception cases are explicitly tested

## Git Commit Guidelines

**IMPORTANT:** When asked to create commits, always create **minimum 10 commits per day**.

**Backdating commits on Windows (PowerShell):**
```powershell
$env:GIT_AUTHOR_DATE="2026-02-04 14:32:23"
$env:GIT_COMMITTER_DATE="2026-02-04 14:32:23"
git commit -m "commit message"
```

This sets both author and committer dates, ensuring commits appear correctly in git log.

**Commit message style:**
- Keep messages simple and descriptive
- Never include co-author unless explicitly requested
- Use lowercase and imperative mood (e.g., "add feature" not "Added feature")
