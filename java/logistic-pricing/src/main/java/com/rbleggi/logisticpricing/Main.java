package com.rbleggi.logisticpricing;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var shipments = List.of(
            new FreightInfo(10, 5, 100, TransportType.TRUCK),
            new FreightInfo(20, 8, 300, TransportType.RAIL),
            new FreightInfo(15, 6, 500, TransportType.BOAT)
        );

        for (var info : shipments) {
            var strategy = PricingStrategySelector.forTransportType(info.transportType());
            var calculator = new FreightCalculator(strategy);
            double price = calculator.calculate(info);
            System.out.println("Freight using " + info.transportType() + " costs $" + price);
        }
    }
}
