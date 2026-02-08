package com.rbleggi.taxsystem.impl2

import kotlin.test.*

class TaxRuleTest {
    @Test
    fun `calculateTax returns correct tax amount`() {
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertEquals(127.5, rule.calculateTax(1500.0), 0.01)
    }

    @Test
    fun `calculateTax with zero rate returns zero`() {
        val rule = TaxRule("CA", "Book", 2024, 0.0)
        assertEquals(0.0, rule.calculateTax(30.0))
    }
}

class StateSpecificationTest {
    @Test
    fun `isSatisfiedBy returns true for matching state`() {
        val spec = StateSpecification("CA")
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for different state`() {
        val spec = StateSpecification("CA")
        val rule = TaxRule("TX", "Laptop", 2023, 6.25)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class YearSpecificationTest {
    @Test
    fun `isSatisfiedBy returns true for matching year`() {
        val spec = YearSpecification(2023)
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for different year`() {
        val spec = YearSpecification(2023)
        val rule = TaxRule("CA", "Laptop", 2024, 9.0)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class CompoundSpecificationTest {
    @Test
    fun `isSatisfiedBy returns true when year and state match`() {
        val yearSpecs = listOf(YearSpecification(2023), YearSpecification(2024))
        val stateSpec = StateSpecification("CA")
        val spec = CompoundSpecification(yearSpecs, stateSpec)
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false when state does not match`() {
        val yearSpecs = listOf(YearSpecification(2023), YearSpecification(2024))
        val stateSpec = StateSpecification("CA")
        val spec = CompoundSpecification(yearSpecs, stateSpec)
        val rule = TaxRule("TX", "Laptop", 2023, 6.25)
        assertFalse(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false when year does not match`() {
        val yearSpecs = listOf(YearSpecification(2023), YearSpecification(2024))
        val stateSpec = StateSpecification("CA")
        val spec = CompoundSpecification(yearSpecs, stateSpec)
        val rule = TaxRule("CA", "Laptop", 2025, 9.0)
        assertFalse(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns true for multiple years`() {
        val yearSpecs = listOf(YearSpecification(2023), YearSpecification(2024))
        val stateSpec = StateSpecification("CA")
        val spec = CompoundSpecification(yearSpecs, stateSpec)
        val rule2024 = TaxRule("CA", "Laptop", 2024, 9.0)
        assertTrue(spec.isSatisfiedBy(rule2024))
    }
}

class ProductSpecificationTest {
    @Test
    fun `isSatisfiedBy returns true when product and compound spec match`() {
        val compoundSpec = CompoundSpecification(
            yearSpecs = listOf(YearSpecification(2023)),
            stateSpec = StateSpecification("CA")
        )
        val spec = ProductSpecification("Laptop", compoundSpec)
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false when product does not match`() {
        val compoundSpec = CompoundSpecification(
            yearSpecs = listOf(YearSpecification(2023)),
            stateSpec = StateSpecification("CA")
        )
        val spec = ProductSpecification("Laptop", compoundSpec)
        val rule = TaxRule("CA", "Book", 2023, 0.0)
        assertFalse(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false when compound spec does not match`() {
        val compoundSpec = CompoundSpecification(
            yearSpecs = listOf(YearSpecification(2023)),
            stateSpec = StateSpecification("CA")
        )
        val spec = ProductSpecification("Laptop", compoundSpec)
        val rule = TaxRule("TX", "Laptop", 2023, 6.25)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class TaxCalculatorCompoundTest {
    private val taxRules = listOf(
        TaxRule("CA", "Laptop", 2023, 8.5),
        TaxRule("CA", "Laptop", 2024, 9.0),
        TaxRule("TX", "Laptop", 2023, 6.25),
        TaxRule("CA", "Book", 2024, 0.0),
        TaxRule("TX", "Pizza", 2024, 4.0)
    )
    private val calculator = TaxCalculatorCompound(taxRules)

    @Test
    fun `calculateTotalPriceCompound for Laptop in CA 2023`() {
        val result = calculator.calculateTotalPriceCompound("Laptop", 1500.0, "CA", 2023)
        assertEquals(1627.5, result, 0.01)
    }

    @Test
    fun `calculateTotalPriceCompound for Laptop in CA 2024`() {
        val result = calculator.calculateTotalPriceCompound("Laptop", 1500.0, "CA", 2024)
        assertEquals(1635.0, result, 0.01)
    }

    @Test
    fun `calculateTotalPriceCompound for Laptop in CA 2025 returns base price`() {
        val result = calculator.calculateTotalPriceCompound("Laptop", 1500.0, "CA", 2025)
        assertEquals(1500.0, result, 0.01)
    }

    @Test
    fun `calculateTotalPriceCompound for Pizza in TX 2024`() {
        val result = calculator.calculateTotalPriceCompound("Pizza", 20.0, "TX", 2024)
        assertEquals(20.8, result, 0.01)
    }

    @Test
    fun `calculateTotalPriceCompound for unknown product returns base price`() {
        val result = calculator.calculateTotalPriceCompound("Unknown", 100.0, "CA", 2023)
        assertEquals(100.0, result, 0.01)
    }

    @Test
    fun `calculateTotalPriceCompound for unknown state returns base price`() {
        val result = calculator.calculateTotalPriceCompound("Laptop", 1500.0, "NY", 2023)
        assertEquals(1500.0, result, 0.01)
    }
}
