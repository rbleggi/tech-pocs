package com.rbleggi.taxsystem.model

data class TaxRule(val state: State, val product: Product, val year: Int, val taxRate: Double) {
    fun calculateTax(): Double {
        return product.price * (taxRate / 100)
    }
}
