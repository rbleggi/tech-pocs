# **Validation Framework**

## **Overview**

This project implements a simple and extensible validation framework using the Strategy Pattern in Kotlin. It allows developers to define reusable validators and compose them to validate data class fields.

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with advanced type safety and functional programming features.
- **Gradle** → Kotlin's official build tool.
- **JDK 21** → Required to run the application.

---

## **Features**

- **Composable Validators** → Combine multiple validators for a single field.
- **Strategy Pattern** → Easily add new validation strategies by implementing the Validator interface.
- **Manual Composition** → Explicitly compose validators for each data class.
- **Extensible** → Add new validators without changing the framework core.

---

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB

    class Validator~T~ {
        <<interface>>
        +validate(value: T): List[String]
    }

    class User {
        -name: String?
        -email: String?
    }

    class Validators {
        +notNullString: Validator[String?]
        +minLength(min: Int): Validator[String?]
        +all[T](validators: Validator[T]*): Validator[T]
    }

    class UserValidator {
        +validate(user: User): List[String]
    }

    Validator~T~ <|.. Validators
    Validator~T~ <|.. UserValidator
    User --> UserValidator
```

---

## **Implementation Details**

Validation is performed using the `Validator<T>` interface, which defines the method `validate(value: T): List<String>`. Concrete implementations of this interface are provided for specific types, such as `String?` and `User`.

- The `Validators` object provides reusable validators like `notNullString` and `minLength`, as well as an `all` method to combine multiple validators.
- To validate a composite type (such as `User`), an implementation of `Validator<User>` is created that uses the appropriate field validators.
- There is no use of annotations, reflection, or dynamic strategy mapping. All validator composition is done manually and explicitly in the code.
- To validate an object, simply instantiate the appropriate validator and call its `validate` method.

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/validation-framework
```

### **2️ - Compile & Run the Application**

```shell
./gradlew build run
```

### **3️ - Run Tests**

```shell
./gradlew test
```
