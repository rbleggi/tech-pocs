# **Own String Implementation**

## **Overview**

This project demonstrates a custom String implementation in Kotlin. The solution uses the Decorator Pattern to extend and encapsulate string operations in a single class, providing methods like toArray, forEach, reverse, iterator, length, charAt, equals, isEmpty, replace, substring, trim, toJson, indexOf, and hashCode.

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with advanced type safety and functional programming features.
- **Gradle** → Kotlin's official build tool.
- **JDK 21** → Required to run the application.

---

## **Features**

- **Terminal Interaction** → Run and test all string methods in the terminal.
- **Decorator Pattern** → Extensible design for custom string operations.
- **Single File Implementation** → All logic is contained in one file for simplicity.

---

## **Implementation Details**

- The solution uses the Decorator Pattern: MyString wraps a standard String and adds custom methods.
- The main entry point is `fun main()`, which demonstrates all implemented methods.
- All logic is in a single Kotlin file, with no comments.
- To test, run the application and observe the output for each method.

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/own-string-impl
```

### **2️ - Compile & Run the Application**

```bash
./gradlew build run
```

### **3️ - Run Tests**

```bash
./gradlew test
```
