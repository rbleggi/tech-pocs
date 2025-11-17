package com.rbleggi.taxsystem;

import com.rbleggi.taxsystem.model.Product;
import com.rbleggi.taxsystem.service.TaxCalculator;

public class Main {
    public static void main(String[] args) {
        var product1 = new Product("Smartphone", "electronics");
        var product2 = new Product("Rice", "food");
        var product3 = new Product("History Book", "book");

        var calculator = new TaxCalculator();

        double priceProduct1 = 2500.0;
        double priceProduct2 = 20.0;
        double priceProduct3 = 50.0;

        System.out.printf("Tax for %s in SP (2024): R$ %.2f%n",
            product1.name(), calculator.calculateTax("SP", 2024, product1, priceProduct1));
        System.out.printf("Tax for %s in MG (2024): R$ %.2f%n",
            product2.name(), calculator.calculateTax("MG", 2024, product2, priceProduct2));
        System.out.printf("Tax for %s in RJ (2024): R$ %.2f%n",
            product3.name(), calculator.calculateTax("RJ", 2024, product3, priceProduct3));
        System.out.printf("Tax for %s in SP (2025): R$ %.2f%n",
            product1.name(), calculator.calculateTax("SP", 2025, product1, priceProduct1));
    }
}
