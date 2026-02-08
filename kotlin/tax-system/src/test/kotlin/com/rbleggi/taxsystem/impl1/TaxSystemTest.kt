package com.rbleggi.taxsystem.impl1

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

class CASpecification2023Test {
    @Test
    fun `isSatisfiedBy returns true for CA state`() {
        val spec = CASpecification2023()
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for TX state`() {
        val spec = CASpecification2023()
        val rule = TaxRule("TX", "Laptop", 2023, 6.25)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class TXSpecification2023Test {
    @Test
    fun `isSatisfiedBy returns true for TX state`() {
        val spec = TXSpecification2023()
        val rule = TaxRule("TX", "Laptop", 2023, 6.25)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for CA state`() {
        val spec = TXSpecification2023()
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class Year2023SpecificationTest {
    @Test
    fun `isSatisfiedBy returns true for 2023 CA`() {
        val spec = Year2023Specification(listOf(CASpecification2023(), TXSpecification2023()))
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns true for 2023 TX`() {
        val spec = Year2023Specification(listOf(CASpecification2023(), TXSpecification2023()))
        val rule = TaxRule("TX", "Laptop", 2023, 6.25)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for 2024`() {
        val spec = Year2023Specification(listOf(CASpecification2023(), TXSpecification2023()))
        val rule = TaxRule("CA", "Laptop", 2024, 9.0)
        assertFalse(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for unknown state`() {
        val spec = Year2023Specification(listOf(CASpecification2023(), TXSpecification2023()))
        val rule = TaxRule("NY", "Laptop", 2023, 7.0)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class Year2024SpecificationTest {
    @Test
    fun `isSatisfiedBy returns true for 2024 CA`() {
        val spec = Year2024Specification(listOf(CASpecification2024(), TXSpecification2024()))
        val rule = TaxRule("CA", "Book", 2024, 0.0)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns true for 2024 TX`() {
        val spec = Year2024Specification(listOf(CASpecification2024(), TXSpecification2024()))
        val rule = TaxRule("TX", "Pizza", 2024, 4.0)
        assertTrue(spec.isSatisfiedBy(rule))
    }

    @Test
    fun `isSatisfiedBy returns false for 2023`() {
        val spec = Year2024Specification(listOf(CASpecification2024(), TXSpecification2024()))
        val rule = TaxRule("CA", "Laptop", 2023, 8.5)
        assertFalse(spec.isSatisfiedBy(rule))
    }
}

class TaxCalculatorTest {
    private val taxRules = listOf(
        TaxRule("CA", "Laptop", 2023, 8.5),
        TaxRule("TX", "Laptop", 2023, 6.25),
        TaxRule("CA", "Book", 2024, 0.0),
        TaxRule("TX", "Pizza", 2024, 4.0)
    )
    private val calculator = TaxCalculator(taxRules)

    @Test
    fun `calculateTotalPrice for Laptop in CA 2023`() {
        val result = calculator.calculateTotalPrice("Laptop", 1500.0, "CA", 2023)
        assertEquals(1627.5, result, 0.01)
    }

    @Test
    fun `calculateTotalPrice for Laptop in TX 2023`() {
        val result = calculator.calculateTotalPrice("Laptop", 1500.0, "TX", 2023)
        assertEquals(1593.75, result, 0.01)
    }

    @Test
    fun `calculateTotalPrice for Book in CA 2024`() {
        val result = calculator.calculateTotalPrice("Book", 30.0, "CA", 2024)
        assertEquals(30.0, result, 0.01)
    }

    @Test
    fun `calculateTotalPrice for Pizza in TX 2024`() {
        val result = calculator.calculateTotalPrice("Pizza", 20.0, "TX", 2024)
        assertEquals(20.8, result, 0.01)
    }

    @Test
    fun `calculateTotalPrice for unknown product returns base price`() {
        val result = calculator.calculateTotalPrice("Unknown", 100.0, "CA", 2023)
        assertEquals(100.0, result, 0.01)
    }

    @Test
    fun `calculateTotalPrice for unknown year returns base price`() {
        val result = calculator.calculateTotalPrice("Laptop", 1500.0, "CA", 2025)
        assertEquals(1500.0, result, 0.01)
    }

    @Test
    fun `calculateTotalPrice for unknown state returns base price`() {
        val result = calculator.calculateTotalPrice("Laptop", 1500.0, "NY", 2023)
        assertEquals(1500.0, result, 0.01)
    }
}
