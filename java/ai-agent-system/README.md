# **AI Agent System**

## Overview

Multi-agent AI system demonstrating the **Strategy Pattern** with specialized agents for research, writing, and reviewing tasks using message-passing communication without actual AI integration.

---

## Tech Stack

- **Java 25** → Latest JDK with modern language features including records and sealed interfaces.
- **Gradle** → Build automation and dependency management.
- **JUnit 5** → Testing framework for unit tests.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Task {
        +id: String
        +description: String
        +type: String
    }

    class Message {
        +from: String
        +to: String
        +content: String
        +timestamp: long
    }

    class AgentResult {
        +agentName: String
        +taskId: String
        +result: String
        +sentMessages: List~Message~
    }

    class Agent {
        <<sealed interface>>
        +process(task: Task, inbox: List~Message~): AgentResult
        +getName(): String
    }

    class ResearcherAgent {
        -knowledgeBase: Map~String, String~
        +process(task: Task, inbox: List~Message~): AgentResult
        +getName(): String
    }

    class WriterAgent {
        +process(task: Task, inbox: List~Message~): AgentResult
        +getName(): String
    }

    class ReviewerAgent {
        +process(task: Task, inbox: List~Message~): AgentResult
        +getName(): String
    }

    class AIAgentSystem {
        -agents: Map~String, Agent~
        -messageQueue: List~Message~
        +executeWorkflow(task: Task, agentOrder: List~String~): List~AgentResult~
        +getMessageStats(): Map~String, Long~
        +getMessageHistory(): List~Message~
    }

    Agent <|.. ResearcherAgent
    Agent <|.. WriterAgent
    Agent <|.. ReviewerAgent
    AIAgentSystem --> Agent
    Agent --> AgentResult
    Agent --> Task
    Agent --> Message
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/ai-agent-system
```

### 2 - Compile & Run the Application
```bash
./gradlew run
```

### 3 - Run Tests
```bash
./gradlew test
```
