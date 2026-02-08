package com.rbleggi.logisticpricing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreightCalculatorTest {

    @Test
    @DisplayName("TruckPricingStrategy should calculate price within range")
    void truckPricingStrategy_calculatesPrice() {
        var strategy = new TruckPricingStrategy();
        var info = new FreightInfo(10, 5, 100, TransportType.TRUCK);
        double price = strategy.calculate(info);
        assertTrue(price > 0);
        assertTrue(price < 500);
    }

    @Test
    @DisplayName("RailPricingStrategy should calculate price within range")
    void railPricingStrategy_calculatesPrice() {
        var strategy = new RailPricingStrategy();
        var info = new FreightInfo(20, 8, 300, TransportType.RAIL);
        double price = strategy.calculate(info);
        assertTrue(price > 0);
        assertTrue(price < 1000);
    }

    @Test
    @DisplayName("BoatPricingStrategy should calculate price within range")
    void boatPricingStrategy_calculatesPrice() {
        var strategy = new BoatPricingStrategy();
        var info = new FreightInfo(15, 6, 500, TransportType.BOAT);
        double price = strategy.calculate(info);
        assertTrue(price > 0);
        assertTrue(price < 1000);
    }

    @Test
    @DisplayName("PricingStrategySelector should return TruckPricingStrategy for TRUCK")
    void pricingStrategySelector_truckType_returnsTruckStrategy() {
        var strategy = PricingStrategySelector.forTransportType(TransportType.TRUCK);
        assertInstanceOf(TruckPricingStrategy.class, strategy);
    }

    @Test
    @DisplayName("PricingStrategySelector should return RailPricingStrategy for RAIL")
    void pricingStrategySelector_railType_returnsRailStrategy() {
        var strategy = PricingStrategySelector.forTransportType(TransportType.RAIL);
        assertInstanceOf(RailPricingStrategy.class, strategy);
    }

    @Test
    @DisplayName("PricingStrategySelector should return BoatPricingStrategy for BOAT")
    void pricingStrategySelector_boatType_returnsBoatStrategy() {
        var strategy = PricingStrategySelector.forTransportType(TransportType.BOAT);
        assertInstanceOf(BoatPricingStrategy.class, strategy);
    }

    @Test
    @DisplayName("FreightCalculator should calculate using provided strategy")
    void freightCalculator_withStrategy_calculatesCorrectly() {
        var strategy = new TruckPricingStrategy();
        var calculator = new FreightCalculator(strategy);
        var info = new FreightInfo(10, 5, 100, TransportType.TRUCK);
        double price = calculator.calculate(info);
        assertTrue(price > 0);
    }

    @Test
    @DisplayName("FreightInfo should hold transport information")
    void freightInfo_holdsCorrectValues() {
        var info = new FreightInfo(10, 5, 100, TransportType.TRUCK);
        assertEquals(10, info.volume());
        assertEquals(5, info.size());
        assertEquals(100, info.distance());
        assertEquals(TransportType.TRUCK, info.transportType());
    }
}
