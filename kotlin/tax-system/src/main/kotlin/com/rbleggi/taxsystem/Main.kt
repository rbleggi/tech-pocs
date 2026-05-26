package com.rbleggi.taxsystem

data class TaxRule(val state: String, val product: String, val year: Int, val rate: Double) {
    fun tax(price: Double): Double = price * (rate / 100)
}

fun interface Specification<T> {
    fun matches(candidate: T): Boolean

    infix fun and(other: Specification<T>): Specification<T> =
        Specification { matches(it) && other.matches(it) }

    infix fun or(other: Specification<T>): Specification<T> =
        Specification { matches(it) || other.matches(it) }

    fun not(): Specification<T> = Specification { !matches(it) }
}

fun stateSpec(state: String): Specification<TaxRule> = Specification { it.state == state }
fun yearSpec(year: Int): Specification<TaxRule> = Specification { it.year == year }
fun productSpec(product: String): Specification<TaxRule> = Specification { it.product == product }

fun taxSpec(product: String, state: String, year: Int): Specification<TaxRule> =
    productSpec(product) and stateSpec(state) and yearSpec(year)

class TaxCalculator(private val rules: List<TaxRule>) {
    private val knownYears = setOf(2023, 2024)
    private val knownStates = setOf("CA", "TX")

    fun totalPrice(product: String, price: Double, state: String, year: Int): Double {
        if (year !in knownYears || state !in knownStates) return price
        val spec = taxSpec(product, state, year)
        val tax = rules.firstOrNull { spec.matches(it) }?.tax(price) ?: 0.0
        return price + tax
    }
}

class FlexibleTaxCalculator(private val rules: List<TaxRule>) {
    fun totalPrice(product: String, price: Double, state: String, year: Int): Double {
        val spec = taxSpec(product, state, year)
        val tax = rules.firstOrNull { spec.matches(it) }?.tax(price) ?: 0.0
        return price + tax
    }
}

fun main() {
    println("Tax System")
}
