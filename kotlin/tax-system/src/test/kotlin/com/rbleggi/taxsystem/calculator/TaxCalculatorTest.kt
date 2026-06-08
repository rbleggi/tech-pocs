package com.rbleggi.taxsystem.calculator

import com.rbleggi.taxsystem.factory.TaxRuleRegistry
import kotlin.test.*

class TaxCalculatorTest {
    private val calculator = TaxCalculator(TaxRuleRegistry.allRules())

    @Test
    fun `Electronics in SP applies 18 percent ICMS`() {
        assertEquals(1180.0, calculator.totalPrice("Electronics", 1000.0, "SP"), 0.01)
    }

    @Test
    fun `Refrigerator in RJ applies 20 percent ICMS below the cap`() {
        assertEquals(2400.0, calculator.totalPrice("Refrigerator", 2000.0, "RJ"), 0.01)
    }

    @Test
    fun `Refrigerator in RJ caps the ICMS at 500`() {
        assertEquals(5500.0, calculator.totalPrice("Refrigerator", 5000.0, "RJ"), 0.01)
    }

    @Test
    fun `Electronics in MG applies 12 percent ICMS`() {
        assertEquals(1120.0, calculator.totalPrice("Electronics", 1000.0, "MG"), 0.01)
    }

    @Test
    fun `Electronics in RS applies a flat ICMS of 50`() {
        assertEquals(1050.0, calculator.totalPrice("Electronics", 1000.0, "RS"), 0.01)
    }

    @Test
    fun `unknown product returns base price`() {
        assertEquals(100.0, calculator.totalPrice("Unknown", 100.0, "SP"), 0.01)
    }

    @Test
    fun `unknown state returns base price`() {
        assertEquals(1000.0, calculator.totalPrice("Electronics", 1000.0, "BA"), 0.01)
    }
}
