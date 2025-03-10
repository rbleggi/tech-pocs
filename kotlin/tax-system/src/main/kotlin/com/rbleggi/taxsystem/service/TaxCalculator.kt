package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.Product
import com.rbleggi.taxsystem.model.State
import com.rbleggi.taxsystem.model.TaxRule

class TaxCalculator(private val taxRules: List<TaxRule>) {

    fun calculateTotalPrice(product: Product, state: State, year: Int): Double {
        val applicableTaxRule = taxRules.find { 
            it.state == state && it.product == product && it.year == year
        }

        val taxAmount = applicableTaxRule?.calculateTax() ?: 0.0
        return product.price + taxAmount
    }
}
