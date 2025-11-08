# **OAuth 2.0 Server (Kotlin)**

## **Overview**

This project demonstrates a minimal **OAuth 2.0 Server** in **Kotlin** using the **Factory Pattern** to handle extensible grant types. The solution allows easy addition of new OAuth 2.0 flows and contains all logic in a single file.

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build tool for JVM projects.
- **JDK 21** → Required to run the application.

---

## **Features**

- **OAuth 2.0 Grant Handling** → Supports Authorization Code and Client Credentials grants.
- **Factory Pattern** → Extensible design for grant type handling.
- **Single File Implementation** → All logic is contained in one file for simplicity.
- **Type Safety** → Leverages Kotlin's null safety and type system.

---

## **Architecture Diagram**

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

## **Implementation Details**

- The solution uses the **Factory Pattern**: the factory returns the correct handler for each grant type.
- The main entry point demonstrates grant handling for different OAuth 2.0 flows.
- All logic is in a single Kotlin file.
- To test, run the application and observe the output for each grant type.

---

## **Setup Instructions**

### **1 - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/oauth2
```

### **2 - Compile & Run the Application**

```shell
./gradlew build
./gradlew run
```

### **3 - Run Tests**

```shell
./gradlew test
```