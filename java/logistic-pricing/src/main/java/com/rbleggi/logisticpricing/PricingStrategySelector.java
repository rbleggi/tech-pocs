package com.rbleggi.logisticpricing;

public class PricingStrategySelector {
    public static PricingStrategy forTransportType(TransportType type) {
        return switch (type) {
            case TRUCK -> new TruckPricingStrategy();
            case RAIL -> new RailPricingStrategy();
            case BOAT -> new BoatPricingStrategy();
        };
    }
}
