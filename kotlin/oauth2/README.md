# **OAuth 2.0 Server (Kotlin)**

## Overview

This project demonstrates a minimal OAuth 2.0 Server in Kotlin to handle extensible grant types. The solution allows easy addition of new OAuth 2.0 flows and contains all logic in a single file.

---

## Tech Stack

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build tool for JVM projects.
- **JDK 25** → Required to run the application.

---

## Architecture Diagram

```mermaid
classDiagram
    direction TB

    class GrantHandler {
        <<interface>>
        +handle(request: Map~String, String~): Map~String, String~
    }

    class AuthorizationCodeHandler {
        +handle(request: Map~String, String~): Map~String, String~
    }

    class ClientCredentialsHandler {
        +handle(request: Map~String, String~): Map~String, String~
    }

    class GrantHandlerFactory {
        +getHandler(grantType: String): GrantHandler?
    }

    class OAuth2Server {
        +processRequest(params: Map~String, String~): Map~String, String~
    }

    GrantHandlerFactory --> GrantHandler
    AuthorizationCodeHandler ..|> GrantHandler
    ClientCredentialsHandler ..|> GrantHandler
    OAuth2Server --> GrantHandlerFactory
```

---

## Setup Instructions

### 1 - Clone the Repository

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/oauth2
```

### 2 - Compile & Run the Application

```bash
./gradlew build run
```

### 3 - Run Tests

```bash
./gradlew test
```