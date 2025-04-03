# **TAX System Project**

## **Overview**

This project demonstrates a **dynamic tax calculation system** where different products are subject to **varying tax rates** based on **state and year**. The system follows the **Specification Pattern**, ensuring that tax rules are **modular, extensible, and easily adaptable** to changes.

### **Tech Stack**
- **Scala 3.6** → A powerful JVM-based language with strong functional programming capabilities.
- **SBT** → Build tool for Scala and Java projects.
- **JDK 21** → Required for running the application.

---

## **Features**
✔ **Product-based Taxation** → Different products have distinct tax rates.  
✔ **State-Specific Taxes** → Each state defines its own tax rules.  
✔ **Yearly Tax Updates** → Tax rates change depending on the year.  
✔ **Specification Pattern** → Decouples tax rules from business logic.  
✔ **Encapsulation** → Each tax rule is **self-contained**, making the system easy to extend.

---

## **Class Diagram**

The diagram below represents the core structure of the **TAX system**:

```mermaid
classDiagram
   direction TB

   class Product {
      - String name
      - String category
   }

   class TaxConfiguration {
      - String state
      - Int year
      - Map<String, Double> rates
   }

   class TaxSpecification {
      <<interface>>
      +isSatisfiedBy(state: String, year: Int): Boolean
      +calculateTax(product: Product, price: Double): Double
   }

   class DefaultTaxSpecification {
      - TaxConfiguration config
      +isSatisfiedBy(state: String, year: Int): Boolean
      +calculateTax(product: Product, price: Double): Double
   }

   class TaxCalculator {
      - List<TaxSpecification> specifications
      +calculateTax(state: String, year: Int, product: Product, price: Double): Double
   }

   TaxSpecification <|-- DefaultTaxSpecification
   TaxCalculator --> TaxSpecification : uses
   TaxCalculator --> Product : uses
   DefaultTaxSpecification --> TaxConfiguration : contains
```

---

## **Specification Pattern**
The **Specification Pattern** is used to **encapsulate business rules** for tax calculations. Instead of hardcoding tax logic in a central place, the system:
1. **Uses `TaxSpecification` as an interface** defining the tax contract.
2. **Implements `DefaultTaxSpecification`** for dynamically applying the correct tax rule.
3. **Delegates tax calculation responsibility to each specification**, ensuring that **business rules remain encapsulated**.

This design **keeps the system modular, reusable, and easy to extend**.

---

## **Setup Instructions**

### **1️ - Clone the Repository**
```shell
git clone https://github.com/rbleggi/tech-pocs.git
cd scala-3/tax-system
```

### **2️ - Compile & Run the Application**
```shell
./sbtw compile run
```

### **3️ - Run Tests**
```shell
./sbtw compile test
```