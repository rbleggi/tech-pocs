package com.rbleggi.logisticpricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logistic Pricing");
    }
}

enum TransportType {
    TRUCK, RAIL, BOAT
}

record FreightInfo(double volume, double size, double distance, TransportType transportType) {}

interface PricingStrategy {
    double calculate(FreightInfo info);
}

class TruckPricingStrategy implements PricingStrategy {
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

class RailPricingStrategy implements PricingStrategy {
    private static final Random RANDOM = new Random();

    @Override
    public double calculate(FreightInfo info) {
        double base = 1.2;
        double price = (info.volume() * 0.6 + info.size() * 0.4 + info.distance() * base) * getDynamicFactor();
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static double getDynamicFactor() {
        return 1.0 + RANDOM.nextDouble(-0.15, 0.15);
    }
}

class BoatPricingStrategy implements PricingStrategy {
    private static final Random RANDOM = new Random();

    @Override
    public double calculate(FreightInfo info) {
        double base = 1.0;
        double price = (info.volume() * 0.4 + info.size() * 0.3 + info.distance() * base) * getDynamicFactor();
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static double getDynamicFactor() {
        return 1.0 + RANDOM.nextDouble(-0.2, 0.2);
    }
}

class PricingStrategySelector {
    static PricingStrategy forTransportType(TransportType type) {
        return switch (type) {
            case TRUCK -> new TruckPricingStrategy();
            case RAIL -> new RailPricingStrategy();
            case BOAT -> new BoatPricingStrategy();
        };
    }
}

class FreightCalculator {
    private final PricingStrategy strategy;

    FreightCalculator(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(FreightInfo info) {
        return strategy.calculate(info);
    }
}
