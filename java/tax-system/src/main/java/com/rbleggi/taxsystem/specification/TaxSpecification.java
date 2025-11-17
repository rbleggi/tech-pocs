package com.rbleggi.taxsystem.specification;

import com.rbleggi.taxsystem.model.Product;

public interface TaxSpecification {
    boolean isSatisfiedBy(String state, int year);
    double calculateTax(Product product, double price);
}
