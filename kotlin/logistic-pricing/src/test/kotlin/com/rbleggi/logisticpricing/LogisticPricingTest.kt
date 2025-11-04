package com.rbleggi.logisticpricing

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class LogisticPricingTest {
    @Test
    fun testTruckPricing() {
        val info = FreightInfo(10.0, 5.0, 100.0, TransportType.TRUCK)
        val strategy = TruckPricingStrategy()
        val price = strategy.calculate(info)
        assertTrue(price > 0)
    }

    @Test
    fun testRailPricing() {
        val info = FreightInfo(20.0, 8.0, 300.0, TransportType.RAIL)
        val strategy = RailPricingStrategy()
        val price = strategy.calculate(info)
        assertTrue(price > 0)
    }

    @Test
    fun testBoatPricing() {
        val info = FreightInfo(15.0, 6.0, 500.0, TransportType.BOAT)
        val strategy = BoatPricingStrategy()
        val price = strategy.calculate(info)
        assertTrue(price > 0)
    }
}
