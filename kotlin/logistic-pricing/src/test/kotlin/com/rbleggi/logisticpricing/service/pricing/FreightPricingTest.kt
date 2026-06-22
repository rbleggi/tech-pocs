package com.rbleggi.logisticpricing.service.pricing

import com.rbleggi.logisticpricing.model.FreightInfo
import com.rbleggi.logisticpricing.model.TransportType
import kotlin.test.*

class FreightPricingTest {

    private val info = FreightInfo(10.0, 5.0, 100.0, TransportType.TRUCK)

    @Test
    fun `truck base price sums the weighted factors`() {
        assertEquals(160.5, TruckBasePricing().calculate(info), 0.0001)
    }

    @Test
    fun `rail base price sums the weighted factors`() {
        assertEquals(128.0, RailBasePricing().calculate(info), 0.0001)
    }

    @Test
    fun `boat base price sums the weighted factors`() {
        assertEquals(105.5, BoatBasePricing().calculate(info), 0.0001)
    }
}
