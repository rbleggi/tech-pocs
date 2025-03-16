package com.rbleggi.taxsystem.specification

import com.rbleggi.taxsystem.model.Product
import com.rbleggi.taxsystem.model.State
import com.rbleggi.taxsystem.model.TaxRule

fun interface Specification<T> {
    fun matches(candidate: T): Boolean

    fun and(other: Specification<T>): Specification<T> =
        Specification { candidate -> this.matches(candidate) && other.matches(candidate) }

    fun or(other: Specification<T>): Specification<T> =
        Specification { candidate -> this.matches(candidate) || other.matches(candidate) }
}

class ProductSpecification(private val product: Product) : Specification<TaxRule> {
    override fun matches(candidate: TaxRule): Boolean = candidate.product == product
}

class StateSpecification(private val state: State) : Specification<TaxRule> {
    override fun matches(candidate: TaxRule): Boolean =
        candidate.state == state
}

class YearSpecification(private val year: Int) : Specification<TaxRule> {
    override fun matches(candidate: TaxRule): Boolean =
        candidate.year == year
}