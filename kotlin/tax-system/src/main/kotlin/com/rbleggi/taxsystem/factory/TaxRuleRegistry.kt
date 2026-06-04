package com.rbleggi.taxsystem.factory

import com.rbleggi.taxsystem.model.TaxRule

object TaxRuleRegistry {
    private val factories: List<TaxRuleFactory> =
        listOf(ICMSRuleFactory())

    fun allRules(): List<TaxRule> = factories.flatMap { it.rules() }
}
