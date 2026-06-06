package com.rbleggi.taxsystem

import com.rbleggi.taxsystem.calculator.TaxCalculator
import com.rbleggi.taxsystem.factory.TaxRuleRegistry

fun main() {
    val calculator = TaxCalculator(TaxRuleRegistry.allRules())

    println("Tax System")

    println("\n== Calculate the price of a product ==")
    val product = "Electronics"
    val state = "SP"
    val price = 1000.0
    val tax = calculator.tax(product, price, state)
    val total = calculator.totalPrice(product, price, state)
    println("Product: $product")
    println("State: $state")
    println("Base price: R\$$price")
    println("Tax: R\$$tax")
    println("Total price: R\$$total")

    println("\n== Total price by state (ICMS strategy per state) ==")
    println("Electronics in SP (18% percentage): ${calculator.totalPrice("Electronics", 1000.0, "SP")}")
    println("Refrigerator in RJ (20% capped at R\$500): ${calculator.totalPrice("Refrigerator", 5000.0, "RJ")}")
    println("Electronics in MG (12% percentage): ${calculator.totalPrice("Electronics", 1000.0, "MG")}")
    println("Electronics in RS (flat R\$50): ${calculator.totalPrice("Electronics", 1000.0, "RS")}")
}
