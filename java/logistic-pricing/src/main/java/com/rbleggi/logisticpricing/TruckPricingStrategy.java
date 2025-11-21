package com.rbleggi.logisticpricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class TruckPricingStrategy implements PricingStrategy {
    private static final Random RANDOM = new Random();

    @Override
    public double calculate(FreightInfo info) {
        double base = 1.5;
        double price = (info.volume() * 0.8 + info.size() * 0.5 + info.distance() * base) * getDynamicFactor();
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static double getDynamicFactor() {
        return 1.0 + RANDOM.nextDouble(-0.1, 0.1);
    }
}
