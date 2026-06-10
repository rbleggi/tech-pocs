package com.rbleggi.taxsystem.service.templatemethod

abstract class TaxTemplateMethod {
    fun computeTax(price: Double): Double = applyCap(rawTax(price))

    protected abstract fun rawTax(price: Double): Double

    protected open fun applyCap(tax: Double): Double = tax
}

class PercentageBill(private val rate: Double) : TaxTemplateMethod() {
    override fun rawTax(price: Double): Double = price * (rate / 100)
}

class FlatBill(private val amount: Double) : TaxTemplateMethod() {
    override fun rawTax(price: Double): Double = amount
}

class CappedBill(private val rate: Double, private val max: Double) : TaxTemplateMethod() {
    override fun rawTax(price: Double): Double = price * (rate / 100)
    override fun applyCap(tax: Double): Double = minOf(tax, max)
}
