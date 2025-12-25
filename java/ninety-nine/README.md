# **Ninety-Nine Problems**

## **Overview**

This project implements solutions to the classic "Ninety-Nine Problems" in Java. These problems are a collection of algorithmic challenges designed to improve functional programming skills. Each problem is implemented as a separate class with a main method for demonstration.

---

## **Tech Stack**

- **Java 21** → Modern Java with functional programming features.
- **Gradle** → Official build tool for Java projects.
- **JUnit 5** → Testing framework.

---

## **Features**

- **Multiple Problems** → Each problem has its own class (P01, P02, P03, etc.).
- **Recursive Solutions** → Problems are solved using recursion.
- **Tests** → JUnit tests verify the correctness of each solution.

---

## **Implemented Problems**

- **P01** → Find the last element of a list
- **P02** → Find the penultimate (last but one) element of a list
- **P03** → Find the Kth element of a list

---

## **Setup Instructions**

### **1 - Clone the Repository**

```bash
git clone https://github.com/rbleggi/tech-pocs.git
cd java/ninety-nine
```

### **2 - Run a Specific Problem**

```bash
./gradlew run
```

Or run individual problems:

```bash
java -cp build/classes/java/main com.rbleggi.ninetynine.P01
java -cp build/classes/java/main com.rbleggi.ninetynine.P02
java -cp build/classes/java/main com.rbleggi.ninetynine.P03
```

### **3 - Run the Tests**

```bash
./gradlew test
```
