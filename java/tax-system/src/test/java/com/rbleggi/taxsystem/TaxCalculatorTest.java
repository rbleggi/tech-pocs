package com.rbleggi.taxsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorTest {
    private TaxCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TaxCalculator();
    }

    @Test
    @DisplayName("SP 2024 electronics should apply 18% tax rate")
    void calculateTax_electronicsInSP2024_applies18Percent() {
        var product = new Product("Smartphone", "electronics");
        double price = 1000.0;
        double tax = calculator.calculateTax("SP", 2024, product, price);
        assertEquals(180.0, tax, 0.01);
    }

    @Test
    @DisplayName("SP 2024 food should apply 7% tax rate")
    void calculateTax_foodInSP2024_applies7Percent() {
        var product = new Product("Rice", "food");
        double price = 100.0;
        double tax = calculator.calculateTax("SP", 2024, product, price);
        assertEquals(7.0, tax, 0.01);
    }

    @Test
    @DisplayName("SP 2024 book should apply 0% tax rate")
    void calculateTax_bookInSP2024_applies0Percent() {
        var product = new Product("Java Book", "book");
        double price = 50.0;
        double tax = calculator.calculateTax("SP", 2024, product, price);
        assertEquals(0.0, tax, 0.01);
    }

    @Test
    @DisplayName("RJ 2024 electronics should apply 20% tax rate")
    void calculateTax_electronicsInRJ2024_applies20Percent() {
        var product = new Product("Laptop", "electronics");
        double price = 2000.0;
        double tax = calculator.calculateTax("RJ", 2024, product, price);
        assertEquals(400.0, tax, 0.01);
    }

    @Test
    @DisplayName("RJ 2024 food should apply 8% tax rate")
    void calculateTax_foodInRJ2024_applies8Percent() {
        var product = new Product("Beans", "food");
        double price = 10.0;
        double tax = calculator.calculateTax("RJ", 2024, product, price);
        assertEquals(0.8, tax, 0.01);
    }

    @Test
    @DisplayName("RJ 2024 book should apply 2% tax rate")
    void calculateTax_bookInRJ2024_applies2Percent() {
        var product = new Product("History Book", "book");
        double price = 100.0;
        double tax = calculator.calculateTax("RJ", 2024, product, price);
        assertEquals(2.0, tax, 0.01);
    }

    @Test
    @DisplayName("MG 2024 electronics should apply 15% tax rate")
    void calculateTax_electronicsInMG2024_applies15Percent() {
        var product = new Product("Phone", "electronics");
        double price = 500.0;
        double tax = calculator.calculateTax("MG", 2024, product, price);
        assertEquals(75.0, tax, 0.01);
    }

    @Test
    @DisplayName("MG 2024 food should apply 5% tax rate")
    void calculateTax_foodInMG2024_applies5Percent() {
        var product = new Product("Milk", "food");
        double price = 20.0;
        double tax = calculator.calculateTax("MG", 2024, product, price);
        assertEquals(1.0, tax, 0.01);
    }

    @Test
    @DisplayName("MG 2024 book should apply 1% tax rate")
    void calculateTax_bookInMG2024_applies1Percent() {
        var product = new Product("Science Book", "book");
        double price = 80.0;
        double tax = calculator.calculateTax("MG", 2024, product, price);
        assertEquals(0.8, tax, 0.01);
    }

    @Test
    @DisplayName("SP 2025 electronics should apply 19% tax rate")
    void calculateTax_electronicsInSP2025_applies19Percent() {
        var product = new Product("TV", "electronics");
        double price = 3000.0;
        double tax = calculator.calculateTax("SP", 2025, product, price);
        assertEquals(570.0, tax, 0.01);
    }

    @Test
    @DisplayName("SP 2025 food should apply 6% tax rate")
    void calculateTax_foodInSP2025_applies6Percent() {
        var product = new Product("Bread", "food");
        double price = 5.0;
        double tax = calculator.calculateTax("SP", 2025, product, price);
        assertEquals(0.3, tax, 0.01);
    }

    @Test
    @DisplayName("SP 2025 book should apply 0% tax rate")
    void calculateTax_bookInSP2025_applies0Percent() {
        var product = new Product("Novel", "book");
        double price = 30.0;
        double tax = calculator.calculateTax("SP", 2025, product, price);
        assertEquals(0.0, tax, 0.01);
    }

    @Test
    @DisplayName("Unknown state should throw RuntimeException")
    void calculateTax_unknownState_throwsException() {
        var product = new Product("Item", "electronics");
        assertThrows(RuntimeException.class, () ->
            calculator.calculateTax("UNKNOWN", 2024, product, 100.0));
    }

    @Test
    @DisplayName("Unknown year should throw RuntimeException")
    void calculateTax_unknownYear_throwsException() {
        var product = new Product("Item", "electronics");
        assertThrows(RuntimeException.class, () ->
            calculator.calculateTax("SP", 2030, product, 100.0));
    }
}
