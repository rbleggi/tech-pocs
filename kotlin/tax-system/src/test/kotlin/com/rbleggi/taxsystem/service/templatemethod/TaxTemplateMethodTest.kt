package com.rbleggi.taxsystem.service.templatemethod

import com.rbleggi.taxsystem.service.strategy.CappedTax
import com.rbleggi.taxsystem.service.strategy.FlatTax
import com.rbleggi.taxsystem.service.strategy.PercentageTax
import kotlin.test.*

class TaxTemplateMethodTest {
    @Test
    fun `PercentageBill applies a rate over the price`() {
        assertEquals(180.0, PercentageBill(18.0).computeTax(1000.0), 0.01)
    }

    @Test
    fun `FlatBill ignores the price`() {
        assertEquals(50.0, FlatBill(50.0).computeTax(100.0), 0.01)
        assertEquals(50.0, FlatBill(50.0).computeTax(999.0), 0.01)
    }

    @Test
    fun `CappedBill limits the tax to a maximum`() {
        assertEquals(40.0, CappedBill(20.0, 500.0).computeTax(200.0), 0.01)
        assertEquals(500.0, CappedBill(20.0, 500.0).computeTax(10000.0), 0.01)
    }

    @Test
    fun `the default applyCap leaves the raw tax unchanged`() {
        assertEquals(120.0, PercentageBill(12.0).computeTax(1000.0), 0.01)
    }

    @Test
    fun `Template Method produces the same result as the Strategy version`() {
        val price = 1000.0
        assertEquals(PercentageTax(18.0).compute(price), PercentageBill(18.0).computeTax(price), 0.01)
        assertEquals(FlatTax(50.0).compute(price), FlatBill(50.0).computeTax(price), 0.01)
        assertEquals(CappedTax(20.0, 500.0).compute(price), CappedBill(20.0, 500.0).computeTax(price), 0.01)
    }
}
