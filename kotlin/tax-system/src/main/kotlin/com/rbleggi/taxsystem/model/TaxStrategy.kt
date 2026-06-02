package com.rbleggi.taxsystem.model

fun interface TaxStrategy {
    fun compute(price: Double): Double
}

data class PercentageTax(val rate: Double) : TaxStrategy {
    override fun compute(price: Double): Double = price * (rate / 100)
}

data class FlatTax(val amount: Double) : TaxStrategy {
    override fun compute(price: Double): Double = amount
}

data class CappedTax(val rate: Double, val max: Double) : TaxStrategy {
    override fun compute(price: Double): Double = minOf(price * (rate / 100), max)
}
