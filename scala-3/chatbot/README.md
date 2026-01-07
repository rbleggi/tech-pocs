# AI Chatbot

## Overview

This project implements an AI chatbot using the Command Pattern with intent recognition, entity extraction, and conversation context management. The chatbot supports multiple intents and maintains conversation state across interactions.

---

## Tech Stack

- **Scala 3** → Modern JVM-based language with advanced type safety and functional programming features.
- **SBT** → Scala's official build tool.
- **JDK 25** → Required to run the application.

---

## Command Pattern

The **Command Pattern** encapsulates each chatbot intent as a command object. This allows:

- Each intent (greeting, weather, time, etc.) is implemented as a separate `Command`
- Easy extension with new intents without modifying existing code
- Clean separation between intent matching and execution
- Each command manages its own pattern matching and entity extraction
- Simple to test each intent independently

---

## Implementation Details

### Intent Recognition
- Pattern matching using keywords and regular expressions
- First matching command in the list handles the input
- Fallback to `UnknownCommand` if no match found

### Entity Extraction
- **Name Extraction**: Patterns like "my name is X", "I'm X", "call me X"
- **City Extraction**: Patterns like "in X", "at X", "for X"
- **Task Extraction**: Patterns like "remind me to X"

### Context Management
- Stores user name across conversation
- Maintains message history
- Persists extracted entities (city, reminders, etc.)
- Can be queried and reset

### Available Commands
1. **GreetingCommand** - Handles greetings and name extraction
2. **WeatherCommand** - Provides weather information (simulated)
3. **TimeCommand** - Returns current time
4. **ReminderCommand** - Creates reminders with task extraction
5. **HelpCommand** - Lists available capabilities
6. **ContextCommand** - Shows current conversation context
7. **UnknownCommand** - Fallback for unrecognized inputs

---

## Setup Instructions

### 1️ - Clone the Repository

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/chatbot
```

### 2️ - Compile & Run the Application

```shell
sbt compile run
```

### 3️ - Run Tests

```shell
sbt test
```
