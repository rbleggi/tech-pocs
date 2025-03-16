package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.Product
import com.rbleggi.taxsystem.model.State
import com.rbleggi.taxsystem.model.TaxRule
import com.rbleggi.taxsystem.specification.ProductSpecification
import com.rbleggi.taxsystem.specification.StateSpecification
import com.rbleggi.taxsystem.specification.YearSpecification

class TaxCalculator(private val rules: List<TaxRule>) {

    fun calculateTotalPrice(product: Product, state: State, year: Int): Double {
        val spec = ProductSpecification(product)
            .and(StateSpecification(state))
            .and(YearSpecification(year))

        val tax = rules.firstOrNull { rule -> spec.matches(rule) }
            ?.calculateTax() ?: 0.0

        return product.price + tax
    }
}