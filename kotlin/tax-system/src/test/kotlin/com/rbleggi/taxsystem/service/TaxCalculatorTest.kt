package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.Book
import com.rbleggi.taxsystem.model.Electronic
import com.rbleggi.taxsystem.model.State
import com.rbleggi.taxsystem.model.TaxRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TaxCalculatorTest {

    private val laptop = Electronic(name = "Laptop", price = 1500.0)
    private val book = Book(name = "Book", price = 30.0)

    private val california = State(name = "California", code = "CA")
    private val texas = State(name = "Texas", code = "TX")

    private val taxRules = listOf(
        TaxRule(state = california, product = laptop, year = 2025, taxRate = 8.5),
        TaxRule(state = texas, product = laptop, year = 2025, taxRate = 6.25),
        TaxRule(state = california, product = book, year = 2025, taxRate = 0.0)
    )

    private val taxCalculator = TaxCalculator(taxRules)

    @Test
    fun `should calculate total price for Laptop in California`() {
        val totalPrice = taxCalculator.calculateTotalPrice(laptop, california, 2025)
        assertEquals(1627.5, totalPrice, 0.01)
    }

    @Test
    fun `should calculate total price for Laptop in Texas`() {
        val totalPrice = taxCalculator.calculateTotalPrice(laptop, texas, 2025)
        assertEquals(1593.75, totalPrice, 0.01)
    }

    @Test
    fun `should calculate total price for Book in California`() {
        val totalPrice = taxCalculator.calculateTotalPrice(book, california, 2025)
        assertEquals(30.0, totalPrice, 0.01)
    }

    @Test
    fun `should return product price when no tax rule exists`() {
        val unknownState = State(name = "New York", code = "NY")
        val totalPrice = taxCalculator.calculateTotalPrice(laptop, unknownState, 2025)
        assertEquals(1500.0, totalPrice, 0.01)
    }
}
