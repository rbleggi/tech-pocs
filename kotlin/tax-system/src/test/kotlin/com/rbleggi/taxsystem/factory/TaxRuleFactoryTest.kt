package com.rbleggi.taxsystem.factory

import kotlin.test.*

class ICMSRuleFactoryTest {
    private val rules = ICMSRuleFactory().rules()

    @Test
    fun `builds one rule per product and state`() {
        assertEquals(8, rules.size)
    }

    @Test
    fun `covers both products`() {
        assertEquals(setOf("Electronics", "Refrigerator"), rules.map { it.product }.toSet())
    }

    @Test
    fun `tax per state is correct`() {
        assertEquals(18.0, rules.first { it.state == "SP" && it.product == "Electronics" }.tax(100.0), 0.01)
        assertEquals(20.0, rules.first { it.state == "RJ" && it.product == "Refrigerator" }.tax(100.0), 0.01)
        assertEquals(12.0, rules.first { it.state == "MG" && it.product == "Electronics" }.tax(100.0), 0.01)
        assertEquals(50.0, rules.first { it.state == "RS" && it.product == "Refrigerator" }.tax(100.0), 0.01)
    }

    @Test
    fun `RJ caps the tax at its maximum`() {
        assertEquals(500.0, rules.first { it.state == "RJ" && it.product == "Electronics" }.tax(5000.0), 0.01)
    }

    @Test
    fun `RS applies a flat tax regardless of price`() {
        assertEquals(50.0, rules.first { it.state == "RS" && it.product == "Electronics" }.tax(99999.0), 0.01)
    }
}

class TaxRuleRegistryTest {
    @Test
    fun `aggregates rules from all factories`() {
        assertEquals(8, TaxRuleRegistry.allRules().size)
    }
}
