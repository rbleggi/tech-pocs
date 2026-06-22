package com.rbleggi.logisticpricing.service

import com.rbleggi.logisticpricing.model.FreightInfo
import com.rbleggi.logisticpricing.model.TransportType
import com.rbleggi.logisticpricing.service.decorator.FuelSurcharge
import kotlin.test.*

class LogisticPricingServiceTest {

    private val service = LogisticPricingService()

    @Test
    fun `service prices every transport type by base strategy`() {
        TransportType.entries.forEach { type ->
            val info = FreightInfo(10.0, 5.0, 100.0, type)
            assertTrue(service.basePrice(info) > 0)
        }
    }

    @Test
    fun `service quote rounds a decorated price to two decimals`() {
        val info = FreightInfo(10.0, 5.0, 100.0, TransportType.TRUCK)
        val decorated = FuelSurcharge(service.baseFor(TransportType.TRUCK), 12.0)
        assertEquals(179.76, service.quote(info, decorated), 0.0001)
    }
}
