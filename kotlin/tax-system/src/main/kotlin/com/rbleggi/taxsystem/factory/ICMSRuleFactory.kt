package com.rbleggi.taxsystem.factory

import com.rbleggi.taxsystem.model.CappedTax
import com.rbleggi.taxsystem.model.FlatTax
import com.rbleggi.taxsystem.model.PercentageTax
import com.rbleggi.taxsystem.model.TaxRule
import com.rbleggi.taxsystem.model.TaxStrategy

class ICMSRuleFactory : TaxRuleFactory {
    private val products = listOf("Electronics", "Refrigerator")
    private val strategies: Map<String, TaxStrategy> = mapOf(
        "SP" to PercentageTax(18.0),
        "RJ" to CappedTax(20.0, 500.0),
        "MG" to PercentageTax(12.0),
        "RS" to FlatTax(50.0)
    )

    override fun rules(): List<TaxRule> =
        products.flatMap { product ->
            strategies.map { (state, strategy) -> TaxRule(product, state, strategy) }
        }
}
