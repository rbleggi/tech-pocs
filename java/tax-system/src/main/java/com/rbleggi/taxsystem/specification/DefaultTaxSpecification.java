package com.rbleggi.taxsystem.specification;

import com.rbleggi.taxsystem.model.Product;
import com.rbleggi.taxsystem.model.TaxConfiguration;

public class DefaultTaxSpecification implements TaxSpecification {
    private final TaxConfiguration config;

    public DefaultTaxSpecification(TaxConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean isSatisfiedBy(String state, int year) {
        return config.state().equals(state) && config.year() == year;
    }

    @Override
    public double calculateTax(Product product, double price) {
        return price * config.rates().getOrDefault(product.category(), 0.0);
    }
}
