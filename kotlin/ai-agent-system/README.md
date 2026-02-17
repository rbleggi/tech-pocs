# **AI Agent System**

## Overview

Multi-agent system demonstrating agent collaboration with message passing. Three specialized agents: researcher, writer, and reviewer work together in a workflow to research topics, write articles, and review content.

---

## Tech Stack

- **Kotlin 2.1.10** → Modern JVM language with concise syntax and null safety
- **Gradle 9.3.0** → Build automation tool
- **JDK 25** → Required to run the application
- **kotlin.test** → Testing framework

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class Message {
        +from: String
        +to: String
        +content: String
        +timestamp: Long
    }

    class AgentResult {
        +agentName: String
        +output: String
        +messagesReceived: Int
    }

    class Agent {
        <<interface>>
        +name: String
        +process(input: String): String
        +receiveMessage(message: Message): void
        +getMessages(): List~Message~
    }

    class BaseAgent {
        <<abstract>>
        +name: String
        -messages: List~Message~
        +receiveMessage(message: Message): void
        +getMessages(): List~Message~
        #sendMessage(to: String, content: String, system: AIAgentSystem): void
    }

    class ResearcherAgent {
        -knowledge: Map~String, String~
        +process(input: String): String
    }

    class WriterAgent {
        +process(input: String): String
    }

    class ReviewerAgent {
        +process(input: String): String
    }

    class AIAgentSystem {
        -agents: Map~String, Agent~
        +deliverMessage(message: Message): void
        +runWorkflow(topic: String): Map~String, AgentResult~
        +getAgentMessages(agentName: String): List~Message~
    }

    Agent <|.. BaseAgent
    BaseAgent <|-- ResearcherAgent
    BaseAgent <|-- WriterAgent
    BaseAgent <|-- ReviewerAgent
    AIAgentSystem --> Agent
    Agent --> Message
    AIAgentSystem --> AgentResult
```

---

## Setup Instructions

### 1 - Clone the Repository
```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/ai-agent-system
```

### 2 - Build the Project
```bash
./gradlew build
```

### 3 - Run the Application
```bash
./gradlew run
```

### 4 - Run Tests
```bash
./gradlew test
```
