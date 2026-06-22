package com.rbleggi.logisticpricing.service.decorator

import com.rbleggi.logisticpricing.model.FreightInfo
import com.rbleggi.logisticpricing.model.TransportType
import com.rbleggi.logisticpricing.service.pricing.FreightPricing
import kotlin.random.Random
import kotlin.test.*

class PricingDecoratorTest {

    private val info = FreightInfo(10.0, 5.0, 500.0, TransportType.TRUCK)
    private val base = object : FreightPricing {
        override fun calculate(info: FreightInfo): Double = 100.0
    }

    @Test
    fun `fuel surcharge multiplies the price by a percentage`() {
        assertEquals(112.0, FuelSurcharge(base, 12.0).calculate(info), 0.0001)
    }

    @Test
    fun `insurance adds a flat fee to the price`() {
        assertEquals(130.0, Insurance(base, 30.0).calculate(info), 0.0001)
    }

    @Test
    fun `distance discount applies only beyond the threshold`() {
        assertEquals(95.0, DistanceDiscount(base, threshold = 300.0, percent = 5.0).calculate(info), 0.0001)
        val shortTrip = info.copy(distance = 100.0)
        assertEquals(100.0, DistanceDiscount(base, threshold = 300.0, percent = 5.0).calculate(shortTrip), 0.0001)
    }

    @Test
    fun `dynamic variation stays within the spread bounds`() {
        val price = DynamicVariation(base, spread = 0.1, random = Random(42)).calculate(info)
        assertTrue(price in 90.0..110.0)
    }

    @Test
    fun `dynamic variation with the same seed is reproducible`() {
        assertEquals(
            DynamicVariation(base, spread = 0.1, random = Random(42)).calculate(info),
            DynamicVariation(base, spread = 0.1, random = Random(42)).calculate(info),
            0.0001
        )
    }

    @Test
    fun `decorators stack in wrapping order`() {
        val stacked = FuelSurcharge(Insurance(base, 30.0), 12.0)
        assertEquals(145.6, stacked.calculate(info), 0.0001)
    }
}
