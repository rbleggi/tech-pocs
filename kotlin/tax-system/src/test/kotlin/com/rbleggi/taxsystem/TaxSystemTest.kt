package com.rbleggi.taxsystem

import kotlin.test.*

class TaxRuleTest {
    @Test
    fun `tax returns correct amount`() {
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertEquals(127.5, rule.tax(1500.0), 0.01)
    }

    @Test
    fun `tax with zero rate returns zero`() {
        val rule = TaxRule("CA", "Book", 2024, 0.0)
        assertEquals(0.0, rule.tax(30.0))
    }

    @Test
    fun `tax scales linearly with price`() {
        val rule = TaxRule("TX", "Pizza", 2024, 4.0)
        assertEquals(0.8, rule.tax(20.0), 0.001)
    }
}

class SpecificationTest {
    private val laptopCA2023 = TaxRule("CA", "Laptop", 2023, 8.5)
    private val laptopTX2023 = TaxRule("TX", "Laptop", 2023, 6.25)
    private val bookCA2024 = TaxRule("CA", "Book", 2024, 0.0)
    private val pizzaTX2024 = TaxRule("TX", "Pizza", 2024, 4.0)

    @Test
    fun `stateSpec matches correct state`() {
        val spec = stateSpec("CA")
        assertTrue(spec.matches(laptopCA2023))
        assertFalse(spec.matches(laptopTX2023))
    }

    @Test
    fun `yearSpec matches correct year`() {
        val spec = yearSpec(2023)
        assertTrue(spec.matches(laptopCA2023))
        assertFalse(spec.matches(bookCA2024))
    }

    @Test
    fun `productSpec matches correct product`() {
        val spec = productSpec("Laptop")
        assertTrue(spec.matches(laptopCA2023))
        assertFalse(spec.matches(bookCA2024))
    }

    @Test
    fun `and combinator requires both specs`() {
        val spec = stateSpec("CA") and yearSpec(2023)
        assertTrue(spec.matches(laptopCA2023))
        assertFalse(spec.matches(laptopTX2023))
        assertFalse(spec.matches(bookCA2024))
    }

    @Test
    fun `or combinator requires either spec`() {
        val spec = stateSpec("CA") or stateSpec("TX")
        assertTrue(spec.matches(laptopCA2023))
        assertTrue(spec.matches(laptopTX2023))
        assertFalse(spec.matches(TaxRule("NY", "Laptop", 2023, 7.0)))
    }

    @Test
    fun `not combinator inverts result`() {
        val spec = stateSpec("CA").not()
        assertFalse(spec.matches(laptopCA2023))
        assertTrue(spec.matches(laptopTX2023))
    }

    @Test
    fun `taxSpec matches all three dimensions`() {
        val spec = taxSpec("Laptop", "CA", 2023)
        assertTrue(spec.matches(laptopCA2023))
        assertFalse(spec.matches(laptopTX2023))
        assertFalse(spec.matches(bookCA2024))
        assertFalse(spec.matches(pizzaTX2024))
    }

    @Test
    fun `taxSpec does not match wrong product`() {
        val spec = taxSpec("Book", "CA", 2023)
        assertFalse(spec.matches(laptopCA2023))
        assertTrue(spec.matches(TaxRule("CA", "Book", 2023, 1.0)))
    }

    @Test
    fun `composed spec with three rules`() {
        val spec = (stateSpec("CA") or stateSpec("TX")) and yearSpec(2024)
        assertFalse(spec.matches(laptopCA2023))
        assertTrue(spec.matches(bookCA2024))
        assertTrue(spec.matches(pizzaTX2024))
    }
}

class TaxCalculatorTest {
    private val rules = listOf(
        TaxRule("CA", "Laptop", 2023, 8.5),
        TaxRule("TX", "Laptop", 2023, 6.25),
        TaxRule("CA", "Book", 2024, 0.0),
        TaxRule("TX", "Pizza", 2024, 4.0)
    )
    private val calculator = TaxCalculator(rules)

    @Test
    fun `Laptop in CA 2023 applies 8 5 percent tax`() {
        assertEquals(1627.5, calculator.totalPrice("Laptop", 1500.0, "CA", 2023), 0.01)
    }

    @Test
    fun `Laptop in TX 2023 applies 6 25 percent tax`() {
        assertEquals(1593.75, calculator.totalPrice("Laptop", 1500.0, "TX", 2023), 0.01)
    }

    @Test
    fun `Book in CA 2024 applies zero tax`() {
        assertEquals(30.0, calculator.totalPrice("Book", 30.0, "CA", 2024), 0.01)
    }

    @Test
    fun `Pizza in TX 2024 applies 4 percent tax`() {
        assertEquals(20.8, calculator.totalPrice("Pizza", 20.0, "TX", 2024), 0.01)
    }

    @Test
    fun `unknown product returns base price`() {
        assertEquals(100.0, calculator.totalPrice("Unknown", 100.0, "CA", 2023), 0.01)
    }

    @Test
    fun `unknown year returns base price`() {
        assertEquals(1500.0, calculator.totalPrice("Laptop", 1500.0, "CA", 2025), 0.01)
    }

    @Test
    fun `unknown state returns base price`() {
        assertEquals(1500.0, calculator.totalPrice("Laptop", 1500.0, "NY", 2023), 0.01)
    }
}

class FlexibleTaxCalculatorTest {
    private val rules = listOf(
        TaxRule("CA", "Laptop", 2023, 8.5),
        TaxRule("CA", "Laptop", 2024, 9.0),
        TaxRule("TX", "Laptop", 2023, 6.25),
        TaxRule("CA", "Book", 2024, 0.0),
        TaxRule("TX", "Pizza", 2024, 4.0)
    )
    private val calculator = FlexibleTaxCalculator(rules)

    @Test
    fun `Laptop in CA 2023`() {
        assertEquals(1627.5, calculator.totalPrice("Laptop", 1500.0, "CA", 2023), 0.01)
    }

    @Test
    fun `Laptop in CA 2024`() {
        assertEquals(1635.0, calculator.totalPrice("Laptop", 1500.0, "CA", 2024), 0.01)
    }

    @Test
    fun `Laptop in CA 2025 returns base price`() {
        assertEquals(1500.0, calculator.totalPrice("Laptop", 1500.0, "CA", 2025), 0.01)
    }

    @Test
    fun `Pizza in TX 2024`() {
        assertEquals(20.8, calculator.totalPrice("Pizza", 20.0, "TX", 2024), 0.01)
    }

    @Test
    fun `unknown product returns base price`() {
        assertEquals(100.0, calculator.totalPrice("Unknown", 100.0, "CA", 2023), 0.01)
    }

    @Test
    fun `unknown state returns base price`() {
        assertEquals(1500.0, calculator.totalPrice("Laptop", 1500.0, "NY", 2023), 0.01)
    }

    @Test
    fun `Book in CA 2024 zero tax`() {
        assertEquals(30.0, calculator.totalPrice("Book", 30.0, "CA", 2024), 0.01)
    }
}
