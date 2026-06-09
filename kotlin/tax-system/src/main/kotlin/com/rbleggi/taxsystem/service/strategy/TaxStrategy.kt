package com.rbleggi.taxsystem.service.strategy

interface TaxStrategy {
    fun compute(price: Double): Double
}

class PercentageTax(val rate: Double) : TaxStrategy {
    override fun compute(price: Double): Double = price * (rate / 100)
}

class FlatTax(val amount: Double) : TaxStrategy {
    override fun compute(price: Double): Double = amount
}

class CappedTax(val rate: Double, val max: Double) : TaxStrategy {
    override fun compute(price: Double): Double = minOf(price * (rate / 100), max)
}
