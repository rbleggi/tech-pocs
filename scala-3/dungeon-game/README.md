# **Dungeon Game**

## **Overview**

This project calculates the minimum initial health required for a knight to rescue a princess in a dungeon.

### **Tech Stack**

- **Scala 3** → Functional and object-oriented programming.
- **SBT** → Build tool for Scala.
- **JDK 21** → Required to run the application.

---

## **Features**

- Calculates the minimum health required using a bottom-up approach.
- Includes predefined test cases for validation.

---

## **Example Usage**

### **Input Dungeons**

#### Dungeon 1

```scala
val dungeon = Array(
  Array(-2, -3, 3),
  Array(-5, -10, 1),
  Array(10, 30, -5)
)
```

#### Dungeon 2

```scala
val dungeon = Array(
  Array(-2, -3, 3, 5),
  Array(-5, -10, 1, 9),
  Array(10, 30, -5, 7)
)
```

#### Dungeon 3

```scala
val dungeon = Array(Array(0))
```

### **Output**

```shell
Minimum initial health needed: 7
Minimum initial health needed: 6
Minimum initial health needed: 1
```

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/dungeon-game
```

### **2️ - Compile & Run the Application**

```shell
./sbtw compile run
```

### **3️ - Run Tests**

```shell
./sbtw test
```
