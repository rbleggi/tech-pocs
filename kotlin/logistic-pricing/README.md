# **Logistic Pricing**

## **Overview**

This project implements a **modular and extensible freight pricing system** using the **Strategy Pattern**. Freight costs are dynamically calculated based on factors like **volume, size, and transport type** — such as **Truck**, **Rail**, or **Boat** — with prices subject to **real-time variability**.

---

## **Tech Stack**

- **Kotlin** → Modern JVM-based language with concise syntax and strong type safety.
- **Gradle** → Build automation tool for Kotlin projects.
- **JDK 21** → Required to run the application.

---

## **Features**

- **Strategy-Based Price Calculation** → Easily plug in new pricing models
- **Supports Multiple Transport Types** → Truck, Rail, Boat
- **Dynamic Pricing** → Pricing logic is fully decoupled and runtime-driven
- **Extensible Design** → Add more transport strategies without touching core logic
- **Minimal Boilerplate** → No builder classes or verbose configuration

---

## **Architecture Diagram**

```mermaid
classDiagram
    direction TB

    class FreightInfo {
        +volume: Double
        +size: Double
        +distance: Double
        +transportType: TransportType
    }

    class TransportType {
        <<enum>>
        +TRUCK
        +RAIL
        +BOAT
    }

    class PricingStrategy {
        <<interface>>
        +calculate(info: FreightInfo): Double
    }

    class TruckPricingStrategy {
        +calculate(info: FreightInfo): Double
        -getDynamicFactor(): Double
    }

    class RailPricingStrategy {
        +calculate(info: FreightInfo): Double
        -getDynamicFactor(): Double
    }

    class BoatPricingStrategy {
        +calculate(info: FreightInfo): Double
        -getDynamicFactor(): Double
    }

    class FreightCalculator {
        -strategy: PricingStrategy
        +calculate(info: FreightInfo): Double
    }

    class PricingStrategySelector {
        +forTransportType(t: TransportType): PricingStrategy
    }

    PricingStrategy <|-- TruckPricingStrategy
    PricingStrategy <|-- RailPricingStrategy
    PricingStrategy <|-- BoatPricingStrategy
    FreightCalculator --> PricingStrategy: uses
    PricingStrategySelector --> PricingStrategy: creates
```

---

## **Strategy Pattern**

Using the **Strategy Pattern**, the pricing logic is abstracted away from the core calculator:

1. Each transport mode implements its own `PricingStrategy`.
2. `FreightCalculator` delegates calculation to the selected strategy.
3. Easily swap or update strategies at runtime.
4. No need to modify core logic when adding a new transport type or changing pricing logic.

---

## **Setup Instructions**

### **1️ - Clone the Repository**

```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd kotlin/logistic-pricing
```

### **2️ - Build & Run the Application**

```shell
./gradlew build
./gradlew run
```

### **3️ - Run Tests**

```shell
./gradlew test
```
