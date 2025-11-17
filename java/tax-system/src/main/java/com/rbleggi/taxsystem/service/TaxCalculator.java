package com.rbleggi.taxsystem.service;

import com.rbleggi.taxsystem.model.Product;
import com.rbleggi.taxsystem.model.TaxConfiguration;
import com.rbleggi.taxsystem.specification.DefaultTaxSpecification;
import com.rbleggi.taxsystem.specification.TaxSpecification;

import java.util.List;
import java.util.Map;

public class TaxCalculator {
    public double calculateTax(String state, int year, Product product, double price) {
        List<TaxSpecification> specifications = List.of(
            new DefaultTaxSpecification(new TaxConfiguration("SP", 2024, Map.of("electronics", 0.18, "food", 0.07, "book", 0.00))),
            new DefaultTaxSpecification(new TaxConfiguration("RJ", 2024, Map.of("electronics", 0.20, "food", 0.08, "book", 0.02))),
            new DefaultTaxSpecification(new TaxConfiguration("MG", 2024, Map.of("electronics", 0.15, "food", 0.05, "book", 0.01))),
            new DefaultTaxSpecification(new TaxConfiguration("SP", 2025, Map.of("electronics", 0.19, "food", 0.06, "book", 0.00)))
        );

        return specifications.stream()
            .filter(spec -> spec.isSatisfiedBy(state, year))
            .findFirst()
            .map(spec -> spec.calculateTax(product, price))
            .orElseThrow(() -> new RuntimeException("No tax rule found for " + state + " in the year " + year));
    }
}
