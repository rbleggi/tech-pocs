package com.rbleggi.taxsystem.calculator

import com.rbleggi.taxsystem.model.TaxRule

class TaxCalculator(private val rules: List<TaxRule>) {
    fun tax(product: String, price: Double, state: String): Double =
        rules.firstOrNull { it.product == product && it.state == state }?.tax(price) ?: 0.0

    fun totalPrice(product: String, price: Double, state: String): Double =
        price + tax(product, price, state)
}
