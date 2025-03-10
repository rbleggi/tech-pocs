package com.rbleggi.taxsystem

import com.rbleggi.taxsystem.model.*
import com.rbleggi.taxsystem.service.TaxCalculator

fun main() {
    val laptop = Electronic(name = "Laptop", price = 1500.0)
    val book = Book(name = "Scala Programming", price = 30.0)
    val pizza = Food(name = "Pizza", price = 15.0)

    val california = State(name = "California", code = "CA")
    val texas = State(name = "Texas", code = "TX")

    val taxRules = listOf(
        TaxRule(state = california, product = laptop, year = 2025, taxRate = 8.5),
        TaxRule(state = texas, product = laptop, year = 2025, taxRate = 6.25),
        TaxRule(state = california, product = book, year = 2025, taxRate = 0.0),
        TaxRule(state = texas, product = pizza, year = 2025, taxRate = 4.0)
    )

    val taxCalculator = TaxCalculator(taxRules)

    println("Total price for Laptop in CA: \$${taxCalculator.calculateTotalPrice(laptop, california, 2025)}")
    println("Total price for Laptop in TX: \$${taxCalculator.calculateTotalPrice(laptop, texas, 2025)}")
    println("Total price for Book in CA: \$${taxCalculator.calculateTotalPrice(book, california, 2025)}")
    println("Total price for Pizza in TX: \$${taxCalculator.calculateTotalPrice(pizza, texas, 2025)}")
}
