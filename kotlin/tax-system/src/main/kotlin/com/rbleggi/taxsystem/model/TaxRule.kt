package com.rbleggi.taxsystem.model

data class TaxRule(
    val product: String,
    val state: String,
    val strategy: TaxStrategy
) {
    fun tax(price: Double): Double = strategy.compute(price)
}
