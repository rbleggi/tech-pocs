package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.TaxRule
import com.rbleggi.taxsystem.service.strategy.CappedTax
import com.rbleggi.taxsystem.service.strategy.FlatTax
import com.rbleggi.taxsystem.service.strategy.PercentageTax
import com.rbleggi.taxsystem.service.templatemethod.CappedBill
import com.rbleggi.taxsystem.service.templatemethod.PercentageBill
import kotlin.test.*

class TaxSystemServiceTest {
    private val service = TaxSystemService(
        listOf(
            TaxRule("Electronics", "SP", PercentageTax(18.0)),
            TaxRule("Refrigerator", "RJ", CappedTax(20.0, 500.0)),
            TaxRule("Electronics", "MG", PercentageTax(12.0)),
            TaxRule("Electronics", "RS", FlatTax(50.0))
        )
    )

    @Test
    fun `Electronics in SP applies 18 percent ICMS`() {
        assertEquals(1180.0, service.totalPrice("Electronics", 1000.0, "SP"), 0.01)
    }

    @Test
    fun `Refrigerator in RJ applies 20 percent ICMS below the cap`() {
        assertEquals(2400.0, service.totalPrice("Refrigerator", 2000.0, "RJ"), 0.01)
    }

    @Test
    fun `Refrigerator in RJ caps the ICMS at 500`() {
        assertEquals(5500.0, service.totalPrice("Refrigerator", 5000.0, "RJ"), 0.01)
    }

    @Test
    fun `Electronics in MG applies 12 percent ICMS`() {
        assertEquals(1120.0, service.totalPrice("Electronics", 1000.0, "MG"), 0.01)
    }

    @Test
    fun `Electronics in RS applies a flat ICMS of 50`() {
        assertEquals(1050.0, service.totalPrice("Electronics", 1000.0, "RS"), 0.01)
    }

    @Test
    fun `unknown product returns base price`() {
        assertEquals(100.0, service.totalPrice("Unknown", 100.0, "SP"), 0.01)
    }

    @Test
    fun `unknown state returns base price`() {
        assertEquals(1000.0, service.totalPrice("Electronics", 1000.0, "BA"), 0.01)
    }

    @Test
    fun `taxByTemplate computes via the Template Method`() {
        assertEquals(180.0, service.taxByTemplate(PercentageBill(18.0), 1000.0), 0.01)
        assertEquals(500.0, service.taxByTemplate(CappedBill(20.0, 500.0), 5000.0), 0.01)
    }

    @Test
    fun `totalPriceByTemplate adds the template tax to the base price`() {
        assertEquals(1180.0, service.totalPriceByTemplate(PercentageBill(18.0), 1000.0), 0.01)
    }
}
