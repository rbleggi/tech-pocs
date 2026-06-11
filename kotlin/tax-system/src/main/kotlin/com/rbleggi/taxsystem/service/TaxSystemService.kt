package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.TaxRule
import com.rbleggi.taxsystem.service.templatemethod.TaxTemplateMethod

class TaxSystemService(private val rules: List<TaxRule>) {
    fun tax(product: String, price: Double, state: String): Double =
        rules.firstOrNull { it.product == product && it.state == state }?.strategy?.compute(price) ?: 0.0

    fun totalPrice(product: String, price: Double, state: String): Double =
        price + tax(product, price, state)

    fun taxByTemplate(bill: TaxTemplateMethod, price: Double): Double =
        bill.computeTax(price)

    fun totalPriceByTemplate(bill: TaxTemplateMethod, price: Double): Double =
        price + taxByTemplate(bill, price)
}
