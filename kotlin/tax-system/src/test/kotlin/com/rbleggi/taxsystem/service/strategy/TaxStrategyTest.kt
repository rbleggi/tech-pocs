package com.rbleggi.taxsystem.service.strategy

import kotlin.test.*

class TaxStrategyTest {
    @Test
    fun `PercentageTax applies a rate over the price`() {
        assertEquals(18.0, PercentageTax(18.0).compute(100.0), 0.01)
    }

    @Test
    fun `FlatTax ignores the price`() {
        assertEquals(50.0, FlatTax(50.0).compute(100.0), 0.01)
        assertEquals(50.0, FlatTax(50.0).compute(999.0), 0.01)
    }

    @Test
    fun `CappedTax limits the tax to a maximum`() {
        val strategy = CappedTax(20.0, 500.0)
        assertEquals(40.0, strategy.compute(200.0), 0.01)
        assertEquals(500.0, strategy.compute(10000.0), 0.01)
    }
}
