package com.rbleggi.taxsystem

data class TaxRule(val state: String, val product: String, val year: Int, val taxRate: Double) {
    fun calculateTax(price: Double): Double = price * (taxRate / 100)
}

interface StateSpecification {
    fun isSatisfiedBy(candidate: TaxRule): Boolean
}

class CASpecification2023 : StateSpecification {
    override fun isSatisfiedBy(candidate: TaxRule): Boolean = candidate.state == "CA"
}

class TXSpecification2023 : StateSpecification {
    override fun isSatisfiedBy(candidate: TaxRule): Boolean = candidate.state == "TX"
}

class CASpecification2024 : StateSpecification {
    override fun isSatisfiedBy(candidate: TaxRule): Boolean = candidate.state == "CA"
}

class TXSpecification2024 : StateSpecification {
    override fun isSatisfiedBy(candidate: TaxRule): Boolean = candidate.state == "TX"
}

interface YearSpecification {
    fun isSatisfiedBy(candidate: TaxRule): Boolean
}

class Year2023Specification(private val stateSpecs: List<StateSpecification>) : YearSpecification {
    override fun isSatisfiedBy(candidate: TaxRule): Boolean =
        candidate.year == 2023 && stateSpecs.any { it.isSatisfiedBy(candidate) }
}

class Year2024Specification(private val stateSpecs: List<StateSpecification>) : YearSpecification {
    override fun isSatisfiedBy(candidate: TaxRule): Boolean =
        candidate.year == 2024 && stateSpecs.any { it.isSatisfiedBy(candidate) }
}

class TaxCalculator(private val rules: List<TaxRule>) {
    fun calculateTotalPrice(product: String, price: Double, state: String, year: Int): Double {
        val yearSpec: YearSpecification? = when (year) {
            2023 -> Year2023Specification(listOf(CASpecification2023(), TXSpecification2023()))
            2024 -> Year2024Specification(listOf(CASpecification2024(), TXSpecification2024()))
            else -> null
        }

        val tax = rules.firstOrNull { rule ->
            rule.product == product && yearSpec?.isSatisfiedBy(rule) == true && rule.state == state
        }?.calculateTax(price) ?: 0.0

        return price + tax
    }
}

class SimpleStateSpecification(private val state: String) {
    fun isSatisfiedBy(candidate: TaxRule) = candidate.state == state
}

class SimpleYearSpecification(private val year: Int) {
    fun isSatisfiedBy(candidate: TaxRule) = candidate.year == year
}

class CompoundSpecification(
    private val yearSpecs: List<SimpleYearSpecification>,
    private val stateSpec: SimpleStateSpecification
) {
    fun isSatisfiedBy(candidate: TaxRule): Boolean =
        yearSpecs.any { it.isSatisfiedBy(candidate) } && stateSpec.isSatisfiedBy(candidate)
}

class ProductSpecification(
    private val product: String,
    private val compoundSpec: CompoundSpecification
) {
    fun isSatisfiedBy(candidate: TaxRule): Boolean =
        candidate.product == product && compoundSpec.isSatisfiedBy(candidate)
}

class TaxCalculatorCompound(private val rules: List<TaxRule>) {
    fun calculateTotalPriceCompound(product: String, price: Double, state: String, year: Int): Double {
        val spec = ProductSpecification(
            product,
            CompoundSpecification(
                yearSpecs = listOf(SimpleYearSpecification(2023), SimpleYearSpecification(2024)),
                stateSpec = SimpleStateSpecification(state)
            )
        )

        val tax = rules.firstOrNull { rule ->
            rule.year == year && spec.isSatisfiedBy(rule)
        }?.calculateTax(price) ?: 0.0

        return price + tax
    }
}

fun main() {
    println("Tax System")
}
