package com.rbleggi.logisticpricing;

public class FreightCalculator {
    private final PricingStrategy strategy;

    public FreightCalculator(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(FreightInfo info) {
        return strategy.calculate(info);
    }
}
