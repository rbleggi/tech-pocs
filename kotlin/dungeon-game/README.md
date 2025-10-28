# Dungeon Game Project

## Overview

This project solves the **Dungeon Game** problem using **Dynamic Programming**. Given a 2D grid representing a dungeon where:
- Negative values represent damage
- Positive values represent healing
- The goal is to find the minimum initial health needed to reach from top-left to bottom-right

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Kotlin's build tool for JVM projects.
- **JDK 21** → Required to run the application.

---

## **Features**

- Calculates the minimum health required using a bottom-up approach.
- Includes predefined test cases for validation.

---

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/rbleggi/tech-pocs.git
   cd kotlin/dungeon-game
   ```

2. **Compiling & Running**:
   ```bash
   ./gradlew build run
   ```

3. **Tests**:
   ```sh
   ./gradlew test
   ```
